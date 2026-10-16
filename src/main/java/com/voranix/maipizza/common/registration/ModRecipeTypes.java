package com.voranix.maipizza.common.registration;

import com.voranix.maipizza.common.cooking.HornoDeBarroRecipe;
import com.voranix.maipizza.common.cooking.HornoDeBarroRecipeType;
import com.voranix.voranixmod.VoranixMod;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** Mirrors {@code com.voranix.ottoke.common.registration.ModRecipeTypes}. */
public final class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, VoranixMod.MODID);

    public static final RegistryObject<RecipeType<HornoDeBarroRecipe>> HORNO_DE_BARRO =
            RECIPE_TYPES.register("horno_de_barro", () -> HornoDeBarroRecipeType.INSTANCE);

    private ModRecipeTypes() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}
