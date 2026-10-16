package com.voranix.ottoke.common.villager;

import com.google.common.collect.ImmutableSet;
import com.voranix.voranixmod.VoranixMod;
import com.voranix.ottoke.common.registration.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class OttokeChefProfession {
    public static final ResourceLocation ID = new ResourceLocation(VoranixMod.MODID, "chef_ottoke");

    public static final RegistryObject<PoiType> POI = ModVillagerRegistries.POI_TYPES.register("chef_ottoke",
            () -> new PoiType(ImmutableSet.of(ModBlocks.FREIDORA.get().defaultBlockState()), 1, 1));

    /**
     * The two predicates below are NOT the same thing, even though it's easy to assume so:
     * - heldWorkstation (2nd arg) validates a workstation a villager ALREADY has - keeps a
     *   deliberately-spawned Chef Ottoke's Freidora recognized as its job site.
     * - acquirableWorkstation (3rd arg) is what vanilla's unemployed-villager job search checks
     *   before letting ANY jobless villager claim a free POI. Returning {@code false} here is what
     *   stops a random villager from becoming a Chef Ottoke just by standing next to a Freidora -
     *   this profession is meant to only exist via a deliberate spawn (spawn egg / command), never
     *   through natural job assignment. See OttokeChefSpawner for the controlled-spawn path.
     */
    public static final RegistryObject<VillagerProfession> PROFESSION = ModVillagerRegistries.VILLAGER_PROFESSIONS.register("chef_ottoke",
            () -> new VillagerProfession("chef_ottoke", poi -> poi.get() == POI.get(), poi -> false, ImmutableSet.of(), ImmutableSet.of(), null));

    public static void bootstrap() {
    }
}
