package net.cozystudios.tokimisruinedmod.client;

import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.cozystudios.tokimisruinedmod.registry.ModStatusEffects;
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

public class BleedingFeatureRenderer
        extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private static final Identifier TEXTURE = Identifier.of(TokimisRuinedMod.MOD_ID, "textures/entity/bleeding.png");
    private static final int COLOR = (255 << 24) | (255 << 16) | (255 << 8) | 255;

    public BleedingFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta,
                       float animationProgress, float headYaw, float headPitch) {
        if (!entity.hasStatusEffect(ModStatusEffects.BLEEDING)) return;

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                RenderLayer.getEntityTranslucent(TEXTURE));

        PlayerEntityModel<AbstractClientPlayerEntity> model = this.getContextModel();
        model.leftArm.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, COLOR);
        model.rightArm.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, COLOR);
        model.leftLeg.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, COLOR);
        model.rightLeg.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, COLOR);
        model.body.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, COLOR);
    }
}
