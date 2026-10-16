package com.voranix.maipizza.client.renderer;

import com.voranix.maipizza.common.villager.MaiPizzaChefProfession;
import com.voranix.voranixmod.VoranixMod;
import com.voranix.voranixmod.client.villager.ChefVillagerRenderDefinition;
import com.voranix.voranixmod.client.villager.ChefVillagerRenderRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Bakes Chef Mai Pizza's model layer and hands it to {@link ChefVillagerRenderRegistry} - mirrors
 * {@code com.voranix.ottoke.client.renderer.ModEntityRenderEvents} exactly, see that class and
 * {@code voranixmod.client.villager.VoranixChefVillagerRenderer} for why the actual villager renderer
 * registration lives in core instead of here.
 */
@Mod.EventBusSubscriber(modid = VoranixMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModEntityRenderEvents {
    private static final ResourceLocation CHEF_TEXTURE =
            new ResourceLocation(VoranixMod.MODID, "textures/entity/chef_maipizza.png");

    private ModEntityRenderEvents() {
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.CHEF_MAIPIZZA, ChefMaiPizzaModel::createBodyLayer);
        ChefVillagerRenderRegistry.register(new ChefVillagerRenderDefinition(
                MaiPizzaChefProfession.PROFESSION, ModModelLayers.CHEF_MAIPIZZA, CHEF_TEXTURE, ChefMaiPizzaModel::new));
    }
}
