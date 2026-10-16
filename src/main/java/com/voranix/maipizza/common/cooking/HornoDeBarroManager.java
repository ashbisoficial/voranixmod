package com.voranix.maipizza.common.cooking;

import com.voranix.maipizza.common.registration.ModRecipeTypes;
import com.voranix.voranixmod.common.cooking.system.AbstractCookingManager;
import com.voranix.voranixmod.common.cooking.system.CookingContainer;
import com.voranix.voranixmod.common.cooking.system.CookingRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;
import java.util.Optional;

/**
 * Horno de Barro's {@link AbstractCookingManager} - a direct port of {@code FreidoraManager}'s shape
 * (recipe lookup cached by {@code RecipeManager} identity, fuel validated/consumed against
 * {@link HornoDeBarroInventory}'s fuel slot). See that class's javadoc for why the cook/don't-cook
 * state machine itself is inherited rather than repeated here.
 */
public class HornoDeBarroManager extends AbstractCookingManager {
    public static final HornoDeBarroManager INSTANCE = new HornoDeBarroManager();

    private RecipeManager cachedRecipeManager;
    private List<HornoDeBarroRecipe> cachedRecipes = List.of();

    @Override
    protected Optional<CookingRecipe> findMatchingRecipe(CookingContainer container, Level level) {
        List<HornoDeBarroRecipe> recipes = getRecipes(level);
        for (int i = 0; i < recipes.size(); i++) {
            HornoDeBarroRecipe recipe = recipes.get(i);
            if (recipe.matches(container)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    private List<HornoDeBarroRecipe> getRecipes(Level level) {
        RecipeManager recipeManager = level.getRecipeManager();
        if (recipeManager != this.cachedRecipeManager) {
            this.cachedRecipeManager = recipeManager;
            this.cachedRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.HORNO_DE_BARRO.get());
        }
        return this.cachedRecipes;
    }

    @Override
    protected boolean hasFuel(CookingContainer container, CookingRecipe recipe) {
        int fuelCost = fuelCost(recipe);
        if (fuelCost <= 0) {
            return true;
        }

        ItemStack fuel = ((HornoDeBarroInventory) container).getFuelItem();
        return ForgeHooks.getBurnTime(fuel, null) > 0 && fuel.getCount() >= fuelCost;
    }

    @Override
    protected void consumeFuel(CookingContainer container, CookingRecipe recipe) {
        int fuelCost = fuelCost(recipe);
        if (fuelCost > 0) {
            ((HornoDeBarroInventory) container).extractFuel(fuelCost);
        }
    }

    private static int fuelCost(CookingRecipe recipe) {
        return recipe.getCookingResult().fuelCost();
    }

    @Override
    protected void consumeInputs(CookingContainer container, CookingRecipe recipe) {
        ((HornoDeBarroInventory) container).extractInput(1);
    }
}
