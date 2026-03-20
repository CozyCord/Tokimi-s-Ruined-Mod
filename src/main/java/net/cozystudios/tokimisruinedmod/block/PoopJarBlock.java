package net.cozystudios.tokimisruinedmod.block;

import net.cozystudios.tokimisruinedmod.config.GeneralConfig;
import net.cozystudios.tokimisruinedmod.registry.ModParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PoopJarBlock extends Block {
    private static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(5, 0, 5, 11, 8, 11),
            Block.createCuboidShape(7, 8, 7, 9, 9, 9)
    );

    public PoopJarBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!GeneralConfig.get().particles.flyParticlesInJar) return;
        for (int i = 0; i < 2; i++) {
            double x = pos.getX() + 0.5625 + (random.nextDouble() - 0.5) * 0.1;
            double y = pos.getY() + 0.25 + random.nextDouble() * 0.15;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.1;
            world.addParticle(ModParticles.FLY, x, y, z, 1.0, 0, 0);
        }
    }
}
