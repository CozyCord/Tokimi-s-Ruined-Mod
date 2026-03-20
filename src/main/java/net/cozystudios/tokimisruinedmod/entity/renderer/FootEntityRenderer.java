package net.cozystudios.tokimisruinedmod.entity.renderer;

import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.cozystudios.tokimisruinedmod.entity.FootEntity;
import net.cozystudios.tokimisruinedmod.entity.model.FootEntityModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class FootEntityRenderer extends MobEntityRenderer<FootEntity, FootEntityModel> {
    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(
            Identifier.of(TokimisRuinedMod.MOD_ID, "foot"), "main");

    private static final Identifier TEXTURE = Identifier.of(TokimisRuinedMod.MOD_ID, "textures/entity/foot.png");

    public FootEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FootEntityModel(context.getPart(MODEL_LAYER)), 0.3f);
    }

    @Override
    public Identifier getTexture(FootEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(FootEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        int fuseTimer = entity.getFuseTimer();
        if (fuseTimer >= 0) {
            float flash = MathHelper.abs(((float) fuseTimer - tickDelta) / (float) 10 % 2.0F - 1.0F);
            matrices.scale(1.0F + flash * 0.05F, 1.0F + flash * 0.05F, 1.0F + flash * 0.05F);
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
