package com.voranix.voranixmod.common.effect;

import net.minecraft.world.effect.MobEffectCategory;

/** +50% size/reach per level - Pizza Suprema's third effect (see {@code MaiPizza.ModItems}). */
public class GrowMobEffect extends ScaleMobEffect {
    public GrowMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x55DD55, 0.5F,
                "3f6b0f2a-9b7b-4a3a-8f0b-7a1c2d3e4f5a", "3f6b0f2a-9b7b-4a3a-8f0b-7a1c2d3e4f5b");
    }
}
