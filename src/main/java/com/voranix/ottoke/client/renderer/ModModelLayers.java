package com.voranix.ottoke.client.renderer;

import com.voranix.voranixmod.VoranixMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class ModModelLayers {
    public static final ModelLayerLocation CHEF_OTTOKE =
            new ModelLayerLocation(new ResourceLocation(VoranixMod.MODID, "chef_ottoke"), "main");

    private ModModelLayers() {
    }
}
