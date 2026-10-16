package com.voranix.maipizza.common.cooking;

import com.google.gson.JsonObject;
import com.voranix.maipizza.common.config.MaiPizzaConfig;
import com.voranix.voranixmod.common.cooking.system.CookingResult;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

/**
 * Reads/writes the {@code voranixmod:horno_de_barro} recipe format - a direct port of
 * {@code FreidoraRecipeSerializer}'s {@code ingredient}/{@code result}/{@code cookTime}/
 * {@code fuelCost}/{@code experience}/{@code qualityWindow}/{@code burnWindow} shape.
 */
public class HornoDeBarroRecipeSerializer implements RecipeSerializer<HornoDeBarroRecipe> {
    @Override
    public HornoDeBarroRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
        ItemStack resultStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        int cookTime = GsonHelper.getAsInt(json, "cookTime", MaiPizzaConfig.HORNO_DE_BARRO_COOKING_TIME.get());
        int fuelCost = GsonHelper.getAsInt(json, "fuelCost", 0);
        float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
        int qualityWindow = GsonHelper.getAsInt(json, "qualityWindow", 0);
        int burnWindow = GsonHelper.getAsInt(json, "burnWindow", Integer.MAX_VALUE);

        CookingResult result = CookingResult.of(resultStack, experience, fuelCost);
        return new HornoDeBarroRecipe(recipeId, ingredient, result, cookTime, qualityWindow, burnWindow);
    }

    @Override
    public HornoDeBarroRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack resultStack = buffer.readItem();
        int cookTime = buffer.readVarInt();
        int fuelCost = buffer.readVarInt();
        float experience = buffer.readFloat();
        int qualityWindow = buffer.readVarInt();
        int burnWindow = buffer.readVarInt();

        CookingResult result = CookingResult.of(resultStack, experience, fuelCost);
        return new HornoDeBarroRecipe(recipeId, ingredient, result, cookTime, qualityWindow, burnWindow);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, HornoDeBarroRecipe recipe) {
        recipe.getIngredient().toNetwork(buffer);
        buffer.writeItem(recipe.getCookingResult().result());
        buffer.writeVarInt(recipe.getCookingTime());
        buffer.writeVarInt(recipe.getCookingResult().fuelCost());
        buffer.writeFloat(recipe.getCookingResult().experience());
        buffer.writeVarInt(recipe.getQualityWindow());
        buffer.writeVarInt(recipe.getBurnWindow());
    }
}
