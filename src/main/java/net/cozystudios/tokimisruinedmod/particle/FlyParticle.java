package net.cozystudios.tokimisruinedmod.particle;

import net.cozystudios.tokimisruinedmod.registry.ModBlocks;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;

public class FlyParticle extends SpriteBillboardParticle {
    private final double centerX;
    private final double centerY;
    private final double centerZ;
    private final double orbitRadius;
    private double orbitAngle;
    private final double orbitSpeed;
    private final double bobSpeed;
    private double bobOffset;
    private final double bobHeight;
    private final boolean jarMode;

    protected FlyParticle(ClientWorld world, double x, double y, double z, boolean jarMode, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.setSpriteForAge(spriteProvider);
        this.jarMode = jarMode;

        this.centerX = x;
        this.centerY = y;
        this.centerZ = z;

        if (jarMode) {
            this.orbitRadius = 0.02 + random.nextDouble() * 0.03;
            this.orbitAngle = random.nextDouble() * Math.PI * 2;
            this.orbitSpeed = 0.15 + random.nextDouble() * 0.1;
            this.bobSpeed = 0.08 + random.nextDouble() * 0.06;
            this.bobHeight = 0.06;
            this.scale = 0.1f;
            this.maxAge = 60 + random.nextInt(40);
        } else {
            this.orbitRadius = 0.3 + random.nextDouble() * 0.4;
            this.orbitAngle = random.nextDouble() * Math.PI * 2;
            this.orbitSpeed = 0.08 + random.nextDouble() * 0.12;
            this.bobSpeed = 0.05 + random.nextDouble() * 0.05;
            this.bobHeight = 0.15;
            this.scale = 0.15f + random.nextFloat() * 0.05f;
            this.maxAge = 40 + random.nextInt(40);
        }

        this.bobOffset = random.nextDouble() * Math.PI * 2;
        this.collidesWithWorld = false;

        this.x = centerX + Math.cos(orbitAngle) * orbitRadius;
        this.z = centerZ + Math.sin(orbitAngle) * orbitRadius;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }

        if (jarMode) {
            BlockPos blockPos = BlockPos.ofFloored(centerX, centerY - 0.1, centerZ);
            if (!this.world.getBlockState(blockPos).isOf(ModBlocks.POOP_JAR)) {
                this.markDead();
                return;
            }
        }

        orbitAngle += orbitSpeed;
        bobOffset += bobSpeed;

        this.x = centerX + Math.cos(orbitAngle) * orbitRadius;
        this.z = centerZ + Math.sin(orbitAngle) * orbitRadius;
        this.y = centerY + Math.sin(bobOffset) * bobHeight;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            boolean jarMode = velocityX > 0;
            return new FlyParticle(world, x, y, z, jarMode, spriteProvider);
        }
    }
}
