package com.voranix.ottoke.common.cooking;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class FreidoraRecipeSerializer implements RecipeSerializer<FreidoraRecipe> {
    @Override
    public FreidoraRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.getAsJsonObject("input"));
        ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        int cookingTime = GsonHelper.getAsInt(json, "cookingTime", 200);
        return new FreidoraRecipe(recipeId, input, output, cookingTime);
    }

    @Override
    public FreidoraRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        ItemStack output = buffer.readItem();
        int cookingTime = buffer.readVarInt();
        return new FreidoraRecipe(recipeId, input, output, cookingTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, FreidoraRecipe recipe) {
        recipe.getIngredients().get(0).toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem(null));
        buffer.writeVarInt(recipe.getCookingTime());
    }
}
