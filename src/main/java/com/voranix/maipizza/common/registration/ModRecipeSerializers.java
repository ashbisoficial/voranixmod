package com.voranix.maipizza.common.registration;

import com.voranix.maipizza.common.cooking.HornoDeBarroRecipe;
import com.voranix.maipizza.common.cooking.HornoDeBarroRecipeSerializer;
import com.voranix.voranixmod.VoranixMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** Mirrors {@code com.voranix.ottoke.common.registration.ModRecipeSerializers}. */
public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, VoranixMod.MODID);

    public static final RegistryObject<RecipeSerializer<HornoDeBarroRecipe>> HORNO_DE_BARRO =
            RECIPE_SERIALIZERS.register("horno_de_barro", HornoDeBarroRecipeSerializer::new);

    private ModRecipeSerializers() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
