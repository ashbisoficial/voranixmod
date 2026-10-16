package com.voranix.voranixmod.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Datagen for the concerns that belong to the {@code VoranixMod} umbrella rather than any one
 * module - the two {@code LanguageProvider}s, plus {@code RecipeProvider} and
 * {@code LootTableProvider} (see {@link ModLanguageProvider}'s javadoc for why: all four have a
 * fixed name with no per-modid variation, so only one instance of each can exist per
 * {@code GatherDataEvent}). Each module's own {@code ModDataGenerators} still registers everything
 * else (blockstates, item models, tags, advancements) independently.
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
        boolean client = event.includeClient();
        boolean server = event.includeServer();

        generator.addProvider(client, new ModLanguageProvider(output));
        generator.addProvider(client, new ModLanguageProviderEs(output));
        generator.addProvider(server, new ModRecipeProvider(output));
        generator.addProvider(server, new ModLootTableProvider(output));
    }
}
