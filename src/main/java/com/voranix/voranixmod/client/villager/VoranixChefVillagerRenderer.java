package com.voranix.voranixmod.client.villager;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Replaces vanilla's registered renderer for {@link net.minecraft.world.entity.EntityType#VILLAGER} so
 * every module's custom-modeled chef ({@link ChefVillagerRenderRegistry}) can use its own geometry
 * while every other villager profession keeps rendering exactly as vanilla does.
 * <p>
 * The naive way to reskin one profession is to keep vanilla's {@code VillagerRenderer} and just swap
 * its protected {@code model} field per-entity - but that field's declared type is pinned to
 * {@code VillagerModel<Villager>} by the class hierarchy, so only a real {@code VillagerModel} instance
 * can go there. Even if a custom chef model were rebuilt as a {@code VillagerModel} subtype (matching
 * vanilla's bone names so the swap compiles), vanilla's {@code VillagerRenderer} constructor unconditionally
 * adds a {@code VillagerProfessionLayer}. That layer re-renders the parent model two to three more times
 * per frame using vanilla's own UV layout (villager type skin, profession clothing, profession-level badge)
 * - and there is no supported way for a subclass to remove one specific layer, since
 * {@code LivingEntityRenderer} keeps its layer list private with only an add method. Feeding it a custom
 * chef's own UV-mapped geometry would make it sample those unrelated vanilla textures at the wrong
 * coordinates every frame.
 * <p>
 * Bypassing that entirely - not calling {@code super.render()} for a registered chef profession and instead
 * reimplementing the small part of {@code LivingEntityRenderer.render()} an NPC actually needs (limb swing,
 * head tracking, the model's own single texture, the name tag) - sidesteps both problems and leaves every
 * other villager profession completely untouched via the {@code super.render()} fallback below.
 * <p>
 * This class used to be Chef Ottoke-specific ({@code OttokeVillagerRenderer}, hardcoded to one profession
 * and one model). It moved to {@code voranixmod} core and reads {@link ChefVillagerRenderRegistry} instead
 * once a second module (Mai Pizza) needed the exact same bypass for its own chef - only one renderer can
 * ever be bound to {@code EntityType.VILLAGER}, so this dispatch has to live in one shared place rather than
 * duplicating it per module.
 */
public class VoranixChefVillagerRenderer extends VillagerRenderer {
    private static final float FEET_OFFSET = -1.501F;

    private final Map<VillagerProfession, ChefVillagerRenderDefinition> definitionsByProfession = new LinkedHashMap<>();
    private final Map<VillagerProfession, EntityModel<Villager>> bakedModels = new LinkedHashMap<>();

    public VoranixChefVillagerRenderer(EntityRendererProvider.Context context) {
        super(context);
        for (ChefVillagerRenderDefinition definition : ChefVillagerRenderRegistry.all()) {
            VillagerProfession profession = definition.profession().get();
            definitionsByProfession.put(profession, definition);
            bakedModels.put(profession, definition.modelFactory().apply(context.bakeLayer(definition.modelLayer())));
        }
    }

    @Override
    public void render(Villager entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        VillagerProfession profession = entity.getVillagerData().getProfession();
        ChefVillagerRenderDefinition definition = definitionsByProfession.get(profession);
        if (definition == null) {
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
            return;
        }
        renderCustomChef(entity, definition, bakedModels.get(profession), partialTicks, poseStack, buffer, packedLight);
    }

    private void renderCustomChef(Villager entity, ChefVillagerRenderDefinition definition, EntityModel<Villager> model,
                                   float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float bodyYaw = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        float headYaw = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
        float netHeadYaw = headYaw - bodyYaw;
        float headPitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        float ageInTicks = entity.tickCount + partialTicks;

        this.setupRotations(entity, poseStack, ageInTicks, bodyYaw, partialTicks);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(entity, poseStack, partialTicks);
        poseStack.translate(0.0F, FEET_OFFSET, 0.0F);

        float limbSwingAmount = entity.walkAnimation.speed(partialTicks);
        float limbSwing = entity.walkAnimation.position(partialTicks);
        if (limbSwingAmount > 1.0F) {
            limbSwingAmount = 1.0F;
        }

        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(definition.texture()));
        int overlay = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();

        if (this.shouldShowName(entity)) {
            this.renderNameTag(entity, entity.getDisplayName(), poseStack, buffer, packedLight);
        }
    }
}
