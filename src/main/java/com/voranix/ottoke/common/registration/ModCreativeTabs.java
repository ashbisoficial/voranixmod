package com.voranix.ottoke.common.registration;

import com.voranix.voranixmod.VoranixMod;
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
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VoranixMod.MODID);

    public static final RegistryObject<CreativeModeTab> OTTOKE_TAB = TABS.register("ottoke_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ottoke"))
                    .icon(() -> new ItemStack(ModItems.BANDERILLA_CRUDA.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.FREIDORA.get());
                        // CAJA_OTTOKE, MOSTRADOR, CAJA_REGISTRADORA, BASURERO deliberately omitted -
                        // see the comment next to them in ModBlocks for why.
                        output.accept(ModBlocks.MENU.get());
                        output.accept(ModBlocks.CARTEL.get());
                        output.accept(ModBlocks.BANNER_OTTOKE.get());
                        output.accept(ModBlocks.CARTEL_LUMINOSO.get());
                        output.accept(ModItems.BANDERILLA_CRUDA.get());
                        output.accept(ModItems.BANDERILLA_CLASICA.get());
                        output.accept(ModItems.BANDERILLA_PAPA_CRUDA.get());
                        output.accept(ModItems.BANDERILLA_PAPA.get());
                        output.accept(ModItems.BANDERILLA_PICANTE_CRUDA.get());
                        output.accept(ModItems.BANDERILLA_PICANTE.get());
                        output.accept(ModItems.BANDERILLA_MOZZARELLA_CRUDA.get());
                        output.accept(ModItems.BANDERILLA_MOZZARELLA.get());
                        output.accept(ModItems.BANDERILLA_PREMIUM_CRUDA.get());
                        output.accept(ModItems.BANDERILLA_PREMIUM.get());
                        output.accept(ModItems.COLA_OTTOKE.get());
                        output.accept(ModItems.TE_DURAZNO.get());
                        output.accept(ModItems.LIMON_OTTOKE.get());
                        output.accept(ModItems.NARANJA_OTTOKE.get());
                        output.accept(ModItems.GINGER_OTTOKE.get());
                        output.accept(ModIngredients.HARINA.get());
                        output.accept(ModIngredients.SAL.get());
                        output.accept(ModIngredients.AZUCAR.get());
                        output.accept(ModIngredients.ACEITE.get());
                        output.accept(ModIngredients.LECHE.get());
                        output.accept(ModIngredients.PALO.get());
                        output.accept(ModIngredients.SALCHICHA.get());
                        output.accept(ModIngredients.MASA.get());
                        output.accept(ModIngredients.EMPANIZADO.get());
                        output.accept(ModIngredients.QUESO.get());
                        output.accept(ModItems.HUEVO_OTTOKE.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
