package com.voranix.maipizza.common.registration;

import net.minecraftforge.eventbus.api.IEventBus;

/** Mirrors {@code com.voranix.ottoke.common.registration.ModRegistries}. */
public final class ModRegistries {
    private static final Registrar[] REGISTRARS = {
            ModBlocks::register,
            ModBlockItems::register,
            ModItems::register,
            ModBlockEntities::register,
            ModMenuTypes::register,
            ModRecipeTypes::register,
            ModRecipeSerializers::register,
            ModCreativeTabs::register
    };

    private ModRegistries() {
    }

    public static void registerAll(IEventBus modEventBus) {
        for (Registrar registrar : REGISTRARS) {
            registrar.register(modEventBus);
        }
    }

    @FunctionalInterface
    private interface Registrar {
        void register(IEventBus eventBus);
    }
}
