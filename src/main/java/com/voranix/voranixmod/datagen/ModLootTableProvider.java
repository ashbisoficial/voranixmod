package com.voranix.voranixmod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

/**
 * Combines every module's block-loot sub-provider into the one {@code LootTableProvider} a single
 * {@code GatherDataEvent} can register - see {@code ModLanguageProvider}'s javadoc for the same
 * "only one owner of this name" constraint ({@code LootTableProvider#getName()} returns the fixed,
 * {@code final} string {@code "Loot Tables"}, so two separate instances collide exactly like two
 * {@code LanguageProvider}s for the same locale would). Unlike lang, this needed no code to move:
 * {@code SubProviderEntry} already exists to combine independent sub-providers, so each module's own
 * {@code ModBlockLootTables} stays exactly where it was.
 */
public final class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(com.voranix.ottoke.datagen.ModBlockLootTables::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(com.voranix.maipizza.datagen.ModBlockLootTables::new, LootContextParamSets.BLOCK)
        ));
    }
}
