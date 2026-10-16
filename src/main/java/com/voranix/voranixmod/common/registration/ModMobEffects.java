package com.voranix.voranixmod.common.registration;

import com.voranix.voranixmod.VoranixMod;
import com.voranix.voranixmod.common.effect.GrowMobEffect;
import com.voranix.voranixmod.common.effect.ShrinkMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * The two native size effects (see {@code com.voranix.voranixmod.common.effect.ScaleMobEffect}) -
 * lives in {@code voranixmod}, not {@code maipizza}, because it is generic status-effect
 * infrastructure any module's food could use, not something Mai Pizza's pizzas own.
 */
public final class ModMobEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, VoranixMod.MODID);

    public static final RegistryObject<GrowMobEffect> GROW = EFFECTS.register("grow", GrowMobEffect::new);
    public static final RegistryObject<ShrinkMobEffect> SHRINK = EFFECTS.register("shrink", ShrinkMobEffect::new);

    private ModMobEffects() {
    }

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
