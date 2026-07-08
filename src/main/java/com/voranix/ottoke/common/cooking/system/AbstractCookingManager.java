package com.voranix.ottoke.common.cooking.system;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * The decision-making piece every cooking station shares: given its container and process, whether
 * to start a new cook, keep advancing one, cancel it, or finish and emit output. This used to be a
 * plain interface; it is a class now because that state-machine ({@link #tick}) is identical for
 * every station that fits the "input(s) + time -> output" shape - Freidora, Parrilla, Olla, and a
 * future drink machine all follow it - so it belongs written once here, not copy-pasted into every
 * station's manager.
 * <p>
 * What genuinely differs per station is (a) where its recipes come from, (b) which input slots a
 * specific recipe consumes, and (c) whether/how it uses fuel. Those questions are the only abstract
 * methods below; everything a station would otherwise have to duplicate - the tick loop itself, and
 * the output-stack merge math - is implemented here.
 * <p>
 * Contains nothing about the Freidora specifically. {@code FreidoraManager} extends this and
 * supplies only {@link #findMatchingRecipe}, {@link #consumeInputs}, {@link #hasFuel}, and
 * {@link #consumeFuel}.
 */
public abstract class AbstractCookingManager {

    /**
     * The full lifecycle of a cook, run once per server tick. Final on purpose: every station goes
     * through the exact same state machine (look for a recipe -> start/continue/cancel -> finish),
     * so a subclass customizes behavior only through the abstract hooks below, never by replacing
     * this method.
     * <ol>
     *     <li>Client side never simulates cooking - the server is authoritative and syncs progress
     *     to the client via {@link CookingProcess} being a {@code ContainerData}.</li>
     *     <li>Look up a recipe matching the container's current input via {@link #findMatchingRecipe}.</li>
     *     <li>If no recipe matches, its result can't fit in the output slot, or there isn't enough
     *     fuel available right now, cancel any in-progress cook (a player pulled the ingredient or
     *     the fuel out, or the output slot is full/blocked) and stop.</li>
     *     <li>If a cook wasn't already running, start one against the recipe that was just found.</li>
     *     <li>Advance the process by one tick.</li>
     *     <li>If that reached the recipe's total time, apply the output, let the subclass consume
     *     whatever inputs and fuel the recipe specifies, and reset the process for the next cook.</li>
     * </ol>
     */
    public final void tick(CookingContainer container, CookingProcess process, Level level) {
        if (level.isClientSide()) {
            return;
        }

        Optional<CookingRecipe> match = findMatchingRecipe(container, level);

        boolean canCook = match.isPresent()
                && canFitOutput(container, match.get().getCookingResult().result())
                && hasFuel(container, match.get());

        if (!canCook) {
            if (process.isActive()) {
                process.reset();
            }
            return;
        }

        CookingRecipe recipe = match.get();

        if (!process.isActive()) {
            process.start(recipe);
        }

        process.advance();

        if (process.getProgress() >= process.getTotalTime()) {
            applyOutput(container, recipe.getCookingResult());
            consumeInputs(container, recipe);
            consumeFuel(container, recipe);
            process.reset();
        }
    }

    /**
     * Where a station's recipes come from. This is the main thing that makes a Freidora different
     * from a Parrilla or an Olla: each queries a different recipe type/source (e.g. Freidora will
     * query {@code level.getRecipeManager()} for its own {@code RecipeType}). Left abstract because
     * the cooking system deliberately never assumes every station's recipes live in Minecraft's
     * RecipeManager - see {@link CookingRecipe}.
     */
    protected abstract Optional<CookingRecipe> findMatchingRecipe(CookingContainer container, Level level);

    /**
     * Removes whatever the just-finished recipe consumed from the container's input slots. Left
     * abstract because "how many of which input slots" is recipe-shape knowledge specific to each
     * station: a Freidora consumes one item from its single input slot, but an Olla recipe might
     * need to consume several different ingredients from several slots at once. Neither
     * {@link CookingContainer} nor {@link CookingRecipe} encode that shape generically, so the
     * concrete manager - which knows its own station's recipe format - is the right place to decide.
     */
    protected abstract void consumeInputs(CookingContainer container, CookingRecipe recipe);

    /**
     * Whether the station currently has enough fuel to pay for {@code recipe}. Checked every tick a
     * cook could be running, not only when it starts - pulling fuel out mid-cook cancels it, the
     * same way pulling the ingredient out or blocking the output does. A recipe with no fuel cost is
     * expected to trivially always have enough.
     * <p>
     * Left abstract rather than reading a "fuel slot" off {@link CookingContainer}: fuel has no
     * place in that interface's generic contract (a station without any fuel concept, e.g. a future
     * drink machine, would gain a meaningless slot just to satisfy this method). A station that does
     * use fuel reaches for its own concrete container type - see {@code FreidoraManager} and
     * {@code FreidoraInventory}.
     */
    protected abstract boolean hasFuel(CookingContainer container, CookingRecipe recipe);

    /**
     * Removes whatever fuel {@code recipe} charges for. Called once, at the same point as
     * {@link #consumeInputs}, when a cook finishes - not gradually over the cook's duration the way
     * vanilla furnaces deplete burn time. Left abstract for the same reason {@link #hasFuel} is.
     */
    protected abstract void consumeFuel(CookingContainer container, CookingRecipe recipe);

    /**
     * Whether {@code result} can be placed into the container's output slot right now: the slot is
     * empty, or it holds the same item with room left in the stack. Pure ItemStack arithmetic, true
     * for every station regardless of what it cooks, so it is implemented once here rather than
     * left for each station to re-derive. A station with an unusual output shape (multiple output
     * slots, byproducts) overrides this instead of duplicating {@link #tick}.
     */
    protected boolean canFitOutput(CookingContainer container, ItemStack result) {
        ItemStack currentOutput = container.getOutputItem();
        if (currentOutput.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameTags(currentOutput, result)) {
            return false;
        }
        return currentOutput.getCount() + result.getCount() <= currentOutput.getMaxStackSize();
    }

    /**
     * Places a finished recipe's {@link CookingResult} into the container's output slot, merging
     * into an existing stack when possible. Pure stack math, identical for every station, so - like
     * {@link #canFitOutput} - it is implemented once here. Only called after {@link #canFitOutput}
     * has already confirmed the item fits.
     * <p>
     * Takes the whole {@link CookingResult}, not just its {@code ItemStack}, on purpose: granting
     * {@code experience} or reacting to {@code quality} belong here once those mechanics exist - as
     * more logic inside this method, not as a new parameter threaded through {@link #tick}. Neither
     * is implemented yet; today this only ever reads {@code result()}.
     */
    protected void applyOutput(CookingContainer container, CookingResult result) {
        ItemStack outputStack = result.result();
        ItemStack currentOutput = container.getOutputItem();
        if (currentOutput.isEmpty()) {
            container.setOutputItem(outputStack.copy());
        } else {
            currentOutput.grow(outputStack.getCount());
        }
    }
}
