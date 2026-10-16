package com.voranix.ottoke;

import com.voranix.ottoke.client.screen.FreidoraScreen;
import com.voranix.ottoke.common.config.ConfigHandler;
import com.voranix.ottoke.common.registration.ModMenuTypes;
import com.voranix.ottoke.common.registration.ModRegistries;
import com.voranix.ottoke.common.registration.ModVillagerRegistration;
import com.voranix.ottoke.datagen.ModDataGenerators;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Ottoke Street Food's own init entry point - registers everything this module owns onto whichever
 * mod event bus {@link #init} is handed. No longer an {@code @Mod} class itself: VoranixMod is a
 * single-jar umbrella that will eventually also host Voranix Coins and Mai Pizza as sibling
 * modules, each with its own {@code XyzModule.init(IEventBus)} called from
 * {@code com.voranix.voranixmod.VoranixMod}'s constructor - see that class for the actual
 * {@code @Mod} entry point and the shared modid.
 */
public final class OttokeModule {
    private OttokeModule() {
    }

    public static void init(IEventBus modEventBus) {
        ConfigHandler.register();

        ModRegistries.registerAll(modEventBus);
        ModVillagerRegistration.register(modEventBus);
        ModDataGenerators.register(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(OttokeModule::clientSetup));
    }

    public static void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.FREIDORA.get(), FreidoraScreen::new);
    }
}
