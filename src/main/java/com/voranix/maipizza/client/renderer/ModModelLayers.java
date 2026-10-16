package com.voranix.maipizza.client.renderer;

import com.voranix.voranixmod.VoranixMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class ModModelLayers {
    public static final ModelLayerLocation CHEF_MAIPIZZA =
            new ModelLayerLocation(new ResourceLocation(VoranixMod.MODID, "chef_maipizza"), "main");

    private ModModelLayers() {
    }
}
