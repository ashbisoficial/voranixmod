package com.voranix.ottoke.common.cooking.system;

/**
 * The single entry point for running a cooking station's cook cycle. Mirrors how the rest of the
 * mod already names "where a subsystem starts" - {@code ModRegistries} for registration,
 * {@code ModDataGenerators} for datagen - with one small static class, instead of that knowledge
 * being implicit in whichever class happens to call the pieces first.
 * <p>
 * Contains nothing about the Freidora, or about {@code BlockEntity} at all - it only knows about
 * {@link CookingContext}. That is what makes this a real decoupling, not just an extra layer:
 * before it existed, "how do I run a cook" was answered by "extend AbstractCookingBlockEntity and
 * it happens in serverTick". Now it's answered by "implement CookingStation, build a
 * CookingContext, and call CookingSystem.tick" - a future non-block-entity integration (an entity,
 * a command, a test) can drive a station without extending that base class at all.
 * {@link AbstractCookingBlockEntity#serverTick} is this method's first caller, not a required one.
 * <p>
 * No cooking logic lives here - see {@link AbstractCookingManager#tick} for the actual state
 * machine. This class only pulls a context's collaborators apart and hands them to the station's
 * manager; that is the full extent of its responsibility, and it should stay that way.
 */
public final class CookingSystem {
    private CookingSystem() {
    }

    public static void tick(CookingContext context) {
        CookingStation station = context.station();
        station.getCookingManager().tick(station.getCookingContainer(), station.getCookingProcess(), context.level());
    }
}
