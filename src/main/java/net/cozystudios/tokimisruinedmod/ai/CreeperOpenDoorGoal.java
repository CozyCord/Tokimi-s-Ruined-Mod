package net.cozystudios.tokimisruinedmod.ai;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

public class CreeperOpenDoorGoal extends Goal {
    private final MobEntity mob;
    private BlockPos doorPos;

    public CreeperOpenDoorGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (mob.getTarget() == null) return false;
        if (!mob.horizontalCollision) return false;

        doorPos = findNearbyDoor();
        return doorPos != null;
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void start() {
        if (doorPos != null) {
            World world = mob.getWorld();
            BlockState state = world.getBlockState(doorPos);
            if (state.getBlock() instanceof DoorBlock door) {
                if (!state.get(DoorBlock.OPEN)) {
                    door.setOpen(mob, world, state, doorPos, true);
                }
            }
        }
    }

    private BlockPos findNearbyDoor() {
        BlockPos mobPos = mob.getBlockPos();
        World world = mob.getWorld();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = 0; dy <= 1; dy++) {
                    BlockPos checkPos = mobPos.add(dx, dy, dz);
                    BlockState state = world.getBlockState(checkPos);
                    if (state.getBlock() instanceof DoorBlock && !state.get(DoorBlock.OPEN)) {
                        return checkPos;
                    }
                }
            }
        }
        return null;
    }
}
