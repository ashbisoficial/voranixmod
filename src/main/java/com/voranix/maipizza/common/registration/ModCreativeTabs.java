package com.voranix.maipizza.common.registration;

import com.voranix.voranixmod.VoranixMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/** Mirrors {@code com.voranix.ottoke.common.registration.ModCreativeTabs}. */
public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VoranixMod.MODID);

    public static final RegistryObject<CreativeModeTab> MAIPIZZA_TAB = TABS.register("maipizza_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.maipizza"))
                    .icon(() -> new ItemStack(ModBlocks.PIZZA_SUPREMA.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.HORNO_DE_BARRO.get());
                        output.accept(ModBlocks.MENU.get());
                        output.accept(ModBlocks.CARTEL.get());
                        output.accept(ModBlocks.CARTEL_LUMINOSO.get());
                        output.accept(ModItems.PIZZA_MARGARITA_CRUDA.get());
                        output.accept(ModBlocks.PIZZA_MARGARITA.get());
                        output.accept(ModItems.PIZZA_PEPPERONI_CRUDA.get());
                        output.accept(ModBlocks.PIZZA_PEPPERONI.get());
                        output.accept(ModItems.PIZZA_CHAMPINON_CRUDA.get());
                        output.accept(ModBlocks.PIZZA_CHAMPINON.get());
                        output.accept(ModItems.PIZZA_CUATRO_QUESOS_CRUDA.get());
                        output.accept(ModBlocks.PIZZA_CUATRO_QUESOS.get());
                        output.accept(ModItems.PIZZA_SUPREMA_CRUDA.get());
                        output.accept(ModBlocks.PIZZA_SUPREMA.get());
                        output.accept(ModItems.HUEVO_MAIPIZZA.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
