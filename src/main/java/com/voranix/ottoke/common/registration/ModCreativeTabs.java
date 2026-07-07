package com.voranix.ottoke.common.registration;

import com.voranix.ottoke.OttokeStreetFood;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.RegisterEvent;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OttokeStreetFood.MODID);

    public static final RegistryObject<CreativeModeTab> OTTOKE_TAB = TABS.register("ottoke_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ottoke"))
                    .icon(() -> new ItemStack(ModItems.BANDERILLA_CRUDA.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.BANDERILLA_CRUDA.get());
                        output.accept(ModItems.BANDERILLA_CLASICA.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
