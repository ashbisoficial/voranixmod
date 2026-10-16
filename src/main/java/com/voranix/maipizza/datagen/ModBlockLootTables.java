package com.voranix.maipizza.datagen;

import com.voranix.maipizza.common.registration.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Mirrors {@code com.voranix.ottoke.datagen.ModBlockLootTables} - every decorative block drops itself. */
public final class ModBlockLootTables extends BlockLootSubProvider {
    private static final List<RegistryObject<Block>> DROPS_SELF = List.of(
            ModBlocks.HORNO_DE_BARRO, ModBlocks.MENU, ModBlocks.CARTEL, ModBlocks.CARTEL_LUMINOSO
    );

    /** Pizzas drop nothing when broken, same as vanilla cake - they're meant to be eaten, not mined. */
    private static final List<RegistryObject<Block>> NO_DROPS = List.of(
            ModBlocks.PIZZA_MARGARITA, ModBlocks.PIZZA_PEPPERONI, ModBlocks.PIZZA_CHAMPINON,
            ModBlocks.PIZZA_CUATRO_QUESOS, ModBlocks.PIZZA_SUPREMA
    );

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        DROPS_SELF.forEach(block -> dropSelf(block.get()));
        NO_DROPS.forEach(block -> add(block.get(), LootTable.lootTable()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Stream.concat(DROPS_SELF.stream(), NO_DROPS.stream()).map(RegistryObject::get).collect(Collectors.toList());
    }
}
