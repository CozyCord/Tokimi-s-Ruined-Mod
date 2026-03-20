package net.cozystudios.tokimisruinedmod.client;

import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.cozystudios.tokimisruinedmod.registry.ModStatusEffects;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PoopyFeetFeatureRenderer
        extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private static final Identifier TEXTURE = Identifier.of(TokimisRuinedMod.MOD_ID, "textures/entity/poopy_feet.png");
    private static final int COLOR = (255 << 24) | (255 << 16) | (255 << 8) | 255;

    private final ModelPart leftFoot;
    private final ModelPart rightFoot;

    public PoopyFeetFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);

        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        root.addChild("right_foot", ModelPartBuilder.create()
                .uv(0, 0).cuboid(-2.0f, 8.0f, -2.0f, 4.0f, 4.0f, 4.0f,
                        new Dilation(0.26f)), ModelTransform.pivot(-1.9f, 12.0f, 0.0f));

        root.addChild("left_foot", ModelPartBuilder.create()
                .uv(0, 0).cuboid(-2.0f, 8.0f, -2.0f, 4.0f, 4.0f, 4.0f,
                        new Dilation(0.26f)), ModelTransform.pivot(1.9f, 12.0f, 0.0f));

        ModelPart rootPart = TexturedModelData.of(modelData, 16, 16).createModel();
        this.rightFoot = rootPart.getChild("right_foot");
        this.leftFoot = rootPart.getChild("left_foot");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta,
                       float animationProgress, float headYaw, float headPitch) {
        if (!entity.hasStatusEffect(ModStatusEffects.POOPY_FEET)) return;

        PlayerEntityModel<AbstractClientPlayerEntity> model = this.getContextModel();
        this.rightFoot.pitch = model.rightLeg.pitch;
        this.rightFoot.yaw = model.rightLeg.yaw;
        this.rightFoot.roll = model.rightLeg.roll;
        this.leftFoot.pitch = model.leftLeg.pitch;
        this.leftFoot.yaw = model.leftLeg.yaw;
        this.leftFoot.roll = model.leftLeg.roll;

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                RenderLayer.getEntityTranslucent(TEXTURE));

        this.rightFoot.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, COLOR);
        this.leftFoot.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, COLOR);
    }
}
