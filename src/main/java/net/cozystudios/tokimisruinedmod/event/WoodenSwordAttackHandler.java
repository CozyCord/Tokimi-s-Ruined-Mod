package net.cozystudios.tokimisruinedmod.event;

import net.cozystudios.tokimisruinedmod.block.PoopBlock;
import net.cozystudios.tokimisruinedmod.config.GeneralConfig;
import net.cozystudios.tokimisruinedmod.registry.ModBlocks;
import net.cozystudios.tokimisruinedmod.registry.ModEnchantments;
import net.cozystudios.tokimisruinedmod.registry.ModSounds;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WoodenSwordAttackHandler {
    public static ActionResult onAttack(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (world.isClient) return ActionResult.PASS;
        if (!player.getStackInHand(hand).isOf(Items.WOODEN_SWORD)) return ActionResult.PASS;

        var config = GeneralConfig.get();
        ItemStack stack = player.getStackInHand(hand);

        Direction facing = player.getHorizontalFacing();
        Direction behind = facing.getOpposite();
        BlockPos playerPos = player.getBlockPos();
        BlockPos behindPos = playerPos.offset(behind);

        BlockPos placePos = findGroundPosition(world, behindPos);
        if (placePos == null) return ActionResult.PASS;

        boolean placedGolden = false;
        var enchantmentRegistry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
        var shartedEntry = enchantmentRegistry.getEntry(ModEnchantments.SHARTED);
        if (shartedEntry.isPresent()) {
            int level = EnchantmentHelper.getLevel(shartedEntry.get(), stack);
            if (level > 0) {
                float chance = switch (level) {
                    case 1 -> config.enchantments.shartedLevel1ChancePercent / 100.0f;
                    case 2 -> config.enchantments.shartedLevel2ChancePercent / 100.0f;
                    default -> config.enchantments.shartedLevel3ChancePercent / 100.0f;
                };
                if (world.getRandom().nextFloat() < chance) {
                    world.setBlockState(placePos, ModBlocks.GOLDEN_POOP_BLOCK.getDefaultState());
                    placedGolden = true;
                }
            }
        }

        if (!placedGolden) {
            boolean explosive = world.getRandom().nextFloat() < (config.combat.poopExplosiveChancePercent / 100.0f);

            world.setBlockState(placePos, ModBlocks.POOP_BLOCK.getDefaultState().with(PoopBlock.LIT, explosive));
            if (explosive) {
                world.scheduleBlockTick(placePos, ModBlocks.POOP_BLOCK, 40);
            }
        }

        if (config.sounds.fartSoundEnabled) {
            world.playSound(null, placePos, ModSounds.FART, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }

        if (placedGolden) {
            world.playSound(null, placePos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
        } else {
            world.playSound(null, placePos, SoundEvents.BLOCK_MUD_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }

        return ActionResult.PASS;
    }

    @Nullable
    private static BlockPos findGroundPosition(World world, BlockPos pos) {
        if (world.getBlockState(pos).isReplaceable() && world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
            return pos;
        }
        BlockPos lower = pos.down();
        if (world.getBlockState(lower).isReplaceable() && world.getBlockState(lower.down()).isSolidBlock(world, lower.down())) {
            return lower;
        }
        BlockPos higher = pos.up();
        if (world.getBlockState(higher).isReplaceable() && world.getBlockState(higher.down()).isSolidBlock(world, higher.down())) {
            return higher;
        }
        return null;
    }
}
