package com.voranix.ottoke.datagen;

import com.voranix.ottoke.common.registration.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * All current Ottoke blocks are simple furniture with no block entity contents worth preserving on
 * break, so every one of them just drops itself. Once a block needs a different drop rule (e.g. the
 * Freidora dropping its held item stacks), add its own branch in {@link #generate()} instead of
 * folding it into DROPS_SELF.
 */
public final class ModBlockLootTables extends BlockLootSubProvider {
    private static final List<RegistryObject<Block>> DROPS_SELF = List.of(
            ModBlocks.FREIDORA, ModBlocks.CAJA_OTTOKE, ModBlocks.MOSTRADOR,
            ModBlocks.CAJA_REGISTRADORA, ModBlocks.BASURERO, ModBlocks.MENU, ModBlocks.CARTEL,
            ModBlocks.BANNER_OTTOKE, ModBlocks.CARTEL_LUMINOSO
    );

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        DROPS_SELF.forEach(block -> dropSelf(block.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return DROPS_SELF.stream().map(RegistryObject::get).collect(Collectors.toList());
    }
}
