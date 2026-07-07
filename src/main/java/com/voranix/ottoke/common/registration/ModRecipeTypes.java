package com.voranix.ottoke.common.registration;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.cooking.FreidoraRecipe;
import com.voranix.ottoke.common.cooking.FreidoraRecipeType;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers RecipeType entries (the "what kind of recipe is this" marker used by RecipeManager
 * lookups). Split from RecipeSerializer registration because a mod can define more recipe types
 * that reuse an existing serializer, or more serializers for one type (e.g. a legacy JSON format).
 */
public final class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, OttokeStreetFood.MODID);

    public static final RegistryObject<RecipeType<FreidoraRecipe>> FREIDORA =
            RECIPE_TYPES.register("freidora", () -> FreidoraRecipeType.INSTANCE);

    private ModRecipeTypes() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}
