package net.cozystudios.tokimisruinedmod.event;

import net.cozystudios.tokimisruinedmod.registry.ModItems;
import net.cozystudios.tokimisruinedmod.registry.ModStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PlayerShearHandler {
    public static ActionResult onInteract(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (world.isClient) return ActionResult.PASS;
        if (!(entity instanceof PlayerEntity target)) return ActionResult.PASS;
        if (target == player) return ActionResult.PASS;
        if (player.isSneaking()) return ActionResult.PASS;

        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.SHEARS)) return ActionResult.PASS;

        world.playSound(null, target.getX(), target.getY(), target.getZ(),
                SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0f, 1.0f);

        ItemEntity skinDrop = new ItemEntity(world,
                target.getX(), target.getY() + 0.5, target.getZ(),
                new ItemStack(ModItems.RAW_SKIN));
        world.spawnEntity(skinDrop);

        stack.damage(1, player, hand == Hand.MAIN_HAND
                ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

        target.addStatusEffect(new StatusEffectInstance(
                ModStatusEffects.BLEEDING, 600, 0, false, false, true));

        target.damage(world.getDamageSources().playerAttack(player), 4.0f);

        return ActionResult.SUCCESS;
    }

    public static TypedActionResult<ItemStack> onSelfShear(PlayerEntity player, World world, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.SHEARS)) return TypedActionResult.pass(stack);
        if (!player.isSneaking()) return TypedActionResult.pass(stack);
        if (world.isClient) return TypedActionResult.success(stack);

        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0f, 1.0f);

        ItemEntity skinDrop = new ItemEntity(world,
                player.getX(), player.getY() + 0.5, player.getZ(),
                new ItemStack(ModItems.RAW_SKIN));
        world.spawnEntity(skinDrop);

        stack.damage(1, player, hand == Hand.MAIN_HAND
                ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

        player.addStatusEffect(new StatusEffectInstance(
                ModStatusEffects.BLEEDING, 600, 0, false, false, true));

        player.damage(world.getDamageSources().generic(), 4.0f);

        return TypedActionResult.success(stack);
    }
}
