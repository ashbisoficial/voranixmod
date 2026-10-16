package com.voranix.ottoke.common.registration;

import com.voranix.voranixmod.VoranixMod;
import com.voranix.ottoke.common.cooking.FreidoraMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, VoranixMod.MODID);

    public static final RegistryObject<MenuType<FreidoraMenu>> FREIDORA = MENU_TYPES.register("freidora",
            () -> IForgeMenuType.create((containerId, inventory, data) ->
                    new FreidoraMenu(containerId, inventory, data.readBlockPos(), inventory.player.level())));

    private ModMenuTypes() {
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
