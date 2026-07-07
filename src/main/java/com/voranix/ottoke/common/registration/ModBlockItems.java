package com.voranix.ottoke.common.registration;

import com.voranix.ottoke.OttokeStreetFood;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers the BlockItem for every block that must be obtainable/placeable by players.
 * Kept separate from {@link ModBlocks} because "a block exists" and "a block is an inventory item"
 * are different responsibilities (e.g. a future block-only decoration piece with no item form
 * would not force a change here).
 *
 * The item id is derived from the block's own RegistryObject id instead of being retyped, so a
 * block and its BlockItem can never drift apart under different names.
 */
public final class ModBlockItems {
    public static final DeferredRegister<Item> BLOCK_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, OttokeStreetFood.MODID);

    public static final RegistryObject<Item> FREIDORA = registerBlockItem(ModBlocks.FREIDORA);
    public static final RegistryObject<Item> CAJA_OTTOKE = registerBlockItem(ModBlocks.CAJA_OTTOKE);
    public static final RegistryObject<Item> MOSTRADOR = registerBlockItem(ModBlocks.MOSTRADOR);
    public static final RegistryObject<Item> CAJA_REGISTRADORA = registerBlockItem(ModBlocks.CAJA_REGISTRADORA);
    public static final RegistryObject<Item> BASURERO = registerBlockItem(ModBlocks.BASURERO);
    public static final RegistryObject<Item> MENU = registerBlockItem(ModBlocks.MENU);
    public static final RegistryObject<Item> CARTEL = registerBlockItem(ModBlocks.CARTEL);

    private ModBlockItems() {
    }

    private static RegistryObject<Item> registerBlockItem(RegistryObject<? extends Block> block) {
        return BLOCK_ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ITEMS.register(eventBus);
    }
}
