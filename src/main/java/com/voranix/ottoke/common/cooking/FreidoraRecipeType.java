package com.voranix.ottoke.common.cooking;

import com.voranix.ottoke.OttokeStreetFood;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public class FreidoraRecipeType implements RecipeType<FreidoraRecipe> {
    public static final ResourceLocation ID = new ResourceLocation(OttokeStreetFood.MODID, "freidora");
    public static final FreidoraRecipeType INSTANCE = new FreidoraRecipeType();
}
