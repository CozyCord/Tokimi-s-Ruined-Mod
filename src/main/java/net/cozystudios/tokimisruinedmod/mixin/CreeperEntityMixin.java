package net.cozystudios.tokimisruinedmod.mixin;

import net.cozystudios.tokimisruinedmod.config.GeneralConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity {

    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void enableDoorPathfinding(CallbackInfo ci) {
        if (!GeneralConfig.get().mobs.creeperDoorOpeningEnabled) return;
        if (this.getNavigation() instanceof MobNavigation mobNav) {
            mobNav.setCanPathThroughDoors(true);
            mobNav.setCanEnterOpenDoors(true);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void openNearbyDoors(CallbackInfo ci) {
        if (!GeneralConfig.get().mobs.creeperDoorOpeningEnabled) return;
        if (this.getWorld().isClient) return;
        if (this.getTarget() == null) return;

        if (this.age % 5 != 0) return;

        BlockPos mobPos = this.getBlockPos();
        World world = this.getWorld();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (Math.abs(dx) + Math.abs(dz) > 1) continue;
                for (int dy = 0; dy <= 1; dy++) {
                    BlockPos checkPos = mobPos.add(dx, dy, dz);
                    BlockState state = world.getBlockState(checkPos);
                    if (state.getBlock() instanceof DoorBlock door && !state.get(DoorBlock.OPEN)) {
                        door.setOpen(this, world, state, checkPos, true);
                        world.createExplosion(this, this.getX(), this.getY(), this.getZ(),
                                10.0f, World.ExplosionSourceType.MOB);
                        for (var player : world.getEntitiesByClass(net.minecraft.entity.player.PlayerEntity.class,
                                this.getBoundingBox().expand(10), p -> !p.isCreative() && !p.isSpectator())) {
                            player.damage(this.getDamageSources().explosion(this, this), Float.MAX_VALUE);
                            if (player.isAlive()) player.kill();
                        }
                        this.discard();
                        return;
                    }
                }
            }
        }
    }
}
