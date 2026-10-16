package com.voranix.voranixmod.client;

import com.voranix.voranixmod.common.effect.ScaleEffectHandler;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Visual half of the scale effects - client-only, unlike the hitbox half in
 * {@link ScaleEffectHandler} (which both sides need for collision to agree). Only registered from
 * {@code VoranixMod} inside a {@code DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ...)} guard: this
 * class references client-only rendering types ({@code RenderPlayerEvent}), so merely loading it
 * on a dedicated server would crash.
 */
public final class ScaleRenderHandler {
    private ScaleRenderHandler() {
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(ScaleRenderHandler::onRenderPlayerPre);
    }

    private static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        float factor = ScaleEffectHandler.currentScaleFactor(event.getEntity());
        if (factor != 1.0F) {
            event.getPoseStack().scale(factor, factor, factor);
        }
    }
}
