package com.voranix.ottoke.common.events;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.voranix.voranixmod.VoranixMod;

@Mod.EventBusSubscriber(modid = VoranixMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ServerTickEvents {
    private ServerTickEvents() {
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EventManager.dispatch(event.getServer().getTickCount());
        }
    }
}
