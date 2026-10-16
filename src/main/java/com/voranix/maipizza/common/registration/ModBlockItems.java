package com.voranix.maipizza.common.registration;

import com.voranix.voranixmod.VoranixMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** Mirrors {@code com.voranix.ottoke.common.registration.ModBlockItems}. */
public final class ModBlockItems {
    public static final DeferredRegister<Item> BLOCK_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, VoranixMod.MODID);

    public static final RegistryObject<Item> HORNO_DE_BARRO = registerBlockItem(ModBlocks.HORNO_DE_BARRO);
    public static final RegistryObject<Item> MENU = registerBlockItem(ModBlocks.MENU);
    public static final RegistryObject<Item> CARTEL = registerBlockItem(ModBlocks.CARTEL);
    public static final RegistryObject<Item> CARTEL_LUMINOSO = registerBlockItem(ModBlocks.CARTEL_LUMINOSO);
    public static final RegistryObject<Item> PIZZA_MARGARITA = registerBlockItem(ModBlocks.PIZZA_MARGARITA);
    public static final RegistryObject<Item> PIZZA_PEPPERONI = registerBlockItem(ModBlocks.PIZZA_PEPPERONI);
    public static final RegistryObject<Item> PIZZA_CHAMPINON = registerBlockItem(ModBlocks.PIZZA_CHAMPINON);
    public static final RegistryObject<Item> PIZZA_CUATRO_QUESOS = registerBlockItem(ModBlocks.PIZZA_CUATRO_QUESOS);
    public static final RegistryObject<Item> PIZZA_SUPREMA = registerBlockItem(ModBlocks.PIZZA_SUPREMA);

    private ModBlockItems() {
    }

    private static RegistryObject<Item> registerBlockItem(RegistryObject<? extends Block> block) {
        return BLOCK_ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ITEMS.register(eventBus);
    }
}
