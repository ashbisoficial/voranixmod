package com.voranix.ottoke.common.registration;

import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.cooking.FreidoraBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OttokeStreetFood.MODID);

    public static final RegistryObject<BlockEntityType<FreidoraBlockEntity>> FREIDORA =
            BLOCK_ENTITIES.register("freidora", () -> BlockEntityType.Builder.of(
                    FreidoraBlockEntity::new,
                    ModBlocks.FREIDORA.get()
            ).build(null));

    private ModBlockEntities() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
