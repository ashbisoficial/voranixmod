package com.voranix.voranixcoins.datagen;

import com.voranix.voranixmod.VoranixMod;
import com.voranix.voranixcoins.common.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public final class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, VoranixMod.MODID, existingFileHelper);
    }

    /**
     * {@code ItemModelProvider#getName()} returns {@code "Item Models: " + modid} with no way to
     * factor in which module owns the provider - since Ottoke's own {@code ModItemModelProvider}
     * shares this modid, both would collide under the identical default name (Forge's
     * {@code DataGenerator} rejects two providers with the same {@code getName()} in one
     * {@code GatherDataEvent}) without this override.
     */
    @Override
    public String getName() {
        return "Item Models: " + VoranixMod.MODID + " (voranixcoins)";
    }

    @Override
    protected void registerModels() {
        flatItem(ModItems.COIN_ESTANO);
        flatItem(ModItems.COIN_SILVER);
        flatItem(ModItems.COIN_GOLD);
        flatItem(ModItems.COIN_MITHRIL);
        flatItem(ModItems.COIN_ORICALCO);
        flatItem(ModItems.COIN_AJNA);
    }

    private void flatItem(RegistryObject<?> item) {
        String path = item.getId().getPath();
        withExistingParent(path, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + path));
    }
}
