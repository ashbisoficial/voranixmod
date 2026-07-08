package com.voranix.ottoke.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Consumer;

/**
 * A plain vanilla shapeless recipe whose ingredients can name another mod's item by raw id string,
 * without that mod's {@code Item} object - or even its jar - existing at datagen time. Built for the
 * Farmer's Delight/Create soft-compat recipes in {@link ModRecipeProvider}: those mods are never a
 * dependency of this project (see {@code ModCompat}), so there is no {@code Item} instance to hand
 * to vanilla's own {@code ShapelessRecipeBuilder}. A recipe ingredient is always just
 * {@code {"item": "<id>"}} in JSON; nothing requires that id to resolve to a real registered item
 * until whatever installation actually loads the file - which, wrapped in a
 * {@link net.minecraftforge.common.crafting.conditions.ModLoadedCondition}, only ever happens on an
 * installation where the other mod (and therefore that item) genuinely exists.
 * <p>
 * Always saved through {@link #saveConditional}, never directly - an unconditional recipe naming an
 * item that may not exist would log a resolution warning (or worse) on any install missing that mod.
 */
final class ExternalItemShapelessRecipe implements FinishedRecipe {
    private final ResourceLocation id;
    private final List<String> ingredientIds;
    private final ItemStack result;

    private ExternalItemShapelessRecipe(ResourceLocation id, List<String> ingredientIds, ItemStack result) {
        this.id = id;
        this.ingredientIds = ingredientIds;
        this.result = result;
    }

    static void saveConditional(Consumer<FinishedRecipe> writer, ResourceLocation id,
                                 List<String> ingredientIds, ItemStack result, ICondition condition) {
        ConditionalRecipe.builder()
                .addCondition(condition)
                .addRecipe(new ExternalItemShapelessRecipe(id, ingredientIds, result))
                .build(writer, id);
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        JsonArray ingredients = new JsonArray();
        for (String itemId : this.ingredientIds) {
            JsonObject ingredient = new JsonObject();
            ingredient.addProperty("item", itemId);
            ingredients.add(ingredient);
        }
        json.add("ingredients", ingredients);

        JsonObject resultJson = new JsonObject();
        resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result.getItem()).toString());
        if (this.result.getCount() > 1) {
            resultJson.addProperty("count", this.result.getCount());
        }
        json.add("result", resultJson);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return RecipeSerializer.SHAPELESS_RECIPE;
    }

    @Override
    public JsonObject serializeAdvancement() {
        return null;
    }

    @Override
    public ResourceLocation getAdvancementId() {
        return null;
    }
}
