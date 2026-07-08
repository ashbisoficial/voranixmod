package com.voranix.ottoke.common.registration;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.block.BannerOttokeBlock;
import com.voranix.ottoke.common.block.CartelLuminosoBlock;
import com.voranix.ottoke.common.block.MenuBoardBlock;
import com.voranix.ottoke.common.cooking.FreidoraBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Registers Block instances only. BlockItems are a separate concern owned by {@link ModBlockItems},
 * so this class never needs to know that some blocks are obtainable in inventories and others might not be.
 */
public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, OttokeStreetFood.MODID);

    public static final RegistryObject<Block> FREIDORA = registerBlock("freidora", FreidoraBlock::new);
    public static final RegistryObject<Block> CAJA_OTTOKE = registerBlock("caja_ottoke",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5f, 4.0f)));
    public static final RegistryObject<Block> MOSTRADOR = registerBlock("mostrador",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.8f, 4.5f)));
    public static final RegistryObject<Block> CAJA_REGISTRADORA = registerBlock("caja_registradora",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(1.2f, 3.0f)));
    public static final RegistryObject<Block> BASURERO = registerBlock("basurero",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.0f, 2.0f)));
    public static final RegistryObject<Block> MENU = registerBlock("menu",
            () -> new MenuBoardBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).strength(0.8f, 2.0f)));
    public static final RegistryObject<Block> CARTEL = registerBlock("cartel",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).strength(0.8f, 2.0f)));
    public static final RegistryObject<Block> BANNER_OTTOKE = registerBlock("banner_ottoke",
            () -> new BannerOttokeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.0f, 2.0f)));
    public static final RegistryObject<Block> CARTEL_LUMINOSO = registerBlock("cartel_luminoso",
            () -> new CartelLuminosoBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(0.8f, 2.0f)
                    .lightLevel(state -> state.getValue(CartelLuminosoBlock.LIT) ? 15 : 0)));

    private ModBlocks() {
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockSupplier) {
        return BLOCKS.register(name, blockSupplier);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
