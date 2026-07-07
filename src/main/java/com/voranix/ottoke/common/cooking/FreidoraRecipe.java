package com.voranix.ottoke.common.cooking;

import com.voranix.ottoke.common.registration.ModRecipeSerializers;
import com.voranix.ottoke.common.registration.ModRecipeTypes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class FreidoraRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack output;
    private final int cookingTime;

    public FreidoraRecipe(ResourceLocation id, Ingredient input, ItemStack output, int cookingTime) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.input.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.FREIDORA.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.FREIDORA.get();
    }

    public int getCookingTime() {
        return this.cookingTime;
    }
}
