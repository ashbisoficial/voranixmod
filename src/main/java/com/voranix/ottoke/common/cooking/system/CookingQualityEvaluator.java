package com.voranix.ottoke.common.cooking.system;

/**
 * Decides the {@link CookingQuality} of a finished cook. This is the one and only place quality is
 * meant to be decided - not {@link CookingRecipe}, not {@link CookingResult} as authored by a
 * recipe. A recipe describes what a cook produces when it succeeds (item, base experience, whether
 * it uses fuel); it deliberately has no say in how well that particular cook turned out. That
 * judgment depends on how the cook actually went - timing, container state, whatever a concrete
 * evaluator decides matters - which a static recipe definition cannot know in advance.
 * <p>
 * Not wired into {@link AbstractCookingManager} yet, and no implementation exists. When it is: a
 * concrete manager (e.g. the future {@code FreidoraManager}) will supply one, and the manager's
 * finish step will call it to produce the {@link CookingQuality} that overrides whatever placeholder
 * quality is sitting in the recipe's {@link CookingResult} (see {@link CookingResult#simple}) before
 * the result is actually applied - the same way {@link #findMatchingRecipe} and
 * {@link #consumeInputs} are already station-supplied hooks today.
 *
 * @see AbstractCookingManager#findMatchingRecipe(CookingContainer, net.minecraft.world.level.Level) findMatchingRecipe
 * @see AbstractCookingManager#consumeInputs(CookingContainer, CookingRecipe) consumeInputs
 */
public interface CookingQualityEvaluator {
    CookingQuality evaluate(CookingContainer container, CookingProcess process, CookingRecipe recipe);
}
