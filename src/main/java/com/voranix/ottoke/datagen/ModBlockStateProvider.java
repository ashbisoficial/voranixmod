package com.voranix.ottoke.datagen;

import com.voranix.voranixmod.VoranixMod;
import com.voranix.ottoke.common.cooking.FreidoraBlock;
import com.voranix.ottoke.common.registration.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

/**
 * Blockstates and block models, plus the trivial "parent = block model" item model for each block -
 * that item model has no meaning independent of the block it represents, so it stays here instead
 * of in {@link ModItemModelProvider}.
 * <p>
 * Blocks without real art yet stay a plain cube_all with a borrowed vanilla texture. Banner Ottoke,
 * Cartel/Cartel Luminoso, Menu and Freidora have real Blockbench-authored models (statically checked-in
 * JSON under {@code models/block/}, since their geometry - multiple elements, hand-placed UVs - is well
 * beyond what a generated cube can express); this provider only wires blockstate variants to those
 * existing files, it does not regenerate their geometry. {@link #horizontalBlock} still generates
 * the four 90-degree rotated variants every {@code HorizontalPanelBlock} subclass's
 * {@code HORIZONTAL_FACING} needs - Menu, Banner Ottoke, Cartel, and Cartel Luminoso all place
 * against whichever wall the player is facing instead of being locked to one fixed direction.
 * <p>
 * Cartel and Cartel Luminoso are two separate blocks that deliberately render identically - same
 * {@code cartel_panel} model, same texture - the only difference between them is that Cartel
 * Luminoso emits light (wired in {@code ModBlocks}) and can be toggled. Cartel Luminoso's LIT
 * variants also share that one model: there's no separate "glowing" texture yet, so only the
 * emitted light itself marks it as lit. Combined with its inherited {@code FACING}, that's 4
 * rotations x 2 LIT states = 8 blockstate entries, all pointing at the same model file -
 * {@link #customHorizontalBooleanStateBlockWithItem} generates that combination. Freidora's
 * {@code COOKING} needs the same FACING x property treatment, since {@code FreidoraBlock} also
 * rotates on placement (see that class for why it can't just extend {@code HorizontalPanelBlock}
 * like the others).
 */
public final class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, VoranixMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        cubeAllBlockWithItem(ModBlocks.CAJA_OTTOKE, mcLoc("block/oak_planks"));
        cubeAllBlockWithItem(ModBlocks.MOSTRADOR, mcLoc("block/warped_planks"));
        cubeAllBlockWithItem(ModBlocks.CAJA_REGISTRADORA, mcLoc("block/black_concrete"));
        cubeAllBlockWithItem(ModBlocks.BASURERO, mcLoc("block/gray_concrete"));

        customHorizontalBooleanStateBlockWithItem(ModBlocks.FREIDORA, modLoc("block/freidora"), FreidoraBlock.COOKING);
        customHorizontalBlockWithItem(ModBlocks.MENU, modLoc("block/menu_board"));
        customHorizontalBlockWithItem(ModBlocks.BANNER_OTTOKE, modLoc("block/banner_ottoke"));
        customHorizontalBlockWithItem(ModBlocks.CARTEL, modLoc("block/cartel_panel"));
        customHorizontalBooleanStateBlockWithItem(ModBlocks.CARTEL_LUMINOSO, modLoc("block/cartel_panel"), BlockStateProperties.LIT);
    }

    private void cubeAllBlockWithItem(RegistryObject<? extends Block> block, ResourceLocation texture) {
        ModelFile model = models().cubeAll(block.getId().getPath(), texture);
        simpleBlockWithItem(block.get(), model);
    }

    private void customHorizontalBlockWithItem(RegistryObject<? extends Block> block, ResourceLocation modelLocation) {
        ModelFile model = models().getExistingFile(modelLocation);
        horizontalBlock(block.get(), model);
        simpleBlockItem(block.get(), model);
    }

    /**
     * One-model-for-every-state shortcut combined with {@link #horizontalBlock}'s per-facing
     * rotation - for a block with a {@code FACING} property and one extra {@link BooleanProperty}
     * that doesn't change its look ({@code COOKING} on Freidora, {@code LIT} on Cartel Luminoso -
     * both exist to drive {@code lightLevel}, see {@code ModBlocks}/{@code FreidoraBlock}, not a
     * different model). The {@code Function} overload of {@code horizontalBlock} generates the full
     * FACING x property cross product (4 rotations x 2 states) and still only ever points at the one
     * model file.
     */
    private void customHorizontalBooleanStateBlockWithItem(RegistryObject<? extends Block> block, ResourceLocation modelLocation, BooleanProperty property) {
        ModelFile model = models().getExistingFile(modelLocation);
        horizontalBlock(block.get(), state -> model);
        simpleBlockItem(block.get(), model);
    }
}
