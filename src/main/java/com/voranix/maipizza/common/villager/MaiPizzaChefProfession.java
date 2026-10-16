package com.voranix.maipizza.common.villager;

import com.google.common.collect.ImmutableSet;
import com.voranix.maipizza.common.registration.ModBlocks;
import com.voranix.voranixmod.VoranixMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/**
 * Mirrors {@code com.voranix.ottoke.common.villager.OttokeChefProfession} exactly - see that class
 * for the full rationale behind {@code heldWorkstation} vs {@code acquirableWorkstation} and why
 * this profession is deliberately unreachable through vanilla's job-search path.
 */
public class MaiPizzaChefProfession {
    public static final ResourceLocation ID = new ResourceLocation(VoranixMod.MODID, "chef_maipizza");

    public static final RegistryObject<PoiType> POI = ModVillagerRegistries.POI_TYPES.register("chef_maipizza",
            () -> new PoiType(ImmutableSet.of(ModBlocks.HORNO_DE_BARRO.get().defaultBlockState()), 1, 1));

    public static final RegistryObject<VillagerProfession> PROFESSION = ModVillagerRegistries.VILLAGER_PROFESSIONS.register("chef_maipizza",
            () -> new VillagerProfession("chef_maipizza", poi -> poi.get() == POI.get(), poi -> false, ImmutableSet.of(), ImmutableSet.of(), null));

    public static void bootstrap() {
    }
}
