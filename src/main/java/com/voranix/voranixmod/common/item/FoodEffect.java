package com.voranix.voranixmod.common.item;

import net.minecraft.world.effect.MobEffectInstance;

import java.util.function.Supplier;

/**
 * One status effect a food can apply on eating, plus the chance it triggers - mirrors the
 * (Supplier&lt;MobEffectInstance&gt;, probability) pair FoodProperties.Builder#effect already takes.
 * <p>
 * Not implemented yet: no food currently populates a non-empty effects list (see FoodDefinition).
 * This type exists so the slot is real in the architecture - adding an effect to a food later is
 * "add one FoodEffect to its list", not a change to OttokeFoodItem or the food hierarchy itself.
 */
public record FoodEffect(Supplier<MobEffectInstance> effect, float probability) {
}
