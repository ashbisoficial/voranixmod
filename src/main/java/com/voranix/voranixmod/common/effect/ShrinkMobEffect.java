package com.voranix.voranixmod.common.effect;

import net.minecraft.world.effect.MobEffectCategory;

/** -40% size/reach per level - Pizza Margarita's added effect (see {@code MaiPizza.ModItems}). Neutral, not harmful: a smaller hitbox is a defensive upside. */
public class ShrinkMobEffect extends ScaleMobEffect {
    public ShrinkMobEffect() {
        super(MobEffectCategory.NEUTRAL, 0xAA88FF, -0.4F,
                "5b4a3f2e-1d0c-4b9a-8f7e-6d5c4b3a2f1a", "5b4a3f2e-1d0c-4b9a-8f7e-6d5c4b3a2f1b");
    }
}
