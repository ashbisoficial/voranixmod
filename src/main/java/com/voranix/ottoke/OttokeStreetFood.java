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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(OttokeStreetFood.MODID)
public class OttokeStreetFood {
    public static final String MODID = "ottoke";

    public OttokeStreetFood() {
        ConfigHandler.register();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModRegistries.registerAll(modEventBus);
        ModVillagerRegistration.register(modEventBus);
        ModDataGenerators.register(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(OttokeStreetFood::clientSetup));
    }

    public static void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.FREIDORA.get(), FreidoraScreen::new);
    }
}
