package com.voranix.voranixmod.client.villager;

import com.voranix.voranixmod.VoranixMod;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Registers {@link VoranixChefVillagerRenderer} as the one renderer for
 * {@link EntityType#VILLAGER}. Lives in {@code voranixmod} core - not in whichever module happens to
 * register a custom chef first - so it stays the single, unambiguous owner of that entity type's
 * renderer no matter how many modules add a chef to {@link ChefVillagerRenderRegistry}.
 */
@Mod.EventBusSubscriber(modid = VoranixMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModVillagerRenderEvents {
    private ModVillagerRenderEvents() {
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityType.VILLAGER, VoranixChefVillagerRenderer::new);
    }
}
