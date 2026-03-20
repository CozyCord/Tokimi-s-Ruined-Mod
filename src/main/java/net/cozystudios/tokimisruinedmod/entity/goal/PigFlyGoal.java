package net.cozystudios.tokimisruinedmod.entity.goal;

import net.cozystudios.tokimisruinedmod.util.PigVariantHelper;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class PigFlyGoal extends Goal {

    private final PigEntity pig;
    private double targetX, targetY, targetZ;
    private int newTargetCooldown = 0;

    private static final double MIN_HEIGHT_ABOVE_GROUND = 8.0;
    private static final double MAX_HEIGHT_ABOVE_GROUND = 24.0;

    public PigFlyGoal(PigEntity pig) {
        this.pig = pig;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        int v = PigVariantHelper.getVariant(pig);
        return v == 1 || v == 3;
    }

    @Override
    public boolean shouldContinue() {
        return canStart();
    }

    @Override
    public void start() {
        pickTarget();
    }

    private double getSurfaceY() {
        BlockPos surface = pig.getWorld().getTopPosition(
                net.minecraft.world.Heightmap.Type.MOTION_BLOCKING,
                pig.getBlockPos()
        );
        return surface.getY();
    }

    private void pickTarget() {
        double range = 20.0;
        targetX = pig.getX() + (pig.getRandom().nextDouble() - 0.5) * range * 2;
        targetZ = pig.getZ() + (pig.getRandom().nextDouble() - 0.5) * range * 2;

        double surfaceY = getSurfaceY();
        double heightAboveGround = MIN_HEIGHT_ABOVE_GROUND
                + pig.getRandom().nextDouble() * (MAX_HEIGHT_ABOVE_GROUND - MIN_HEIGHT_ABOVE_GROUND);
        targetY = surfaceY + heightAboveGround;

        newTargetCooldown = 80 + pig.getRandom().nextInt(60);
    }

    @Override
    public void tick() {
        pig.setNoGravity(true);

        if (--newTargetCooldown <= 0) {
            pickTarget();
        }

        double dx = targetX - pig.getX();
        double dy = targetY - pig.getY();
        double dz = targetZ - pig.getZ();
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (dist < 2.0) {
            pickTarget();
            return;
        }

        double surfaceY = getSurfaceY();
        boolean needsToClimb = pig.getY() < surfaceY + MIN_HEIGHT_ABOVE_GROUND;

        double speed = 0.14;
        double ySpeed = needsToClimb
                ? speed * 1.5
                : speed;

        pig.setVelocity(
                pig.getVelocity().x * 0.85 + (dx / dist) * speed,
                pig.getVelocity().y * 0.85 + (dy / dist) * ySpeed,
                pig.getVelocity().z * 0.85 + (dz / dist) * speed
        );
        pig.setYaw((float) Math.toDegrees(Math.atan2(-dx, dz)));
    }
}
