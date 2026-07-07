package com.voranix.ottoke.common.registration;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.cooking.FreidoraRecipe;
import com.voranix.ottoke.common.cooking.FreidoraRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers RecipeSerializer entries (JSON <-> Recipe <-> network (de)serialization logic).
 * See {@link ModRecipeTypes} for why this is a distinct class from recipe type registration.
 */
public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, OttokeStreetFood.MODID);

    public static final RegistryObject<RecipeSerializer<FreidoraRecipe>> FREIDORA =
            RECIPE_SERIALIZERS.register("freidora", FreidoraRecipeSerializer::new);

    private ModRecipeSerializers() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
