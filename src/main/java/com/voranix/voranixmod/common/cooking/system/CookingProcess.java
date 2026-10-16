package com.voranix.voranixmod.common.cooking.system;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;

/**
 * Tracks one in-progress cook: how far along it is and how long it takes in total. Replaces the raw
 * {@code cookingProgress}/{@code cookingTotalTime} fields that currently live directly on
 * FreidoraBlockEntity - extracting them behind this contract means a future station reuses the same
 * progress bookkeeping instead of re-deriving it per device.
 * <p>
 * <b>Synchronization:</b> extends vanilla's {@link ContainerData} so a CookingProcess can be handed
 * straight to {@code AbstractContainerMenu#addDataSlots(ContainerData)} - reusing Minecraft's
 * existing generic server-to-client int sync (the same mechanism furnaces, brewing stands, and
 * beacons use for their own progress bars) instead of inventing a parallel one. By convention,
 * index 0 is progress and index 1 is total time; a concrete implementation maps
 * {@code get(int)}/{@code set(int, int)} onto its own fields.
 * <p>
 * <b>Storage:</b> {@link #save()}/{@link #load(CompoundTag)} are this process's persistence contract
 * - which recipe was running and how far it got survive a chunk unload/reload. A station persists
 * this by nesting the returned tag under its own BlockEntity NBT, the same way FreidoraBlockEntity
 * persists its own fields today. A {@link CookingContainer}'s items are not this interface's
 * concern - that persistence is delegated to whatever inventory implementation backs the container
 * (e.g. Forge's {@code ItemStackHandler} already serializes itself).
 */
public interface CookingProcess extends ContainerData {
    boolean isActive();

    int getProgress();

    int getTotalTime();

    void start(CookingRecipe recipe);

    void advance();

    void reset();

    CompoundTag save();

    void load(CompoundTag tag);
}
