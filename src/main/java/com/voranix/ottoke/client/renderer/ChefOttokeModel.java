package com.voranix.ottoke.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.Villager;

/**
 * Full replacement geometry for Chef Ottoke, converted 1:1 from {@code src/img/aldeano_ottoke.bbmodel}
 * (Blockbench "Modded Entity" export, 64x64 UV). Every bone below is a direct port of that file's
 * {@code elements}/{@code groups}: same pivots, same box origins/sizes, same {@code uv_offset} used as
 * {@link CubeListBuilder#texOffs}.
 * <p>
 * Blockbench models this NPC Y-up with feet at Y=0 (the file's own {@code modded_entity_flip_y: true}
 * flag says as much); Minecraft's {@link ModelPart} space is Y-down, rooted near the neck. Every pivot
 * and box origin here was converted with {@code forgeY = 24 - blockbenchY} - the same formula this
 * package's old {@code OttokeChefHatModel} derived and validated for the {@code headwear2} piece (kept
 * below as the {@code headwear} bone). All-axis rotation signs are flipped for the same reason documented
 * there: Blockbench and {@link PartPose#offsetAndRotation} rotate a box in opposite directions for the
 * same stated angle.
 * <p>
 * The bbmodel has a second, pixel-identical copy of the {@code body} cube (named {@code body2} - same
 * from/to, same UV). Blockbench keeps it only because the artist duplicated the cube while painting a
 * second texture layer in-app; rendering it too would just re-draw the exact same box, wasting a draw
 * call with zero visual difference (not even a legitimate clothing-overlay inflate, unlike
 * {@code bodywear}). It's intentionally omitted here.
 * <p>
 * This is a plain {@link EntityModel}, not a {@link net.minecraft.client.model.VillagerModel}, because
 * it is never handed to vanilla's {@code VillagerRenderer} machinery - see
 * {@link OttokeVillagerRenderer} for why that renderer bypasses vanilla's villager render path entirely
 * for this profession instead of swapping {@code VillagerRenderer}'s model field.
 */
public class ChefOttokeModel extends EntityModel<Villager> {
    private static final float DEG_TO_RAD = (float) (Math.PI / 180.0);

    private final ModelPart head;
    private final ModelPart nose;
    private final ModelPart headwear;
    private final ModelPart body;
    private final ModelPart bodywear;
    private final ModelPart arms;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public ChefOttokeModel(ModelPart root) {
        this.head = root.getChild("head");
        this.nose = root.getChild("nose");
        this.headwear = root.getChild("headwear");
        this.body = root.getChild("body");
        this.bodywear = root.getChild("bodywear");
        this.arms = root.getChild("arms");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F),
                PartPose.ZERO);

        root.addOrReplaceChild("nose",
                CubeListBuilder.create().texOffs(24, 0)
                        .addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(0.0F, -2.0F, 0.0F));

        root.addOrReplaceChild("headwear",
                CubeListBuilder.create().texOffs(35, 46)
                        .addBox(-5.0F, -5.0F, -10.0F, 10.0F, 10.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) (-Math.PI / 2.0), 0.0F, 0.0F));

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(16, 20)
                        .addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F),
                PartPose.ZERO);

        root.addOrReplaceChild("bodywear",
                CubeListBuilder.create().texOffs(0, 38)
                        .addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new CubeDeformation(0.5F)),
                PartPose.ZERO);

        root.addOrReplaceChild("arms",
                CubeListBuilder.create()
                        .texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F)
                        .texOffs(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F)
                        .texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, true),
                PartPose.offsetAndRotation(0.0F, 2.95F, -1.05F, (float) (-43.0 * Math.PI / 180.0), 0.0F, 0.0F));

        root.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(0, 22)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(2.0F, 12.0F, 0.0F));

        root.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(0, 22)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-2.0F, 12.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(Villager entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * DEG_TO_RAD;
        this.head.xRot = headPitch * DEG_TO_RAD;
        this.nose.yRot = this.head.yRot;
        this.nose.xRot = this.head.xRot;

        this.headwear.resetPose();
        this.headwear.xRot += headPitch * DEG_TO_RAD;
        this.headwear.yRot += netHeadYaw * DEG_TO_RAD;

        // Same amplitude vanilla's own VillagerModel uses for right_leg/left_leg - keeps Chef Ottoke's
        // gait speed-scaled with actual movement instead of looping a fixed-length walk clip regardless
        // of how fast (or whether) the villager is moving.
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.nose.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.headwear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.bodywear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.arms.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
