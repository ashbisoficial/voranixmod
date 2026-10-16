package com.voranix.voranixmod.client.villager;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

/**
 * One module's custom-modeled chef villager, as registered into {@link ChefVillagerRenderRegistry}.
 * {@code modelFactory} bakes the {@link ModelPart} produced from {@code modelLayer} into that module's
 * own {@link EntityModel} implementation (e.g. {@code ChefOttokeModel::new}).
 */
public record ChefVillagerRenderDefinition(
        RegistryObject<VillagerProfession> profession,
        ModelLayerLocation modelLayer,
        ResourceLocation texture,
        Function<ModelPart, EntityModel<Villager>> modelFactory) {
}
