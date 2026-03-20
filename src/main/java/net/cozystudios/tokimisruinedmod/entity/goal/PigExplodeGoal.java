package net.cozystudios.tokimisruinedmod.entity.goal;

import net.cozystudios.tokimisruinedmod.util.PigVariantHelper;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.EnumSet;

public class PigExplodeGoal extends Goal {

    private final PigEntity pig;
    private PlayerEntity target;

    public PigExplodeGoal(PigEntity pig) {
        this.pig = pig;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    private int getVariant() {
        return PigVariantHelper.getVariant(pig);
    }

    private double getDetectRange() {
        return getVariant() == 3 ? 32.0 : 12.0;
    }

    private double getExplodeRange() {
        return getVariant() == 3 ? 2.0 : 1.5;
    }

    @Override
    public boolean canStart() {
        int v = getVariant();
        if (v != 2 && v != 3) return false;
        target = pig.getWorld().getClosestPlayer(
                pig.getX(), pig.getY(), pig.getZ(),
                getDetectRange(),
                p -> p instanceof PlayerEntity player && !player.isCreative() && !player.isSpectator()
        );
        return target != null;
    }

    @Override
    public boolean shouldContinue() {
        int v = getVariant();
        if (v != 2 && v != 3) return false;
        if (target == null || !target.isAlive()) return false;
        if (v == 3) return true;
        return pig.squaredDistanceTo(target) < 256.0;
    }

    @Override
    public void start() {
        PigVariantHelper.setAggro(pig, true);
    }

    @Override
    public void stop() {
        target = null;
        pig.getNavigation().stop();
        PigVariantHelper.setAggro(pig, false);
    }

    @Override
    public void tick() {
        if (target == null || !target.isAlive()) return;

        pig.getLookControl().lookAt(target, 30.0F, 30.0F);
        double dist = pig.distanceTo(target);

        if (dist <= getExplodeRange()) {
            detonate();
            return;
        }

        if (getVariant() == 3) {
            pig.setNoGravity(true);
            double dx = target.getX() - pig.getX();
            double dy = (target.getY() + 0.5) - pig.getY();
            double dz = target.getZ() - pig.getZ();
            double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double speed = 0.22;
            pig.setVelocity(
                    pig.getVelocity().multiply(0.7).add(dx / d * speed, dy / d * speed, dz / d * speed)
            );
            pig.setYaw((float) Math.toDegrees(Math.atan2(-dx, dz)));
        } else {
            pig.getNavigation().startMovingTo(target, 2.0);
        }
    }

    private void detonate() {
        World world = pig.getWorld();
        double x = pig.getX(), y = pig.getY(), z = pig.getZ();

        world.createExplosion(pig, x, y, z, 10.0f, World.ExplosionSourceType.MOB);

        if (world instanceof ServerWorld) {
            world.getEntitiesByClass(
                    PlayerEntity.class,
                    pig.getBoundingBox().expand(12),
                    p -> !p.isCreative() && !p.isSpectator()
            ).forEach(player -> {
                player.damage(world.getDamageSources().explosion(pig, pig), Float.MAX_VALUE);
                if (player.isAlive()) player.kill();
            });
        }

        pig.discard();
    }
}
