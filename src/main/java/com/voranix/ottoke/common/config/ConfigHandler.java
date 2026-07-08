package com.voranix.ottoke.common.config;

import com.voranix.ottoke.OttokeStreetFood;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class ConfigHandler {
    private ConfigHandler() {
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OttokeConfig.SPEC, OttokeStreetFood.MODID + "-common.toml");
    }
}
