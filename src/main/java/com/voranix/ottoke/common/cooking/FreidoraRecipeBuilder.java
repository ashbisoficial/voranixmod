package com.voranix.ottoke.common.cooking;

import com.google.gson.JsonObject;
import com.voranix.ottoke.common.registration.ModRecipeSerializers;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

/**
 * Datagen builder for {@link FreidoraRecipe}, matching the {@code ingredient}/{@code result}/
 * {@code cookTime}/{@code fuelCost}/{@code experience}/{@code qualityWindow}/{@code burnWindow}
 * format read by {@link FreidoraRecipeSerializer}. Only ingredient/result/cookTime are required;
 * the rest are fluent setters so a recipe that doesn't need them doesn't have to mention them -
 * generated JSON stays as minimal as a hand-written one.
 * <p>
 * serializeAdvancement()/getAdvancementId() return null on purpose: the "voranixmod:freidora" recipe
 * type has no registered RecipeBookCategory, so a recipe-book unlock advancement would exist only
 * to unlock an entry that never renders anywhere.
 */
public final class FreidoraRecipeBuilder {
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int cookTime;
    private int fuelCost = 0;
    private float experience = 0.0F;
    private int qualityWindow = 0;
    private int burnWindow = Integer.MAX_VALUE;

    private FreidoraRecipeBuilder(Ingredient ingredient, ItemStack result, int cookTime) {
        this.ingredient = ingredient;
        this.result = result;
        this.cookTime = cookTime;
    }

    public static FreidoraRecipeBuilder frying(Ingredient ingredient, ItemStack result, int cookTime) {
        return new FreidoraRecipeBuilder(ingredient, result, cookTime);
    }

    public FreidoraRecipeBuilder fuelCost(int fuelCost) {
        this.fuelCost = fuelCost;
        return this;
    }

    public FreidoraRecipeBuilder experience(float experience) {
        this.experience = experience;
        return this;
    }

    public FreidoraRecipeBuilder qualityWindow(int qualityWindow) {
        this.qualityWindow = qualityWindow;
        return this;
    }

    public FreidoraRecipeBuilder burnWindow(int burnWindow) {
        this.burnWindow = burnWindow;
        return this;
    }

    public void save(Consumer<FinishedRecipe> writer, ResourceLocation id) {
        writer.accept(new Result(id, this.ingredient, this.result, this.cookTime, this.fuelCost,
                this.experience, this.qualityWindow, this.burnWindow));
    }

    private record Result(ResourceLocation id, Ingredient ingredient, ItemStack result, int cookTime,
                           int fuelCost, float experience, int qualityWindow,
                           int burnWindow) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", this.ingredient.toJson());

            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result.getItem()).toString());
            if (this.result.getCount() > 1) {
                resultJson.addProperty("count", this.result.getCount());
            }
            json.add("result", resultJson);

            json.addProperty("cookTime", this.cookTime);
            if (this.fuelCost != 0) {
                json.addProperty("fuelCost", this.fuelCost);
            }
            if (this.experience != 0.0F) {
                json.addProperty("experience", this.experience);
            }
            if (this.qualityWindow != 0) {
                json.addProperty("qualityWindow", this.qualityWindow);
            }
            if (this.burnWindow != Integer.MAX_VALUE) {
                json.addProperty("burnWindow", this.burnWindow);
            }
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipeSerializers.FREIDORA.get();
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
}
