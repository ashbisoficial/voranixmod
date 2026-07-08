package com.voranix.ottoke.datagen;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.registration.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

/**
 * Blockstates and block models, plus the trivial "parent = block model" item model for each block -
 * that item model has no meaning independent of the block it represents, so it stays here instead
 * of in {@link ModItemModelProvider}.
 * <p>
 * Most blocks are a plain cube_all with a borrowed vanilla texture. Banner Ottoke is the one
 * exception needing more than that: {@link #horizontalBlock} generates the four 90-degree rotated
 * variants {@code BannerOttokeBlock}'s {@code HORIZONTAL_FACING} property needs - plain blockstate
 * variants only support 90-degree increments, which is exactly the rotation granularity that block
 * uses. Cartel Luminoso needs two variants (lit/unlit), each with its own borrowed vanilla texture
 * (the lit/unlit redstone lamp textures) so the state is visible without any new art.
 */
public final class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, OttokeStreetFood.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        cubeAllBlockWithItem(ModBlocks.FREIDORA, mcLoc("block/stone"));
        cubeAllBlockWithItem(ModBlocks.CAJA_OTTOKE, mcLoc("block/oak_planks"));
        cubeAllBlockWithItem(ModBlocks.MOSTRADOR, mcLoc("block/warped_planks"));
        cubeAllBlockWithItem(ModBlocks.CAJA_REGISTRADORA, mcLoc("block/black_concrete"));
        cubeAllBlockWithItem(ModBlocks.BASURERO, mcLoc("block/gray_concrete"));
        cubeAllBlockWithItem(ModBlocks.MENU, mcLoc("block/white_concrete"));
        cubeAllBlockWithItem(ModBlocks.CARTEL, mcLoc("block/stone"));

        horizontalBlockWithItem(ModBlocks.BANNER_OTTOKE, mcLoc("block/white_wool"));

        litUnlitBlockWithItem(ModBlocks.CARTEL_LUMINOSO,
                mcLoc("block/redstone_lamp"), mcLoc("block/redstone_lamp_on"));
    }

    private void cubeAllBlockWithItem(RegistryObject<? extends Block> block, ResourceLocation texture) {
        ModelFile model = models().cubeAll(block.getId().getPath(), texture);
        simpleBlockWithItem(block.get(), model);
    }

    private void horizontalBlockWithItem(RegistryObject<? extends Block> block, ResourceLocation texture) {
        ModelFile model = models().cubeAll(block.getId().getPath(), texture);
        horizontalBlock(block.get(), model);
        simpleBlockItem(block.get(), model);
    }

    private void litUnlitBlockWithItem(RegistryObject<? extends Block> block, ResourceLocation unlitTexture, ResourceLocation litTexture) {
        String name = block.getId().getPath();
        ModelFile unlitModel = models().cubeAll(name, unlitTexture);
        ModelFile litModel = models().cubeAll(name + "_lit", litTexture);

        getVariantBuilder(block.get())
                .partialState().with(BlockStateProperties.LIT, false).setModels(new ConfiguredModel(unlitModel))
                .partialState().with(BlockStateProperties.LIT, true).setModels(new ConfiguredModel(litModel));

        simpleBlockItem(block.get(), unlitModel);
    }
}
