package com.voranix.ottoke.compat;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.compat.top.TopCompat;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

/**
 * The One Probe is the one integration that has to be initiated by us: unlike JEI/EMI/Jade, it has
 * no annotation or {@code ServiceLoader} scan of its own, only an IMC handshake. This class is the
 * only caller of {@link TopCompat}, and only inside the {@link ModCompat#THE_ONE_PROBE} branch -
 * {@code TopCompat} is the one place in the mod that imports {@code mcjty.theoneprobe.api.*}, and a
 * class is only loaded (so its imports only need to resolve) the first time something actively uses
 * it. Skipping the call when TOP isn't installed means {@code TopCompat} is never loaded at all, so
 * its imports never need to exist on the runtime classpath.
 */
@Mod.EventBusSubscriber(modid = OttokeStreetFood.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModCompatEvents {
    private ModCompatEvents() {
    }

    @SubscribeEvent
    public static void onInterModEnqueue(InterModEnqueueEvent event) {
        if (ModCompat.THE_ONE_PROBE) {
            TopCompat.sendCapabilities();
        }
    }
}
