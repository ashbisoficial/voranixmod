package com.voranix.maipizza;

import com.voranix.maipizza.client.screen.HornoDeBarroScreen;
import com.voranix.maipizza.common.config.ConfigHandler;
import com.voranix.maipizza.common.registration.ModMenuTypes;
import com.voranix.maipizza.common.registration.ModRegistries;
import com.voranix.maipizza.common.registration.ModVillagerRegistration;
import com.voranix.maipizza.datagen.ModDataGenerators;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Mai Pizza's own init entry point, mirroring {@code com.voranix.ottoke.OttokeModule} - see that
 * class and {@code com.voranix.voranixmod.VoranixMod} for the shared {@code @Mod} entry point and
 * modid this module is initialized onto.
 * <p>
 * Chef Mai Pizza's model/renderer wiring ({@code com.voranix.maipizza.client.renderer}) is not called
 * from here - like Chef Ottoke's, it registers itself via {@code @Mod.EventBusSubscriber} on
 * {@code EntityRenderersEvent}, feeding {@code voranixmod.client.villager.ChefVillagerRenderRegistry}
 * so the shared {@code VoranixChefVillagerRenderer} in core picks it up.
 */
public final class MaiPizzaModule {
    private MaiPizzaModule() {
    }

    public static void init(IEventBus modEventBus) {
        ConfigHandler.register();

        ModRegistries.registerAll(modEventBus);
        ModVillagerRegistration.register(modEventBus);
        ModDataGenerators.register(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(MaiPizzaModule::clientSetup));
    }

    public static void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.HORNO_DE_BARRO.get(), HornoDeBarroScreen::new);
    }
}
