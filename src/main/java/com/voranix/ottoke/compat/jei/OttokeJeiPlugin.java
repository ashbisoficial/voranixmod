package com.voranix.ottoke.compat.jei;

import com.voranix.voranixmod.VoranixMod;
import com.voranix.ottoke.common.cooking.FreidoraRecipe;
import com.voranix.ottoke.common.registration.ModBlocks;
import com.voranix.ottoke.common.registration.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Discovered by JEI through {@link JeiPlugin} annotation scanning - not referenced anywhere else in
 * the mod, so this class (and everything in {@code compat.jei}) only ever needs {@code mezz.jei.*}
 * to resolve when JEI's own plugin loader finds and loads it, which only happens when JEI is
 * actually installed.
 */
@JeiPlugin
public final class OttokeJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(VoranixMod.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FreidoraJeiCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        RecipeManager recipeManager = level.getRecipeManager();
        List<FreidoraRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.FREIDORA.get());
        registration.addRecipes(FreidoraJeiCategory.TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(FreidoraJeiCategory.TYPE, ModBlocks.FREIDORA.get());
    }
}
