package net.cozystudios.tokimisruinedmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class PoopOverlayRenderer {
    private static final Identifier POOP_OVERLAY_TEXTURE = Identifier.of(TokimisRuinedMod.MOD_ID, "textures/misc/poop_overlay.png");

    private static float overlayAlpha = 0.0f;
    private static float targetAlpha = 0.0f;
    private static final float INITIAL_ALPHA = 1.0f;
    private static final float FADE_OUT_PER_TICK = INITIAL_ALPHA / 80.0f;
    private static final float FADE_IN_PER_TICK = INITIAL_ALPHA / 40.0f;

    public static void activate() {
        overlayAlpha = INITIAL_ALPHA;
        targetAlpha = INITIAL_ALPHA;
    }

    public static void setEatingTarget(float progress) {
        targetAlpha = Math.min(INITIAL_ALPHA, progress);
    }

    public static void clearEatingTarget() {
        targetAlpha = 0.0f;
    }

    public static void tick() {
        if (overlayAlpha < targetAlpha) {
            overlayAlpha = Math.min(targetAlpha, overlayAlpha + FADE_IN_PER_TICK);
        } else if (overlayAlpha > targetAlpha) {
            overlayAlpha -= FADE_OUT_PER_TICK;
            if (overlayAlpha < targetAlpha) overlayAlpha = targetAlpha;
            if (overlayAlpha < 0) overlayAlpha = 0;
        }
        if (targetAlpha > 0) {
            targetAlpha -= FADE_OUT_PER_TICK;
            if (targetAlpha < 0) targetAlpha = 0;
        }
    }

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (overlayAlpha <= 0) return;

        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, overlayAlpha);

        context.drawTexture(POOP_OVERLAY_TEXTURE, 0, 0, 0.0f, 0.0f, width, height, width, height);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}
