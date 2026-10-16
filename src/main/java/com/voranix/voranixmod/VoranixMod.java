package com.voranix.voranixmod;

import com.voranix.maipizza.MaiPizzaModule;
import com.voranix.ottoke.OttokeModule;
import com.voranix.voranixcoins.VoranixCoinsModule;
import com.voranix.voranixmod.client.ScaleRenderHandler;
import com.voranix.voranixmod.common.effect.ScaleDimensionRefreshHandler;
import com.voranix.voranixmod.common.effect.ScaleEffectHandler;
import com.voranix.voranixmod.common.registration.ModMobEffects;
import com.voranix.voranixmod.datagen.ModDataGenerators;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * VoranixMod's single {@code @Mod} entry point - the one class Forge actually loads. Everything
 * else is a self-contained module ({@link OttokeModule}, {@link VoranixCoinsModule},
 * {@link MaiPizzaModule}) initialized from here onto the one shared mod event bus, so adding a
 * module means one more {@code XyzModule.init(modEventBus)} call in this constructor, not a new
 * {@code @Mod} class or a second jar.
 */
@Mod(VoranixMod.MODID)
public class VoranixMod {
    public static final String MODID = "voranixmod";

    public VoranixMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModMobEffects.register(modEventBus);
        ScaleEffectHandler.register();
        ScaleDimensionRefreshHandler.register();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ScaleRenderHandler::register);

        OttokeModule.init(modEventBus);
        VoranixCoinsModule.init(modEventBus);
        MaiPizzaModule.init(modEventBus);
        ModDataGenerators.register(modEventBus);
    }
}
