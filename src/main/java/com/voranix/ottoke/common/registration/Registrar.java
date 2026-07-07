package com.voranix.ottoke.common.registration;

import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Contract every Mod*-registration class exposes: bind its DeferredRegister(s) to the mod event bus.
 * Used as a method-reference type by {@link ModRegistries} so the orchestrator depends on this
 * abstraction instead of listing nine unrelated concrete classes by hand.
 */
@FunctionalInterface
public interface Registrar {
    void register(IEventBus modEventBus);
}
