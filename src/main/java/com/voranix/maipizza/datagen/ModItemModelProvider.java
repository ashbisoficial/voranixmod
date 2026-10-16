package com.voranix.maipizza.datagen;

import com.voranix.maipizza.common.registration.ModItems;
import com.voranix.voranixmod.VoranixMod;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

/**
 * Item models for Mai Pizza's standalone items. No real art exists yet (see the user's own
 * "despues te agrego los modelos y pngs"), and unlike a block's blockstate JSON, an item model's
 * {@code texture} entries ARE validated against {@link ExistingFileHelper} - pointing one at this
 * mod's own not-yet-existing PNGs (the way {@code com.voranix.ottoke.datagen.ModItemModelProvider}'s
 * {@code flatItem} normally would) crashes datagen outright rather than just rendering as a missing
 * texture in-game. Every entry below borrows a vanilla texture instead, the same convention
 * Ottoke's own {@code ModIngredients} entries use for their still-unillustrated ingredients. Swap
 * each {@link #flatItem} call's texture argument for {@code modLoc("item/" + path)} once the real
 * PNG lands at that path.
 */
public final class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, VoranixMod.MODID, existingFileHelper);
    }

    /** See {@code com.voranix.voranixcoins.datagen.ModItemModelProvider#getName} - same collision, this time against Ottoke's own provider. */
    @Override
    public String getName() {
        return "Item Models: " + VoranixMod.MODID + " (maipizza)";
    }

    @Override
    protected void registerModels() {
        // The five cooked pizzas are PizzaBlocks now - their item models are generated in
        // ModBlockStateProvider (block models double as their own item models via simpleBlockItem).
        flatItem(ModItems.PIZZA_MARGARITA_CRUDA, mcLoc("item/wheat"));
        flatItem(ModItems.PIZZA_PEPPERONI_CRUDA, mcLoc("item/wheat"));
        flatItem(ModItems.PIZZA_CHAMPINON_CRUDA, mcLoc("item/wheat"));
        flatItem(ModItems.PIZZA_CUATRO_QUESOS_CRUDA, mcLoc("item/wheat"));
        flatItem(ModItems.PIZZA_SUPREMA_CRUDA, mcLoc("item/wheat"));
        flatItem(ModItems.HUEVO_MAIPIZZA, mcLoc("item/egg"));
    }

    private ItemModelBuilder flatItem(RegistryObject<?> item, ResourceLocation texture) {
        return withExistingParent(item.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", texture);
    }
}
