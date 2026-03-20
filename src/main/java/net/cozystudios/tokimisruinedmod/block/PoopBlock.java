package net.cozystudios.tokimisruinedmod.block;

import net.cozystudios.tokimisruinedmod.config.GeneralConfig;
import net.cozystudios.tokimisruinedmod.registry.ModParticles;
import net.cozystudios.tokimisruinedmod.registry.ModStatusEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PoopBlock extends Block {
    public static final BooleanProperty LIT = Properties.LIT;

    private static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(3, 0, 3, 13, 2, 13),
            Block.createCuboidShape(4, 2, 4, 12, 4, 12),
            Block.createCuboidShape(6, 4, 6, 10, 5, 10),
            Block.createCuboidShape(7, 5, 7, 9, 6, 9)
    );

    public PoopBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(LIT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
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
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.isOf(Items.FLINT_AND_STEEL) && !state.get(LIT)) {
            if (!world.isClient) {
                world.setBlockState(pos, state.with(LIT, true));
                world.scheduleBlockTick(pos, this, 40);
                world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                stack.damage(1, player, player.getActiveHand() == Hand.MAIN_HAND
                        ? net.minecraft.entity.EquipmentSlot.MAINHAND
                        : net.minecraft.entity.EquipmentSlot.OFFHAND);
            }
            return ItemActionResult.success(world.isClient);
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            world.removeBlock(pos, false);
            world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    4.0f, World.ExplosionSourceType.BLOCK);
        }
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return false;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient && entity instanceof LivingEntity living) {
            var config = GeneralConfig.get();
            living.addStatusEffect(new StatusEffectInstance(
                    ModStatusEffects.POOPY_FEET, config.effects.poopyFeetDurationSeconds * 20, 0, false, false, false));
            world.playSound(null, pos, SoundEvents.BLOCK_MUD_STEP, SoundCategory.BLOCKS, 1.0f, 0.8f);
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!GeneralConfig.get().particles.flyParticlesOnPoop) return;
        if (random.nextInt(4) == 0) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.4 + random.nextDouble() * 0.4;
            double z = pos.getZ() + 0.5;
            world.addParticle(ModParticles.FLY, x, y, z, 0, 0, 0);
        }
    }
}
