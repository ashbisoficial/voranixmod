package com.voranix.ottoke.common.villager;

import com.google.common.collect.ImmutableSet;
import com.voranix.ottoke.OttokeStreetFood;
import com.voranix.ottoke.common.registration.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class OttokeChefProfession {
    public static final ResourceLocation ID = new ResourceLocation(OttokeStreetFood.MODID, "chef_ottoke");

    public static final RegistryObject<PoiType> POI = ModVillagerRegistries.POI_TYPES.register("chef_ottoke",
            () -> new PoiType(ImmutableSet.of(ModBlocks.FREIDORA.get().defaultBlockState()), 1, 1));

    public static final RegistryObject<VillagerProfession> PROFESSION = ModVillagerRegistries.VILLAGER_PROFESSIONS.register("chef_ottoke",
            () -> new VillagerProfession("chef_ottoke", poi -> poi.get() == POI.get(), poi -> poi.get() == POI.get(), ImmutableSet.of(), ImmutableSet.of(), null));

    public static void bootstrap() {
    }
}
