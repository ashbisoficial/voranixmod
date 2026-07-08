package com.voranix.ottoke.compat.emi;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.cooking.FreidoraRecipe;
import com.voranix.ottoke.common.registration.ModBlocks;
import com.voranix.ottoke.common.registration.ModRecipeTypes;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

/**
 * Discovered by EMI through the {@code java.util.ServiceLoader} entry at
 * {@code META-INF/services/dev.emi.emi.api.EmiPlugin} - not referenced anywhere else in the mod, so
 * (mirroring the JEI and Jade plugins) it only ever loads, and only ever needs {@code dev.emi.emi.*}
 * to resolve, when EMI itself asks {@code ServiceLoader} for implementations, which only happens
 * when EMI is present. If EMI is absent, nothing ever calls that lookup and this class is never
 * touched.
 */
public final class OttokeEmiPlugin implements EmiPlugin {
    static final EmiRecipeCategory FREIDORA_CATEGORY = new EmiRecipeCategory(
            new ResourceLocation(OttokeStreetFood.MODID, "freidora"),
            EmiStack.of(ModBlocks.FREIDORA.get()));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(FREIDORA_CATEGORY);
        registry.addWorkstation(FREIDORA_CATEGORY, EmiStack.of(ModBlocks.FREIDORA.get()));

        RecipeManager recipeManager = registry.getRecipeManager();
        List<FreidoraRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.FREIDORA.get());
        for (FreidoraRecipe recipe : recipes) {
            registry.addRecipe(new FreidoraEmiRecipe(FREIDORA_CATEGORY, recipe));
        }
    }
}
