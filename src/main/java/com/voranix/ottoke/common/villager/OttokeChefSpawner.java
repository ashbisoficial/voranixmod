package com.voranix.ottoke.common.villager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;

import java.util.function.Predicate;

/**
 * The only intended entry point for creating a Chef Ottoke. Unlike a regular villager, this NPC
 * never acquires its profession through the vanilla POI job search (see the acquirableWorkstation
 * predicate in {@link OttokeChefProfession#PROFESSION}), so something has to assign the profession
 * directly at spawn time instead. A future dedicated Spawn Egg item (or a command) should call
 * {@link #spawn(ServerLevel, BlockPos)} rather than spawning a plain villager and hoping it finds
 * a Freidora on its own.
 * <p>
 * Two things beyond {@code setProfession} are required to make that profession stick, both handled
 * here:
 * <p>
 * 1) Vanilla's {@code ResetProfession} core AI behavior (always active, every tick) reverts a
 * villager's profession back to {@code NONE} whenever it has no claimed {@code JOB_SITE} memory
 * <em>and</em> its trading XP is still 0 <em>and</em> its level is still 1 - exactly the state of a
 * villager the instant after this method assigns it a profession. Because
 * {@code acquirableWorkstation} always returns {@code false} for this profession (by design - see
 * {@link OttokeChefProfession}), the normal vanilla path that would let a villager claim a
 * {@code JOB_SITE} on its own never runs for a Chef Ottoke either, so without an explicit fix that
 * demotion fires within seconds of spawning, with or without a Freidora nearby. Giving the villager
 * a nonzero XP here permanently defeats the "level 1 and XP 0" half of that condition, independent
 * of whether a job site was ever claimed - the fix that actually stops the demotion.
 * <p>
 * 2) {@link #claimNearbyJobSite} opportunistically claims a real Freidora as this villager's
 * {@code JOB_SITE} if one is within range, the same way vanilla's own {@code AcquirePoi} behavior
 * would for an organically-employed villager. This isn't what stops the demotion (the XP bump
 * above already does that unconditionally) - it's what lets the normal "walk to work site" /
 * restocking AI function if a Freidora happens to be nearby. If none is found, the chef still keeps
 * its profession (per point 1) - it just won't path to a workstation until one gets built and it
 * (or another Chef Ottoke) claims it some other way.
 */
public final class OttokeChefSpawner {
    private static final int STARTING_LEVEL = 1;
    private static final int JOB_SITE_SEARCH_RADIUS = 48;

    private OttokeChefSpawner() {
    }

    public static Villager spawn(ServerLevel level, BlockPos pos) {
        Villager chef = new Villager(EntityType.VILLAGER, level, VillagerType.byBiome(level.getBiome(pos)));
        chef.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        chef.setVillagerData(chef.getVillagerData()
                .setProfession(OttokeChefProfession.PROFESSION.get())
                .setLevel(STARTING_LEVEL));
        chef.setVillagerXp(1);
        claimNearbyJobSite(level, pos, chef);

        level.addFreshEntity(chef);
        return chef;
    }

    private static void claimNearbyJobSite(ServerLevel level, BlockPos pos, Villager chef) {
        Predicate<Holder<PoiType>> jobSite = OttokeChefProfession.PROFESSION.get().heldJobSite();
        level.getPoiManager().take(jobSite, (type, poiPos) -> true, pos, JOB_SITE_SEARCH_RADIUS)
                .ifPresent(poiPos -> chef.getBrain().setMemory(MemoryModuleType.JOB_SITE, GlobalPos.of(level.dimension(), poiPos)));
    }
}
