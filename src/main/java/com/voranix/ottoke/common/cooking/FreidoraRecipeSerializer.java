package com.voranix.ottoke.common.cooking;

import com.google.gson.JsonObject;
import com.voranix.ottoke.common.config.OttokeConfig;
import com.voranix.voranixmod.common.cooking.system.CookingResult;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

/**
 * Reads/writes the {@code voranixmod:freidora} recipe format:
 * {@code ingredient, result, cookTime, fuelCost, experience, qualityWindow, burnWindow}.
 * Only {@code ingredient}, {@code result}, and {@code cookTime} are required - the rest default to
 * "this mechanic doesn't apply to this recipe" (0 fuel, 0 experience, no quality tolerance, never
 * burns), so a datapack author can write a minimal recipe today and adopt the newer fields only
 * when they matter.
 * <p>
 * {@code ingredient} is parsed with {@link Ingredient#fromJson(com.google.gson.JsonElement)} rather
 * than forcing a JSON object, so both {@code {"item": "..."}} and {@code {"tag": "forge:sausages"}}
 * work with no serializer changes - tag support was a design goal from the start, not something
 * bolted on here.
 */
public class FreidoraRecipeSerializer implements RecipeSerializer<FreidoraRecipe> {
    @Override
    public FreidoraRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
        ItemStack resultStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        int cookTime = GsonHelper.getAsInt(json, "cookTime", OttokeConfig.FREIDORA_COOKING_TIME.get());
        int fuelCost = GsonHelper.getAsInt(json, "fuelCost", 0);
        float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
        int qualityWindow = GsonHelper.getAsInt(json, "qualityWindow", 0);
        int burnWindow = GsonHelper.getAsInt(json, "burnWindow", Integer.MAX_VALUE);

        CookingResult result = CookingResult.of(resultStack, experience, fuelCost);
        return new FreidoraRecipe(recipeId, ingredient, result, cookTime, qualityWindow, burnWindow);
    }

    @Override
    public FreidoraRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack resultStack = buffer.readItem();
        int cookTime = buffer.readVarInt();
        int fuelCost = buffer.readVarInt();
        float experience = buffer.readFloat();
        int qualityWindow = buffer.readVarInt();
        int burnWindow = buffer.readVarInt();

        CookingResult result = CookingResult.of(resultStack, experience, fuelCost);
        return new FreidoraRecipe(recipeId, ingredient, result, cookTime, qualityWindow, burnWindow);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, FreidoraRecipe recipe) {
        recipe.getIngredient().toNetwork(buffer);
        buffer.writeItem(recipe.getCookingResult().result());
        buffer.writeVarInt(recipe.getCookingTime());
        buffer.writeVarInt(recipe.getCookingResult().fuelCost());
        buffer.writeFloat(recipe.getCookingResult().experience());
        buffer.writeVarInt(recipe.getQualityWindow());
        buffer.writeVarInt(recipe.getBurnWindow());
    }
}
