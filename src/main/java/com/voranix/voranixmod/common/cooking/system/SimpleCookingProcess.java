package com.voranix.voranixmod.common.cooking.system;

import net.minecraft.nbt.CompoundTag;

/**
 * The only {@link CookingProcess} implementation the system needs so far: two ints (progress, total
 * time), synced via {@code ContainerData} and persisted via NBT exactly as the interface promises.
 * Nothing here is Freidora-specific - see {@link CookingProcess}'s own docs for why tracking
 * progress is meant to be identical for every station. A future station instantiates this directly,
 * the same way {@code FreidoraBlockEntity} does, instead of writing its own copy.
 */
public class SimpleCookingProcess implements CookingProcess {
    private static final String TAG_PROGRESS = "Progress";
    private static final String TAG_TOTAL_TIME = "TotalTime";

    private int progress;
    private int totalTime;

    @Override
    public boolean isActive() {
        return this.totalTime > 0;
    }

    @Override
    public int getProgress() {
        return this.progress;
    }

    @Override
    public int getTotalTime() {
        return this.totalTime;
    }

    @Override
    public void start(CookingRecipe recipe) {
        this.progress = 0;
        this.totalTime = recipe.getCookingTime();
    }

    @Override
    public void advance() {
        this.progress++;
    }

    @Override
    public void reset() {
        this.progress = 0;
        this.totalTime = 0;
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(TAG_PROGRESS, this.progress);
        tag.putInt(TAG_TOTAL_TIME, this.totalTime);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.progress = tag.getInt(TAG_PROGRESS);
        this.totalTime = tag.getInt(TAG_TOTAL_TIME);
    }

    @Override
    public int get(int index) {
        return switch (index) {
            case 0 -> this.progress;
            case 1 -> this.totalTime;
            default -> 0;
        };
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case 0 -> this.progress = value;
            case 1 -> this.totalTime = value;
            default -> {
            }
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
