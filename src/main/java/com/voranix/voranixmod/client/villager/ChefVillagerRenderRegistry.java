package com.voranix.voranixmod.client.villager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shared sign-up sheet for every module's custom-modeled chef villager, consumed by
 * {@link VoranixChefVillagerRenderer} - the single renderer registered for
 * {@link net.minecraft.world.entity.EntityType#VILLAGER}. A module registers here instead of
 * registering its own villager renderer because only one renderer can ever be bound to that entity
 * type; letting each module fight over that registration would mean the last module init'd silently
 * wins and every earlier custom chef renders as a plain vanilla villager again.
 * <p>
 * Each module must call {@link #register} from its own {@code EntityRenderersEvent.RegisterLayerDefinitions}
 * subscriber (not earlier), and {@link VoranixChefVillagerRenderer} must only read {@link #all()} from its
 * constructor, which runs while handling {@code EntityRenderersEvent.RegisterRenderers}. Forge fires every
 * subscriber of one event class to completion before posting the next event class, so by construction every
 * module's {@code RegisterLayerDefinitions} handler - and therefore every {@link #register} call - has already
 * run by the time {@code RegisterRenderers} fires, regardless of which module's subscriber class happens to
 * run first.
 */
public final class ChefVillagerRenderRegistry {
    private static final List<ChefVillagerRenderDefinition> DEFINITIONS = new ArrayList<>();

    public static void register(ChefVillagerRenderDefinition definition) {
        DEFINITIONS.add(definition);
    }

    public static List<ChefVillagerRenderDefinition> all() {
        return Collections.unmodifiableList(DEFINITIONS);
    }

    private ChefVillagerRenderRegistry() {
    }
}
