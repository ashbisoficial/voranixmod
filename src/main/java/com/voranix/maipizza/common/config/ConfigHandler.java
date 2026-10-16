package com.voranix.maipizza.common.config;

import com.voranix.voranixmod.VoranixMod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

/** Mirrors {@code com.voranix.ottoke.common.config.ConfigHandler} - own toml file, same modid. */
public final class ConfigHandler {
    private ConfigHandler() {
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MaiPizzaConfig.SPEC, VoranixMod.MODID + "-maipizza.toml");
    }
}
