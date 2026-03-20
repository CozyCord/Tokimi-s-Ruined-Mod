package net.cozystudios.tokimisruinedmod.item;

import net.cozystudios.tokimisruinedmod.registry.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ToenailItem extends Item {
    public ToenailItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            user.addStatusEffect(new StatusEffectInstance(
                    ModStatusEffects.SCRATCHY_THROAT, 400, 0, false, true, true));
        }
        return super.finishUsing(stack, world, user);
    }
}
