package com.voranix.maipizza.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Single entry point for Mai Pizza's own datagen, mirroring
 * {@code com.voranix.ottoke.datagen.ModDataGenerators}. No {@code LanguageProvider}, recipe, or loot
 * table provider registered here - all three have a fixed name with no per-modid variation, so only
 * one instance of each can exist per {@code GatherDataEvent}; see
 * {@code com.voranix.voranixmod.datagen.ModLanguageProvider}/{@code ModRecipeProvider}/
 * {@code ModLootTableProvider} for where this module's recipes and {@link ModBlockLootTables}
 * actually get registered from instead. No tags/advancements provider yet either - nothing in this
 * module contributes to a shared tag or has an advancement tree defined.
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

        generator.addProvider(client, new ModBlockStateProvider(output, existingFileHelper));
        generator.addProvider(client, new ModItemModelProvider(output, existingFileHelper));
    }
}
