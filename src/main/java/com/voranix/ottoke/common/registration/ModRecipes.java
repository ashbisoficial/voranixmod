package com.voranix.ottoke.common.registration;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.cooking.FreidoraRecipe;
import com.voranix.ottoke.common.cooking.FreidoraRecipeSerializer;
import com.voranix.ottoke.common.cooking.FreidoraRecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, OttokeStreetFood.MODID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, OttokeStreetFood.MODID);

    public static final RegistryObject<RecipeSerializer<FreidoraRecipe>> FREIDORA_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("freidora", FreidoraRecipeSerializer::new);

    public static final RegistryObject<RecipeType<FreidoraRecipe>> FREIDORA_RECIPE_TYPE =
            RECIPE_TYPES.register("freidora", () -> FreidoraRecipeType.INSTANCE);

    private ModRecipes() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
