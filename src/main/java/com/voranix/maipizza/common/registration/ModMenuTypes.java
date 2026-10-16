package com.voranix.maipizza.common.registration;

import com.voranix.maipizza.common.cooking.HornoDeBarroMenu;
import com.voranix.voranixmod.VoranixMod;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** Mirrors {@code com.voranix.ottoke.common.registration.ModMenuTypes}. */
public final class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, VoranixMod.MODID);

    public static final RegistryObject<MenuType<HornoDeBarroMenu>> HORNO_DE_BARRO = MENU_TYPES.register("horno_de_barro",
            () -> IForgeMenuType.create((containerId, inventory, data) ->
                    new HornoDeBarroMenu(containerId, inventory, data.readBlockPos(), inventory.player.level())));

    private ModMenuTypes() {
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
