package net.cozystudios.tokimisruinedmod.item;

import net.cozystudios.tokimisruinedmod.config.GeneralConfig;
import net.cozystudios.tokimisruinedmod.entity.ThrownPoopEntity;
import net.cozystudios.tokimisruinedmod.registry.ModBlocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PoopItem extends Item {
    public PoopItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null && player.isSneaking()) {
            World world = context.getWorld();
            BlockPos placePos = context.getBlockPos().offset(context.getSide());
            if (world.getBlockState(placePos).isReplaceable()) {
                if (!world.isClient) {
                    world.setBlockState(placePos, ModBlocks.POOP_BLOCK.getDefaultState());
                    world.playSound(null, placePos, SoundEvents.BLOCK_MUD_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    if (!player.isCreative()) {
                        context.getStack().decrement(1);
                    }
                }
                return ActionResult.success(world.isClient);
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.isSneaking()) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }

        if (!world.isClient) {
            ThrownPoopEntity entity = new ThrownPoopEntity(world, user);
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

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 60;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient && user instanceof PlayerEntity) {
            int elapsed = getMaxUseTime(stack, user) - remainingUseTicks;
            if (elapsed > 0 && elapsed % 15 == 0) {
                int amplifier = Math.min(3, elapsed / 15 - 1);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 60, amplifier, false, false, true));
            }
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            if (GeneralConfig.get().combat.poopEatingKills) {
                user.damage(world.getDamageSources().magic(), Float.MAX_VALUE);
            } else {
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 600, 3));
            }

            if (!user.isPlayer() || !((PlayerEntity) user).isCreative()) {
                stack.decrement(1);
            }
        }
        return stack;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
    }
}
