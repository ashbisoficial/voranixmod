package com.voranix.ottoke.common.registration;

import com.voranix.ottoke.OttokeStreetFood;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Registry skeleton for custom SoundEvents (frying, cash register, etc.). Intentionally empty:
 * no sound assets exist yet, and defining a SoundEvent before the .ogg/sounds.json exists would
 * register a broken entry. Add RegistryObject<SoundEvent> constants here once audio is authored.
 */
public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, OttokeStreetFood.MODID);

    private ModSounds() {
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
