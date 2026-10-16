package com.voranix.voranixmod.common.cooking.system;

import net.minecraft.resources.ResourceLocation;

/**
 * The cooking-domain contract a recipe must satisfy: given a station's {@link CookingContainer},
 * does it match, what does it produce, and how long does it take. Deliberately not vanilla's
 * {@code Recipe<Container>} - that interface also carries RecipeType/RecipeSerializer/recipe-book
 * plumbing that {@link AbstractCookingManager} has no business depending on to make a cook/don't-cook
 * decision.
 * <p>
 * A concrete recipe (FreidoraRecipe) is expected to implement both this and vanilla's
 * {@code Recipe<Container>} at once: {@code Recipe<Container>} is what lets it load from JSON
 * through Minecraft's RecipeManager, this is what {@link AbstractCookingManager} and
 * {@link CookingStation} actually depend on. That split means the cooking system never has to
 * assume recipes come from JSON at all - a future station could source its {@link CookingRecipe}s
 * from anywhere and still reuse this exact manager/process/station contract.
 */
public interface CookingRecipe {
    ResourceLocation getRecipeId();

    boolean matches(CookingContainer container);

    CookingResult getCookingResult();

    int getCookingTime();
}
