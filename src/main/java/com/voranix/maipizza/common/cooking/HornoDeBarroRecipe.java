package com.voranix.maipizza.common.cooking;

import com.voranix.maipizza.common.registration.ModRecipeSerializers;
import com.voranix.maipizza.common.registration.ModRecipeTypes;
import com.voranix.voranixmod.common.cooking.system.AbstractCookingRecipe;
import com.voranix.voranixmod.common.cooking.system.CookingContainer;
import com.voranix.voranixmod.common.cooking.system.CookingResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * Horno de Barro's concrete recipe - a straight port of {@code FreidoraRecipe}'s shape (one
 * ingredient, one result, plus a quality/burn window for a future evaluator).
 */
public class HornoDeBarroRecipe extends AbstractCookingRecipe {
    private final Ingredient ingredient;
    private final int qualityWindow;
    private final int burnWindow;

    public HornoDeBarroRecipe(ResourceLocation id, Ingredient ingredient, CookingResult result, int cookTime,
                               int qualityWindow, int burnWindow) {
        super(id, result, cookTime);
        this.ingredient = ingredient;
        this.qualityWindow = qualityWindow;
        this.burnWindow = burnWindow;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public int getQualityWindow() {
        return this.qualityWindow;
    }

    public int getBurnWindow() {
        return this.burnWindow;
    }

    @Override
    public boolean matches(CookingContainer container) {
        return this.ingredient.test(container.getInputItem(0));
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.HORNO_DE_BARRO.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.HORNO_DE_BARRO.get();
    }
}
