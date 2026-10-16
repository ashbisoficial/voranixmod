package com.voranix.voranixmod.common.effect;

import com.voranix.voranixmod.common.registration.ModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;

/**
 * Hitbox half of the scale effects - common to both sides (collision must agree client/server),
 * unlike the visual half in {@code com.voranix.voranixmod.client.ScaleRenderHandler}. Registered on
 * {@link MinecraftForge#EVENT_BUS} like {@code OttokeChefTrades}: {@link EntityEvent.Size} is a
 * game-bus event too, not a mod-bus one.
 * <p>
 * {@link EntityEvent.Size} is deprecated-for-removal in Forge itself ("needs to be redesigned"),
 * but it's still the only hitbox-resize hook Forge 47.2.0 (this project's pinned version) ships -
 * nothing newer exists to migrate to on 1.20.1.
 */
public final class ScaleEffectHandler {
    private ScaleEffectHandler() {
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(ScaleEffectHandler::onEntitySize);
    }

    private static void onEntitySize(EntityEvent.Size event) {
        // isAddedToWorld() guards the exact case EntityEvent.Size's own javadoc warns about: this
        // event also fires from inside Entity's constructor, before LivingEntity's own field
        // initializers (activeEffects included) have run - calling getEffect() that early throws
        // (activeEffects is still null), crashing every player join. isAddedToWorld() is a plain
        // boolean, safe to read at that point (zero-initialized, not a null Map), and is false
        // until the entity actually finishes spawning into a level - exactly when there's nothing
        // to scale yet anyway.
        if (!(event.getEntity() instanceof LivingEntity living) || !living.isAddedToWorld()) {
            return;
        }
        float factor = currentScaleFactor(living);
        if (factor != 1.0F) {
            // NOT the 2-arg setNewSize(size, true) overload: its "recompute eye height" path calls
            // Entity#getEyeHeight(pose, newSize), and Player overrides that
            // (Player#getStandingEyeHeight) to always return a hardcoded 1.62F/1.27F/0.4F per pose,
            // completely ignoring the EntityDimensions argument - so that recompute silently
            // produces the SAME un-scaled eye height and the first-person camera never actually
            // moves. event.getNewEyeHeight() here still holds vanilla's correct un-scaled value for
            // the entity's current pose (computed before this listener ran), so scaling it directly
            // is the only way this actually reaches the camera.
            event.setNewSize(event.getNewSize().scale(factor));
            event.setNewEyeHeight(event.getNewEyeHeight() * factor);
        }
    }

    /** Combined Grow x Shrink factor for whichever of the two are currently active on this entity - shared with {@code ScaleRenderHandler}. */
    public static float currentScaleFactor(LivingEntity living) {
        float factor = 1.0F;
        MobEffectInstance grow = living.getEffect(ModMobEffects.GROW.get());
        if (grow != null) {
            factor *= ModMobEffects.GROW.get().scaleFactor(grow.getAmplifier());
        }
        MobEffectInstance shrink = living.getEffect(ModMobEffects.SHRINK.get());
        if (shrink != null) {
            factor *= ModMobEffects.SHRINK.get().scaleFactor(shrink.getAmplifier());
        }
        return factor;
    }
}
