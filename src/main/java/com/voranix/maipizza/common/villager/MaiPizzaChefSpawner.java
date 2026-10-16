package com.voranix.maipizza.common.villager;

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
 * Mirrors {@code com.voranix.ottoke.common.villager.OttokeChefSpawner} exactly - see that class for
 * the full rationale (the XP bump defeating vanilla's ResetProfession demotion, and the opportunistic
 * nearby-job-site claim).
 */
public final class MaiPizzaChefSpawner {
    private static final int STARTING_LEVEL = 1;
    private static final int JOB_SITE_SEARCH_RADIUS = 48;

    private MaiPizzaChefSpawner() {
    }

    public static Villager spawn(ServerLevel level, BlockPos pos) {
        Villager chef = new Villager(EntityType.VILLAGER, level, VillagerType.byBiome(level.getBiome(pos)));
        chef.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        chef.setVillagerData(chef.getVillagerData()
                .setProfession(MaiPizzaChefProfession.PROFESSION.get())
                .setLevel(STARTING_LEVEL));
        chef.setVillagerXp(1);
        claimNearbyJobSite(level, pos, chef);

        level.addFreshEntity(chef);
        return chef;
    }

    private static void claimNearbyJobSite(ServerLevel level, BlockPos pos, Villager chef) {
        Predicate<Holder<PoiType>> jobSite = MaiPizzaChefProfession.PROFESSION.get().heldJobSite();
        level.getPoiManager().take(jobSite, (type, poiPos) -> true, pos, JOB_SITE_SEARCH_RADIUS)
                .ifPresent(poiPos -> chef.getBrain().setMemory(MemoryModuleType.JOB_SITE, GlobalPos.of(level.dimension(), poiPos)));
    }
}
