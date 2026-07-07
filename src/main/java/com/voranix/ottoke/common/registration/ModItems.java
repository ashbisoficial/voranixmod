package com.voranix.ottoke.common.registration;

import com.voranix.ottoke.OttokeStreetFood;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, OttokeStreetFood.MODID);

    public static final RegistryObject<Item> BANDERILLA_CRUDA = ITEMS.register("banderilla_cruda",
            () -> new Item(FoodRegistry.nonEdible()));

    public static final RegistryObject<Item> BANDERILLA_CLASICA = ITEMS.register("banderilla_clasica",
            () -> new Item(FoodRegistry.builder()
                    .nutrition(8)
                    .saturation(0.8f)
                    .build()));

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
