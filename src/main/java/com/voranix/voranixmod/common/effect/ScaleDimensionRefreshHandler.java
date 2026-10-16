package com.voranix.voranixmod.common.effect;

import com.voranix.voranixmod.common.registration.ModMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;

/**
 * Forces a hitbox/eye-height recompute while Grow/Shrink is active, instead of waiting for an
 * unrelated pose change (crouch, swim, etc.) to trigger it. {@link LivingEntity#refreshDimensions()}
 * is the only thing that re-fires {@code EntityEvent.Size} (see {@link ScaleEffectHandler}) and
 * recomputes eye height - nothing calls it just because a status effect was added or removed.
 * <p>
 * The {@code MobEffectEvent.Added}/{@code Remove}/{@code Expired} listeners below cover the instant
 * the effect starts or ends, but were observed (in-game, on the client) to still leave the
 * first-person camera at its un-scaled eye height until the player separately crouched - i.e. exactly
 * the stale-until-pose-change symptom this class exists to prevent, just shifted to a narrower window
 * instead of eliminated. Rather than chase the precise cause inside vanilla's effect-sync/event-timing
 * (fragile territory - {@code EntityEvent.Size} is itself deprecated-for-removal, see
 * {@link ScaleEffectHandler}), {@link #onLivingTick} re-asserts the refresh every tick for the
 * duration of the effect, so it's self-correcting on the very next tick even if one particular
 * transition's refresh was skipped or landed before the effect was actually queryable.
 */
public final class ScaleDimensionRefreshHandler {
    private ScaleDimensionRefreshHandler() {
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(ScaleDimensionRefreshHandler::onAdded);
        MinecraftForge.EVENT_BUS.addListener(ScaleDimensionRefreshHandler::onRemove);
        MinecraftForge.EVENT_BUS.addListener(ScaleDimensionRefreshHandler::onExpired);
        MinecraftForge.EVENT_BUS.addListener(ScaleDimensionRefreshHandler::onLivingTick);
    }

    private static void onAdded(MobEffectEvent.Added event) {
        refreshIfScaleEffect(event.getEntity(), event.getEffectInstance().getEffect());
    }

    private static void onRemove(MobEffectEvent.Remove event) {
        refreshIfScaleEffect(event.getEntity(), event.getEffect());
    }

    private static void onExpired(MobEffectEvent.Expired event) {
        refreshIfScaleEffect(event.getEntity(), event.getEffectInstance().getEffect());
    }

    private static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity living = event.getEntity();
        if (living.hasEffect(ModMobEffects.GROW.get()) || living.hasEffect(ModMobEffects.SHRINK.get())) {
            living.refreshDimensions();
        }
    }

    private static void refreshIfScaleEffect(LivingEntity living, MobEffect effect) {
        if (effect == ModMobEffects.GROW.get() || effect == ModMobEffects.SHRINK.get()) {
            living.refreshDimensions();
        }
    }
}
