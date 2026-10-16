package com.voranix.voranixcoins;

import com.voranix.voranixcoins.common.registration.ModRegistries;
import com.voranix.voranixcoins.datagen.ModDataGenerators;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Voranix Coins' own init entry point, mirroring {@code com.voranix.ottoke.OttokeModule} - see that
 * class and {@code com.voranix.voranixmod.VoranixMod} for the shared {@code @Mod} entry point and
 * modid this module is initialized onto.
 * <p>
 * Rescued from Cameloth's original standalone {@code Voranix Coins} mod: only the compiled jar
 * survived (no source), so the six coin items, their rarities, and the creative tab were rebuilt
 * from its bytecode/assets rather than ported from source. Nothing beyond that - the original mod
 * never had a conversion rate between denominations, a wallet, or any other economy logic, so none
 * is invented here either.
 */
public final class VoranixCoinsModule {
    private VoranixCoinsModule() {
    }

    public static void init(IEventBus modEventBus) {
        ModRegistries.registerAll(modEventBus);
        ModDataGenerators.register(modEventBus);
    }
}
