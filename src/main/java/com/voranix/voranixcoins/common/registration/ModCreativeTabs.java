package com.voranix.voranixcoins.common.registration;

import com.voranix.voranixmod.VoranixMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VoranixMod.MODID);

    public static final RegistryObject<CreativeModeTab> VORANIX_COINS_TAB = TABS.register("voranixcoins_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.voranixcoins"))
                    .icon(() -> new ItemStack(ModItems.COIN_GOLD.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.COIN_ESTANO.get());
                        output.accept(ModItems.COIN_SILVER.get());
                        output.accept(ModItems.COIN_GOLD.get());
                        output.accept(ModItems.COIN_MITHRIL.get());
                        output.accept(ModItems.COIN_ORICALCO.get());
                        output.accept(ModItems.COIN_AJNA.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
