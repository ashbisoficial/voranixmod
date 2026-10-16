package com.voranix.ottoke.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;

/**
 * Single entry point for datagen, mirroring ModRegistries: {@link com.voranix.ottoke.OttokeModule}
 * only needs to know "data generation exists", not which providers back which asset category.
 * Adding a provider in a future version means one line in {@link #gatherData}, not a change to
 * {@code OttokeModule}.
 * <p>
 * No {@code LanguageProvider}, recipe, or loot table provider here - {@code RecipeProvider} and
 * {@code LootTableProvider} both have a fixed, {@code final} {@code getName()} ({@code "Recipes"},
 * {@code "Loot Tables"}) with no per-modid variation, so - like lang - only one instance of each can
 * exist per {@code GatherDataEvent}; see {@code com.voranix.voranixmod.datagen.ModRecipeProvider}/
 * {@code ModLootTableProvider} for where Ottoke's recipes and {@link ModBlockLootTables} actually
 * get registered from instead.
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
        generator.addProvider(server, new ModItemTagsProvider(output, event.getLookupProvider(), existingFileHelper));
        generator.addProvider(server, new ForgeAdvancementProvider(
                output, event.getLookupProvider(), existingFileHelper, List.of(new ModAdvancementProvider())));
    }
}
