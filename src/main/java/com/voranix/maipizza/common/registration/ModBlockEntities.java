package com.voranix.maipizza.common.registration;

import com.voranix.maipizza.common.cooking.HornoDeBarroBlockEntity;
import com.voranix.voranixmod.VoranixMod;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** Mirrors {@code com.voranix.ottoke.common.registration.ModBlockEntities}. */
public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, VoranixMod.MODID);

    public static final RegistryObject<BlockEntityType<HornoDeBarroBlockEntity>> HORNO_DE_BARRO =
            BLOCK_ENTITIES.register("horno_de_barro", () -> BlockEntityType.Builder.of(
                    HornoDeBarroBlockEntity::new,
                    ModBlocks.HORNO_DE_BARRO.get()
            ).build(null));

    private ModBlockEntities() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
