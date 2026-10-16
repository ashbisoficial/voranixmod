package com.voranix.voranixmod.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

/**
 * Base for the native (no external mod) size effects the user asked for - "Agrandar"/"Encoger".
 * Reach scales with body size via Forge's own {@code ForgeMod.ENTITY_REACH}/{@code BLOCK_REACH}
 * attributes (confirmed present in Forge 47.2.0) using the exact
 * {@code MobEffect#addAttributeModifier} pattern vanilla's own Speed/Haste effects use - see
 * {@code MobEffects.MOVEMENT_SPEED} for the reference this mirrors. Visual scale and hitbox are
 * NOT attributes (no vanilla/Forge attribute drives either pre-1.20.5) - see
 * {@link ScaleEffectHandler} (hitbox, both sides) and {@code com.voranix.voranixmod.client.ScaleRenderHandler}
 * (visual, client only) for those two hooks, which both read {@link #scaleFactor(int)} off
 * whichever concrete subclass ({@code GrowMobEffect}/{@code ShrinkMobEffect}) is active.
 */
public abstract class ScaleMobEffect extends MobEffect {
    private final float scaleDeltaPerLevel;

    protected ScaleMobEffect(MobEffectCategory category, int color, float scaleDeltaPerLevel,
                              String entityReachModifierUuid, String blockReachModifierUuid) {
        super(category, color);
        this.scaleDeltaPerLevel = scaleDeltaPerLevel;
        this.addAttributeModifier(ForgeMod.ENTITY_REACH.get(), entityReachModifierUuid,
                scaleDeltaPerLevel, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(ForgeMod.BLOCK_REACH.get(), blockReachModifierUuid,
                scaleDeltaPerLevel, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    /** Same per-amplifier scaling vanilla attribute modifiers already get for free: level 0 applies once, level 1 twice, etc. */
    public float scaleFactor(int amplifier) {
        return 1.0F + this.scaleDeltaPerLevel * (amplifier + 1);
    }
}
