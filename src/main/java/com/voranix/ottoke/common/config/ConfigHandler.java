package com.voranix.ottoke.common.config;

import com.voranix.voranixmod.VoranixMod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class ConfigHandler {
    private ConfigHandler() {
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OttokeConfig.SPEC, VoranixMod.MODID + "-common.toml");
    }
}
