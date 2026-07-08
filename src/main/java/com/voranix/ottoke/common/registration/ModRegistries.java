package com.voranix.ottoke.common.registration;

import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Single entry point the main mod class calls during construction. Adding a tenth registry
 * category in a future version means adding one line to {@link #REGISTRARS} here -
 * {@link com.voranix.ottoke.OttokeStreetFood} never has to change.
 * <p>
 * The array order documents each category's logical dependency on the previous ones
 * (BlockItems reference Blocks, RecipeTypes/Serializers are looked up by cooking logic, etc.).
 * It does not control the actual Forge RegisterEvent firing order per registry - that is fixed
 * by Forge itself - so this ordering is for readability, not correctness.
 */
public final class ModRegistries {
    private static final Registrar[] REGISTRARS = {
            ModBlocks::register,
            ModBlockItems::register,
            ModIngredients::register,
            ModItems::register,
            ModBlockEntities::register,
            ModMenuTypes::register,
            ModRecipeTypes::register,
            ModRecipeSerializers::register,
            ModSounds::register,
            ModCreativeTabs::register
    };

    private ModRegistries() {
    }

    public static void registerAll(IEventBus modEventBus) {
        for (Registrar registrar : REGISTRARS) {
            registrar.register(modEventBus);
        }
    }
}
