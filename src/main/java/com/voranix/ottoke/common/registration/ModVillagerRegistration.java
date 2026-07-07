package com.voranix.ottoke.common.registration;

import com.voranix.ottoke.common.villager.ModVillagerRegistries;
import com.voranix.ottoke.common.villager.OttokeChefProfession;
import com.voranix.ottoke.common.villager.OttokeChefTrades;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ModVillagerRegistration {
    private ModVillagerRegistration() {
    }

    public static void register(IEventBus eventBus) {
        ModVillagerRegistries.register(eventBus);
        OttokeChefProfession.bootstrap();
        OttokeChefTrades.register();
    }
}
