package net.cozystudios.tokimisruinedmod;

import net.cozystudios.tokimisruinedmod.client.PoopOverlayRenderer;
import net.cozystudios.tokimisruinedmod.network.PoopOverlayPayload;
import net.cozystudios.tokimisruinedmod.entity.model.FootEntityModel;
import net.cozystudios.tokimisruinedmod.entity.model.MushlingModel;
import net.cozystudios.tokimisruinedmod.entity.renderer.FootEntityRenderer;
import net.cozystudios.tokimisruinedmod.entity.renderer.MushlingRenderer;
import net.cozystudios.tokimisruinedmod.particle.FlyParticle;
import net.cozystudios.tokimisruinedmod.registry.ModBlocks;
import net.cozystudios.tokimisruinedmod.registry.ModEntities;
import net.cozystudios.tokimisruinedmod.registry.ModItems;
import net.cozystudios.tokimisruinedmod.registry.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class TokimisRuinedModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TokimisRuinedMod.LOGGER.info("Tokimi's Ruined Mod Client Initialized");

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> -1, ModItems.MUSHLING_SPAWN_EGG);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POOP_JAR, RenderLayer.getCutout());

        EntityModelLayerRegistry.registerModelLayer(FootEntityRenderer.MODEL_LAYER, FootEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MushlingRenderer.MODEL_LAYER, MushlingModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntities.THROWN_POOP, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.THROWN_GOLDEN_POOP, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FOOT, FootEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.MUSHLING, MushlingRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.FLY, FlyParticle.Factory::new);

        ClientPlayNetworking.registerGlobalReceiver(PoopOverlayPayload.ID, (payload, context) -> {
            PoopOverlayRenderer.activate();
        });

        HudRenderCallback.EVENT.register(PoopOverlayRenderer::render);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PoopOverlayRenderer.tick();

            if (client.player != null && client.player.isUsingItem()) {
                if (client.player.getActiveItem().isOf(ModItems.POOP_ITEM)) {
                    int maxTime = client.player.getActiveItem().getMaxUseTime(client.player);
                    int remaining = client.player.getItemUseTimeLeft();
                    float progress = (float) (maxTime - remaining) / maxTime;
                    PoopOverlayRenderer.setEatingTarget(progress);
                }
            }
        });
    }
}
