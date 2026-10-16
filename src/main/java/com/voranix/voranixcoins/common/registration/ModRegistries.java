package com.voranix.voranixcoins.common.registration;

import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Single entry point {@link com.voranix.voranixcoins.VoranixCoinsModule#init} calls during module
 * init - mirrors {@code com.voranix.ottoke.common.registration.ModRegistries}. Kept as an array even
 * with two entries today because the next registry category (a wallet/bank block, a currency-pouch
 * item, ...) should mean one more line here, not a new call site in {@code VoranixCoinsModule}.
 */
public final class ModRegistries {
    private static final Registrar[] REGISTRARS = {
            ModItems::register,
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
