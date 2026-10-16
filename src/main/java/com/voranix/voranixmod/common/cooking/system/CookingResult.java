package com.voranix.voranixmod.common.cooking.system;

import net.minecraft.world.item.ItemStack;

/**
 * Everything a finished cook produces - not just the item. Replaces the bare {@code ItemStack} that
 * {@link CookingRecipe#getCookingResult()} and {@link AbstractCookingManager#applyOutput} used to
 * pass around.
 * <p>
 * Why bundling this now matters: every mechanic a cooking station gains next - granting XP,
 * consuming fuel, varying quality - describes something about the <i>outcome</i> of a cook, not a
 * new step in {@link AbstractCookingManager#tick}'s state machine. Because
 * {@link AbstractCookingManager#applyOutput} already receives the whole {@code CookingResult}
 * (not just its {@code ItemStack}), turning on XP granting or fuel consumption later means changing
 * what {@code applyOutput} <i>does</i> with fields it already has - not adding a new parameter to
 * {@code tick()}, {@code applyOutput}, {@link CookingRecipe}, or any concrete recipe/manager that
 * already exists by then.
 * <p>
 * {@code experience} and {@code fuelCost} are real fields today but nothing reads them yet: there
 * is no XP-granting code (it would need a {@code Player}, which {@link CookingContext} deliberately
 * doesn't carry yet) and no fuel system. A recipe can already set both without anything happening,
 * so each mechanic can be turned on independently, later, without another pass through this
 * object's shape. {@code fuelCost} is a quantity, not a flag, so a station can eventually charge
 * different recipes different amounts of fuel; {@code 0} means "does not consume fuel".
 * <p>
 * {@code quality} here is only ever a placeholder, never the final word: a recipe is not the
 * authority on how well a cook turns out (see {@link CookingQualityEvaluator}), so whatever value
 * {@link #simple}/{@link #of} sets is expected to be overridden by an evaluator before this result
 * is applied, once that wiring exists.
 */
public record CookingResult(ItemStack result, float experience, CookingQuality quality, int fuelCost) {

    /** The common case today: just an item, no experience, placeholder NORMAL quality, no fuel cost. */
    public static CookingResult simple(ItemStack result) {
        return new CookingResult(result, 0.0F, CookingQuality.NORMAL, 0);
    }

    /** Like {@link #simple}, but for a recipe that also defines experience and/or a fuel cost. */
    public static CookingResult of(ItemStack result, float experience, int fuelCost) {
        return new CookingResult(result, experience, CookingQuality.NORMAL, fuelCost);
    }
}
