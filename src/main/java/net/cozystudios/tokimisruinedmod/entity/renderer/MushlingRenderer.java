package net.cozystudios.tokimisruinedmod.entity.renderer;

import net.cozystudios.tokimisruinedmod.entity.MushlingEntity;
import net.cozystudios.tokimisruinedmod.entity.model.MushlingModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class MushlingRenderer extends MobEntityRenderer<MushlingEntity, MushlingModel<MushlingEntity>> {

    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Identifier.of("tokimis_ruined_mod", "mushling"), "main");
    private static final Identifier TEXTURE = Identifier.of("tokimis_ruined_mod", "textures/entity/mushling.png");

    public MushlingRenderer(EntityRendererFactory.Context context) {
        super(context, new MushlingModel<>(context.getPart(MODEL_LAYER)), 0.4f);
        this.addFeature(new MushlingEyesFeature(this));
    }

    @Override
    public Identifier getTexture(MushlingEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(MushlingEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        int phase = entity.getCurrentPhase();

        float baseScale = 0.69f;
        float sizeMultiplier = switch (phase) {
            case 2 -> 1.1f;
            case 3 -> 1.25f;
            case 4 -> 1.5f;
            case 5 -> 2.0f;
            case 6 -> 3.0f;
            default -> 1.0f;
        };
        matrices.scale(baseScale * sizeMultiplier, baseScale * sizeMultiplier, baseScale * sizeMultiplier);

        if (phase >= 1) {
            float shakeIntensity = switch (phase) {
                case 1 -> 0.015f;
                case 2 -> 0.03f;
                case 3 -> 0.05f;
                case 4 -> 0.08f;
                case 5 -> 0.15f;
                case 6 -> 0.35f;
                default -> 0f;
            };
            float shakeSpeed = switch (phase) {
                case 1 -> 8.0f;
                case 2 -> 12.0f;
                case 3 -> 16.0f;
                case 4 -> 22.0f;
                case 5 -> 32.0f;
                case 6 -> 55.0f;
                default -> 5.0f;
            };
            float time = entity.age + tickDelta;
            matrices.translate(
                    MathHelper.sin(time * shakeSpeed) * shakeIntensity,
                    MathHelper.sin(time * shakeSpeed * 1.3f) * shakeIntensity * 0.3f,
                    MathHelper.cos(time * shakeSpeed * 0.9f) * shakeIntensity
            );
        }

        int savedHurtTime = entity.hurtTime;
        if (phase >= 6) {
            entity.hurtTime = 1;
        } else if (phase >= 1) {
            float time = entity.age + tickDelta;
            float freq = phase * 4.0f;
            float threshold = switch (phase) {
                case 1 -> 0.85f;
                case 2 -> 0.65f;
                case 3 -> 0.40f;
                case 4 -> 0.15f;
                case 5 -> -0.50f;
                default -> 1.0f;
            };
            entity.hurtTime = MathHelper.sin(time * freq) > threshold ? 1 : 0;
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        entity.hurtTime = savedHurtTime;

        matrices.pop();
    }
}
