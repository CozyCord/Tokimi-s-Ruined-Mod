package net.cozystudios.tokimisruinedmod.event;

import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.cozystudios.tokimisruinedmod.config.GeneralConfig;
import net.cozystudios.tokimisruinedmod.entity.FootEntity;
import net.cozystudios.tokimisruinedmod.registry.ModEntities;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class FootSpawnHandler {
    private static int tickCounter = 0;
    private static final int SPAWN_INTERVAL = 400;
    private static final int MAX_FEET_PER_PLAYER = 3;
    private static final int SPAWN_HEIGHT_ABOVE = 40;

    public static void tick(ServerWorld world) {
        if (!GeneralConfig.get().spawning.footSpawningEnabled) return;

        if (!world.getRegistryKey().equals(World.OVERWORLD)) return;

        tickCounter++;
        if (tickCounter < SPAWN_INTERVAL) return;
        tickCounter = 0;

        Random random = world.getRandom();

        for (ServerPlayerEntity player : world.getPlayers()) {
            long feetNearby = world.getEntitiesByType(ModEntities.FOOT, player.getBoundingBox().expand(64), e -> true).size();
            if (feetNearby >= MAX_FEET_PER_PLAYER) continue;

            double angle = random.nextDouble() * Math.PI * 2;
            double distance = 16 + random.nextDouble() * 16;
            int x = (int) (player.getX() + Math.cos(angle) * distance);
            int z = (int) (player.getZ() + Math.sin(angle) * distance);

            int surfaceY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
            int spawnY = surfaceY + SPAWN_HEIGHT_ABOVE + random.nextInt(20);

            BlockPos spawnPos = new BlockPos(x, spawnY, z);

            FootEntity foot = new FootEntity(ModEntities.FOOT, world);
            foot.refreshPositionAndAngles(spawnPos, random.nextFloat() * 360, 0);
            foot.setPersistent();
            world.spawnEntity(foot);

            TokimisRuinedMod.LOGGER.info("Spawned foot at {} {} {}", x, spawnY, z);
        }
    }
}
