package com.voranix.voranixmod.common.cooking.system;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Base class for a station's recipe: fixes the ceremony every {@link CookingRecipe} needs in order
 * to also satisfy vanilla's {@code Recipe<Container>} (so it keeps loading from JSON through
 * Minecraft's RecipeManager), without repeating that ceremony per concrete recipe.
 * <p>
 * Only the truly mechanical parts are implemented: id, result, cooking time, and the fact that
 * vanilla's {@code assemble()}/{@code getResultItem()} - which must return a plain
 * {@code ItemStack}, not a {@link CookingResult} - are always just a copy of the item inside the
 * stored result. What actually varies per recipe - does a given input match, and which
 * serializer/type identifies it - stays abstract.
 * <p>
 * {@code matches(Container, Level)} is deliberately left abstract rather than given a default
 * delegation to {@link CookingRecipe#matches(CookingContainer)}: how a station's vanilla
 * {@code Container} maps to its {@link CookingContainer} (slot count, slot order) is specific to
 * that station, not something safe to assume here.
 */
public abstract class AbstractCookingRecipe implements Recipe<Container>, CookingRecipe {
    protected final ResourceLocation id;
    protected final CookingResult result;
    protected final int cookingTime;

    protected AbstractCookingRecipe(ResourceLocation id, CookingResult result, int cookingTime) {
        this.id = id;
        this.result = result;
        this.cookingTime = cookingTime;
    }

    @Override
    public ResourceLocation getRecipeId() {
        return this.id;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public CookingResult getCookingResult() {
        return this.result;
    }

    @Override
    public int getCookingTime() {
        return this.cookingTime;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return this.result.result().copy();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result.result().copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }
}
