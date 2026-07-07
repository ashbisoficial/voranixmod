package com.voranix.ottoke;

import com.voranix.ottoke.client.screen.FreidoraScreen;
import com.voranix.ottoke.common.registration.ModBlockEntities;
import com.voranix.ottoke.common.registration.ModBlocks;
import com.voranix.ottoke.common.registration.ModCreativeTabs;
import com.voranix.ottoke.common.registration.ModItems;
import com.voranix.ottoke.common.registration.ModMenuTypes;
import com.voranix.ottoke.common.registration.ModRecipes;
import com.voranix.ottoke.common.registration.ModVillagerRegistration;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(OttokeStreetFood.MODID)
public class OttokeStreetFood {
    public static final String MODID = "ottoke";

    public OttokeStreetFood() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModVillagerRegistration.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MenuScreens.register(ModMenuTypes.FREIDORA.get(), FreidoraScreen::new));
    }
}
