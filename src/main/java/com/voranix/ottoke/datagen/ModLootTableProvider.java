package com.voranix.ottoke.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

/**
 * Extends vanilla's LootTableProvider directly - {@code net.minecraftforge.common.data.LootTableProvider}
 * does not exist in this Forge version (that package only has the {@code final}, Forge-internal
 * {@code ForgeLootTableProvider}, which cannot be subclassed). Passing an empty {@code requiredTables}
 * set means only the tables this mod's own sub-providers generate are validated - nothing forces
 * generating the full vanilla table set.
 */
public final class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(ModBlockLootTables::new, LootContextParamSets.BLOCK)
        ));
    }
}
