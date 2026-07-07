package com.voranix.ottoke;

import com.voranix.ottoke.common.registration.ModCreativeTabs;
import com.voranix.ottoke.common.registration.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(OttokeStreetFood.MODID)
public class OttokeStreetFood {
    public static final String MODID = "ottoke";

    public OttokeStreetFood() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }
}
