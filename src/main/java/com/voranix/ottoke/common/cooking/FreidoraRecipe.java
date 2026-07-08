package com.voranix.ottoke.common.cooking;

import com.voranix.ottoke.common.cooking.system.AbstractCookingRecipe;
import com.voranix.ottoke.common.cooking.system.CookingContainer;
import com.voranix.ottoke.common.cooking.system.CookingResult;
import com.voranix.ottoke.common.registration.ModRecipeSerializers;
import com.voranix.ottoke.common.registration.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * The Freidora's concrete recipe: one ingredient, one result, plus the timing data
 * {@code qualityWindow}/{@code burnWindow} that a future {@code CookingQualityEvaluator} will read.
 * <p>
 * {@code qualityWindow} and {@code burnWindow} live here, not on {@link AbstractCookingRecipe},
 * because the Freidora is still the only concrete recipe in the system - promoting them to the
 * shared base class before a second recipe type exists to prove they belong there would be
 * guessing at a shape instead of extracting a real duplication.
 */
public class FreidoraRecipe extends AbstractCookingRecipe {
    private final Ingredient ingredient;
    private final int qualityWindow;
    private final int burnWindow;

    public FreidoraRecipe(ResourceLocation id, Ingredient ingredient, CookingResult result, int cookTime,
                           int qualityWindow, int burnWindow) {
        super(id, result, cookTime);
        this.ingredient = ingredient;
        this.qualityWindow = qualityWindow;
        this.burnWindow = burnWindow;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    /** Ticks past {@code cookTime}, once evaluated, within which the cook grades as PERFECT. */
    public int getQualityWindow() {
        return this.qualityWindow;
    }

    /** Ticks past {@code cookTime} after which the cook grades as BURNT. */
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
        return ModRecipeSerializers.FREIDORA.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.FREIDORA.get();
    }
}
