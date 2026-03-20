package net.cozystudios.tokimisruinedmod.item;

import net.cozystudios.tokimisruinedmod.entity.ThrownGoldenPoopEntity;
import net.cozystudios.tokimisruinedmod.registry.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GoldenPoopItem extends BlockItem {
    public GoldenPoopItem(Settings settings) {
        super(ModBlocks.GOLDEN_POOP_BLOCK, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null && player.isSneaking()) {
            ActionResult result = super.useOnBlock(context);
            if (result.isAccepted()) {
                World world = context.getWorld();
                BlockPos pos = context.getBlockPos().offset(context.getSide());
                if (!world.isClient) {
                    world.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
            }
            return result;
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!user.isSneaking()) {
            if (!world.isClient) {
                ThrownGoldenPoopEntity entity = new ThrownGoldenPoopEntity(world, user);
                entity.setItem(stack);
                entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
                world.spawnEntity(entity);
            }
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f);
            if (!user.isCreative()) {
                stack.decrement(1);
            }
            return TypedActionResult.success(stack, world.isClient);
        }

        return TypedActionResult.pass(stack);
    }
}
