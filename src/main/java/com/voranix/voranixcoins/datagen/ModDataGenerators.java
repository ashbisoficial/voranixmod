package com.voranix.voranixcoins.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Single entry point for datagen, mirroring {@code com.voranix.ottoke.datagen.ModDataGenerators}.
 * No {@code LanguageProvider} here - see {@code com.voranix.voranixmod.datagen.ModLanguageProvider}
 * for why lang generation for every module lives at the {@code VoranixMod} umbrella level instead.
 */
public final class ModDataGenerators {
    private ModDataGenerators() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ModDataGenerators::gatherData);
    }

    private static void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        boolean client = event.includeClient();

        generator.addProvider(client, new ModItemModelProvider(output, existingFileHelper));
    }
}
