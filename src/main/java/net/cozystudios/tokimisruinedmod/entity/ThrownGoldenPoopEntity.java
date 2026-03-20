package net.cozystudios.tokimisruinedmod.entity;

import net.cozystudios.tokimisruinedmod.config.GeneralConfig;
import net.cozystudios.tokimisruinedmod.registry.ModEntities;
import net.cozystudios.tokimisruinedmod.registry.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ThrownGoldenPoopEntity extends ThrownItemEntity {
    public ThrownGoldenPoopEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public ThrownGoldenPoopEntity(World world, LivingEntity owner) {
        super(ModEntities.THROWN_GOLDEN_POOP, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GOLDEN_POOP_ITEM;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        var config = GeneralConfig.get();
        var entity = entityHitResult.getEntity();

        if (config.combat.goldenPoopInstaKill) {
            entity.damage(this.getDamageSources().thrown(this, this.getOwner()), Float.MAX_VALUE);
            if (entity instanceof LivingEntity living && living.isAlive()) {
                living.kill();
            }
        } else {
            entity.damage(this.getDamageSources().thrown(this, this.getOwner()), config.combat.goldenPoopDamageHearts * 2.0f);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            ItemEntity itemEntity = new ItemEntity(this.getWorld(),
                    this.getX(), this.getY(), this.getZ(),
                    new ItemStack(ModItems.GOLDEN_POOP_ITEM));
            this.getWorld().spawnEntity(itemEntity);
            this.discard();
        }
    }
}
