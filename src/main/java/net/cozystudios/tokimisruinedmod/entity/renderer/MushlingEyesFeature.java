package net.cozystudios.tokimisruinedmod.entity.renderer;

import net.cozystudios.tokimisruinedmod.entity.MushlingEntity;
import net.cozystudios.tokimisruinedmod.entity.model.MushlingModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class MushlingEyesFeature extends FeatureRenderer<MushlingEntity, MushlingModel<MushlingEntity>> {

    private static final Identifier EYES_TEXTURE = Identifier.of("tokimis_ruined_mod", "textures/entity/mushling_e.png");
    private static final RenderLayer EYES_LAYER = RenderLayer.getEntityCutoutNoCullZOffset(EYES_TEXTURE);

    public MushlingEyesFeature(FeatureRendererContext<MushlingEntity, MushlingModel<MushlingEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, MushlingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(EYES_LAYER);
        this.getContextModel().render(matrices, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV);
    }
}
