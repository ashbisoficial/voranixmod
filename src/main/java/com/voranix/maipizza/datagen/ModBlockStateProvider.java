package com.voranix.maipizza.datagen;

import com.voranix.maipizza.common.registration.ModBlocks;
import com.voranix.voranixmod.VoranixMod;
import com.voranix.voranixmod.common.block.SliceableFoodBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

/**
 * Blockstates and block models for Mai Pizza. Pizza Margarita and Pizza Cuatro Quesos already have
 * real Blockbench geometry (4 models each, one per {@code SliceableFoodBlock.SLICES_EATEN} state -
 * see {@link #pizzaWithModels}); their textures (referenced as {@code block/masa}/{@code block/texture})
 * aren't in the repo yet though - Blockbench's "Java Block/Item" export only carries texture *name*
 * references, not the actual PNG pixels, so those still need to be exported/handed over separately
 * and dropped at {@code assets/voranixmod/textures/block/masa.png} etc. Every other block here
 * (including the three pizzas with no geometry yet) is a generated {@code cube_all} against a
 * borrowed vanilla texture, the same placeholder convention
 * {@code com.voranix.ottoke.datagen.ModBlockStateProvider} already uses for its own not-yet-
 * illustrated blocks (Caja Ottoke, Mostrador, etc).
 */
public final class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, VoranixMod.MODID, existingFileHelper);
    }

    /**
     * {@code BlockStateProvider#getName()} returns {@code "Block States: " + modid} with no way to
     * factor in which module owns the provider - Ottoke's own {@code ModBlockStateProvider} shares
     * this modid, so both would collide under the identical default name without this override (see
     * {@code com.voranix.voranixcoins.datagen.ModItemModelProvider} for the same fix applied there
     * first).
     */
    @Override
    public String getName() {
        return "Block States: " + VoranixMod.MODID + " (maipizza)";
    }

    @Override
    protected void registerStatesAndModels() {
        cubeAllHorizontalBooleanStateBlockWithItem(ModBlocks.HORNO_DE_BARRO, mcLoc("block/bricks"));
        cubeAllHorizontalBlockWithItem(ModBlocks.MENU, mcLoc("block/oak_planks"));
        cubeAllHorizontalBlockWithItem(ModBlocks.CARTEL, mcLoc("block/oak_planks"));
        cubeAllHorizontalBooleanStateBlockWithItem(ModBlocks.CARTEL_LUMINOSO, mcLoc("block/glowstone"));

        // Real Blockbench models: completa/34/12/14, one per SLICES_EATEN state (0-3) - see
        // SliceableFoodBlock for why there are exactly 4.
        pizzaWithModels(ModBlocks.PIZZA_MARGARITA, new ModelFile[]{
                models().getExistingFile(modLoc("pizza_margarita_completa")),
                models().getExistingFile(modLoc("pizza_margarita_34")),
                models().getExistingFile(modLoc("pizza_margarita_12")),
                models().getExistingFile(modLoc("pizza_margarita_14"))
        });

        pizzaWithModels(ModBlocks.PIZZA_CUATRO_QUESOS, new ModelFile[]{
                models().getExistingFile(modLoc("pizza_queso_completa")),
                models().getExistingFile(modLoc("pizza_queso_34")),
                models().getExistingFile(modLoc("pizza_queso_12")),
                models().getExistingFile(modLoc("pizza_queso_14"))
        });

        // No Blockbench models yet for these three - placeholder cube shared across all 4 bite
        // states, same convention as Horno/Menú/Cartel above. Swap for real per-slice models
        // (mirroring PIZZA_MARGARITA/PIZZA_CUATRO_QUESOS above) once they exist.
        placeholderPizza(ModBlocks.PIZZA_PEPPERONI, mcLoc("block/red_terracotta"));
        placeholderPizza(ModBlocks.PIZZA_CHAMPINON, mcLoc("block/brown_terracotta"));
        placeholderPizza(ModBlocks.PIZZA_SUPREMA, mcLoc("block/orange_terracotta"));
    }

    /** One model per {@link SliceableFoodBlock#SLICES_EATEN} value (index 0 = whole pizza) - the item icon reuses the whole-pizza model. */
    private void pizzaWithModels(RegistryObject<? extends Block> block, ModelFile[] modelsBySlicesEaten) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            int slicesEaten = state.getValue(SliceableFoodBlock.SLICES_EATEN);
            return ConfiguredModel.builder().modelFile(modelsBySlicesEaten[slicesEaten]).build();
        });
        itemModels().getBuilder(block.getId().getPath()).parent(modelsBySlicesEaten[0]);
    }

    private void placeholderPizza(RegistryObject<? extends Block> block, ResourceLocation texture) {
        ModelFile model = models().cubeAll(block.getId().getPath(), texture);
        getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(model).build());
        simpleBlockItem(block.get(), model);
    }

    private void cubeAllHorizontalBlockWithItem(RegistryObject<? extends Block> block, ResourceLocation texture) {
        ModelFile model = models().cubeAll(block.getId().getPath(), texture);
        horizontalBlock(block.get(), model);
        simpleBlockItem(block.get(), model);
    }

    /** Same shortcut as above, but via the {@code Function} overload so it also covers a second blockstate property (COOKING/LIT). */
    private void cubeAllHorizontalBooleanStateBlockWithItem(RegistryObject<? extends Block> block, ResourceLocation texture) {
        ModelFile model = models().cubeAll(block.getId().getPath(), texture);
        horizontalBlock(block.get(), state -> model);
        simpleBlockItem(block.get(), model);
    }
}
