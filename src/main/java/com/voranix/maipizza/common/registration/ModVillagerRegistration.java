package com.voranix.maipizza.common.registration;

import com.voranix.maipizza.common.villager.MaiPizzaChefProfession;
import com.voranix.maipizza.common.villager.MaiPizzaChefTrades;
import com.voranix.maipizza.common.villager.ModVillagerRegistries;
import net.minecraftforge.eventbus.api.IEventBus;

/** Mirrors {@code com.voranix.ottoke.common.registration.ModVillagerRegistration}. */
public final class ModVillagerRegistration {
    private ModVillagerRegistration() {
    }

    public static void register(IEventBus eventBus) {
        ModVillagerRegistries.register(eventBus);
        MaiPizzaChefProfession.bootstrap();
        MaiPizzaChefTrades.register();
    }
}
