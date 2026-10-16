package com.voranix.ottoke.client.renderer;

import com.voranix.ottoke.common.villager.OttokeChefProfession;
import com.voranix.voranixmod.VoranixMod;
import com.voranix.voranixmod.client.villager.ChefVillagerRenderDefinition;
import com.voranix.voranixmod.client.villager.ChefVillagerRenderRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Bakes Chef Ottoke's model layer and hands it to {@link ChefVillagerRenderRegistry} - see that class
 * and {@code voranixmod.client.villager.VoranixChefVillagerRenderer} for why the actual villager
 * renderer registration lives in core instead of here.
 */
@Mod.EventBusSubscriber(modid = VoranixMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModEntityRenderEvents {
    private static final ResourceLocation CHEF_TEXTURE =
            new ResourceLocation(VoranixMod.MODID, "textures/entity/chef_ottoke.png");

    private ModEntityRenderEvents() {
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.CHEF_OTTOKE, ChefOttokeModel::createBodyLayer);
        ChefVillagerRenderRegistry.register(new ChefVillagerRenderDefinition(
                OttokeChefProfession.PROFESSION, ModModelLayers.CHEF_OTTOKE, CHEF_TEXTURE, ChefOttokeModel::new));
    }
}
