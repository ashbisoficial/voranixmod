package com.voranix.ottoke.common.cooking;

import com.voranix.voranixmod.common.cooking.system.AbstractCookingManager;
import com.voranix.voranixmod.common.cooking.system.CookingContainer;
import com.voranix.voranixmod.common.cooking.system.CookingRecipe;
import com.voranix.ottoke.common.registration.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;
import java.util.Optional;

/**
 * The first concrete {@link AbstractCookingManager}. Every method here is either Freidora-specific
 * wiring (which recipe type to query, how to reach {@link FreidoraInventory}'s fuel slot) or one of
 * the six responsibilities this manager owns; the cook/don't-cook state machine, output placement,
 * and the client-side skip all stay inherited, unchanged, from {@link AbstractCookingManager} - see
 * the class docs there for why those are shared instead of repeated here.
 * <p>
 * <b>Avanzar progreso</b> is deliberately not reimplemented: {@link AbstractCookingManager#tick}
 * already owns it via {@code process.advance()}. That is exactly the part meant to be reused as-is;
 * giving {@code FreidoraManager} its own copy would defeat the point of the shared state machine.
 * <p>
 * Quality and status effects are intentionally absent: {@code CookingResult.quality} stays whatever
 * placeholder the recipe set (see {@code CookingQualityEvaluator}), and nothing here grants or
 * removes a {@code MobEffect}.
 * <p>
 * Every method assumes the {@link CookingContainer} it receives is a {@link FreidoraInventory} - a
 * Freidora is always paired with one, so this manager is allowed to know that concrete detail even
 * though {@link AbstractCookingManager}'s contract only promises the generic interface.
 * <p>
 * {@link #INSTANCE} exists because this class holds no per-station state - every method's behavior
 * depends only on the {@link CookingContainer}/{@link CookingRecipe} passed in, never on a field
 * that varies per station - so every {@code FreidoraBlockEntity} in a world shares the same manager
 * instead of each allocating its own. The one field this class does carry ({@link #cachedRecipes})
 * is a cache of world-global data (every registered {@code voranixmod:freidora} recipe), identical for
 * every station and every caller, not per-station state - see {@link #findMatchingRecipe}.
 */
public class FreidoraManager extends AbstractCookingManager {
    public static final FreidoraManager INSTANCE = new FreidoraManager();

    /**
     * {@link RecipeManager#getAllRecipesFor} copies its whole result into a fresh {@code List} on
     * every call (it is not a cached view) - calling it unconditionally from {@link #tick} would mean
     * every loaded Freidora re-copies and re-scans the full Freidora recipe list every single server
     * tick, cooking or not. The list only ever actually changes when the server's {@link RecipeManager}
     * itself is replaced (a {@code /reload} or datapack sync installs a brand new instance), so caching
     * by that instance's identity is enough to make repeat ticks free: same {@code RecipeManager} means
     * the cached list is still correct, no re-query needed.
     */
    private RecipeManager cachedRecipeManager;
    private List<FreidoraRecipe> cachedRecipes = List.of();

    /**
     * Buscar receta: every {@code FreidoraRecipe} registered under {@code voranixmod:freidora} is a
     * candidate; the first one whose ingredient matches wins. A plain indexed loop instead of a
     * {@code Stream} pipeline - the stream/lambda/Optional-boxing machinery would otherwise be rebuilt
     * every tick for what is, at most, a handful of recipes to check.
     */
    @Override
    protected Optional<CookingRecipe> findMatchingRecipe(CookingContainer container, Level level) {
        List<FreidoraRecipe> recipes = getFreidoraRecipes(level);
        for (int i = 0; i < recipes.size(); i++) {
            FreidoraRecipe recipe = recipes.get(i);
            if (recipe.matches(container)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    private List<FreidoraRecipe> getFreidoraRecipes(Level level) {
        RecipeManager recipeManager = level.getRecipeManager();
        if (recipeManager != this.cachedRecipeManager) {
            this.cachedRecipeManager = recipeManager;
            this.cachedRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.FREIDORA.get());
        }
        return this.cachedRecipes;
    }

    /**
     * Validar combustible: a recipe with no fuel cost never blocks on fuel. When one is charged,
     * the fuel slot must hold a genuinely burnable item (reusing Forge's own fuel registry, so any
     * mod's fuel item works with no Ottoke-specific list to maintain) in at least the needed amount.
     */
    @Override
    protected boolean hasFuel(CookingContainer container, CookingRecipe recipe) {
        int fuelCost = fuelCost(recipe);
        if (fuelCost <= 0) {
            return true;
        }

        ItemStack fuel = ((FreidoraInventory) container).getFuelItem();
        return ForgeHooks.getBurnTime(fuel, null) > 0 && fuel.getCount() >= fuelCost;
    }

    /**
     * Consumir combustible: mirrors {@link #hasFuel}'s threshold, and - like
     * {@link AbstractCookingManager#consumeInputs} - only runs once a cook actually finishes.
     */
    @Override
    protected void consumeFuel(CookingContainer container, CookingRecipe recipe) {
        int fuelCost = fuelCost(recipe);
        if (fuelCost > 0) {
            ((FreidoraInventory) container).extractFuel(fuelCost);
        }
    }

    private static int fuelCost(CookingRecipe recipe) {
        return recipe.getCookingResult().fuelCost();
    }

    /**
     * Finalizar receta (the ingredient half): removes one unit of whatever was in the input slot.
     * The other half - placing the output - is shared logic; see
     * {@link AbstractCookingManager#applyOutput}.
     */
    @Override
    protected void consumeInputs(CookingContainer container, CookingRecipe recipe) {
        ((FreidoraInventory) container).extractInput(1);
    }
}
