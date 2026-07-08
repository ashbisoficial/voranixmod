package com.voranix.ottoke.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;

/**
 * Single entry point for datagen, mirroring ModRegistries: the main mod class only needs to know
 * "data generation exists", not which providers back which asset category. Adding a provider in a
 * future version means one line in {@link #gatherData}, not a change to OttokeStreetFood.
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
        boolean server = event.includeServer();

        generator.addProvider(client, new ModBlockStateProvider(output, existingFileHelper));
        generator.addProvider(client, new ModItemModelProvider(output, existingFileHelper));
        generator.addProvider(client, new ModLanguageProvider(output));
        generator.addProvider(server, new ModRecipeProvider(output));
        generator.addProvider(server, new ModLootTableProvider(output));
        generator.addProvider(server, new ModItemTagsProvider(output, event.getLookupProvider(), existingFileHelper));
        generator.addProvider(server, new ForgeAdvancementProvider(
                output, event.getLookupProvider(), existingFileHelper, List.of(new ModAdvancementProvider())));
    }
}
