package net.cozystudios.tokimisruinedmod.entity.model;

import net.cozystudios.tokimisruinedmod.entity.FootEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class FootEntityModel extends EntityModel<FootEntity> {
    private final ModelPart foot;
    private final ModelPart mainfoot;
    private final ModelPart heel;
    private final ModelPart pointertoe;
    private final ModelPart middletoe;
    private final ModelPart ringtoe;
    private final ModelPart pinkytoe;

    public FootEntityModel(ModelPart root) {
        this.foot = root.getChild("foot");
        this.mainfoot = this.foot.getChild("mainfoot");
        this.heel = this.foot.getChild("heel");
        this.pointertoe = this.foot.getChild("pointertoe");
        this.middletoe = this.foot.getChild("middletoe");
        this.ringtoe = this.foot.getChild("ringtoe");
        this.pinkytoe = this.foot.getChild("pinkytoe");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData foot = modelPartData.addChild("foot", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 24.0F, -1.0F));

        foot.addChild("mainfoot", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -2.1F, -1.0F, 5.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        foot.addChild("heel", ModelPartBuilder.create().uv(0, 9).cuboid(-1.5F, -2.0F, -1.0F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, 0.0F, 7.0F));

        ModelPartData pointertoe = foot.addChild("pointertoe", ModelPartBuilder.create(), ModelTransform.pivot(0.5F, 0.0F, -1.8F));
        pointertoe.addChild("pointertoe_r1", ModelPartBuilder.create().uv(14, 9).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.1658F, 0.0F));

        foot.addChild("secondtoe", ModelPartBuilder.create().uv(8, 9).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.8F, 0.0F, -1.7F));

        foot.addChild("middletoe", ModelPartBuilder.create().uv(0, 12).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.1F, 0.0F, -1.8F));

        foot.addChild("ringtoe", ModelPartBuilder.create().uv(6, 13).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.4F, 0.0F, -1.4F));

        ModelPartData pinkytoe = foot.addChild("pinkytoe", ModelPartBuilder.create(), ModelTransform.pivot(-4.6F, 0.0F, -1.2F));
        pinkytoe.addChild("pinkytoe_r1", ModelPartBuilder.create().uv(12, 13).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.1571F, 0.0F));

        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void setAngles(FootEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        mainfoot.pitch = 0;
        heel.pitch = 0;
        pointertoe.pitch = 0;
        middletoe.pitch = 0;
        ringtoe.pitch = 0;
        pinkytoe.pitch = 0;

        float walkCycle = limbSwing * 0.8F;
        float walkAmount = limbSwingAmount;

        float mainCycle = MathHelper.sin(walkCycle) * walkAmount;

        float rockAngle = mainCycle * (-12.5F * MathHelper.RADIANS_PER_DEGREE);
        mainfoot.pitch = rockAngle;

        float toeCycle = MathHelper.sin(walkCycle) * walkAmount;
        pointertoe.pitch = toeCycle * (-30.0F * MathHelper.RADIANS_PER_DEGREE);
        middletoe.pitch = toeCycle * (-40.0F * MathHelper.RADIANS_PER_DEGREE);
        ringtoe.pitch = toeCycle * (12.5F * MathHelper.RADIANS_PER_DEGREE);
        pinkytoe.pitch = toeCycle * (-17.5F * MathHelper.RADIANS_PER_DEGREE);

        foot.roll = MathHelper.cos(limbSwing * 0.6662F) * 0.15F * limbSwingAmount;
        float stompCycle = Math.abs(MathHelper.sin(limbSwing * 0.6662F));
        foot.pivotY = 24.0F - stompCycle * 1.5F * limbSwingAmount;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        foot.render(matrices, vertexConsumer, light, overlay, color);
    }
}
