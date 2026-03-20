package net.cozystudios.tokimisruinedmod.entity;

import net.cozystudios.tokimisruinedmod.config.GeneralConfig;
import net.cozystudios.tokimisruinedmod.network.PoopOverlayPayload;
import net.cozystudios.tokimisruinedmod.registry.ModEntities;
import net.cozystudios.tokimisruinedmod.registry.ModItems;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ThrownPoopEntity extends ThrownItemEntity {
    public ThrownPoopEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public ThrownPoopEntity(World world, LivingEntity owner) {
        super(ModEntities.THROWN_POOP, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.POOP_ITEM;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        var config = GeneralConfig.get();
        var entity = entityHitResult.getEntity();

        entity.damage(this.getDamageSources().thrown(this, this.getOwner()), config.combat.poopThrowDamageHearts * 2.0f);

        if (entity instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, config.effects.nauseaDurationSeconds * 20, 0));
        }

        if (config.effects.poopOverlayEnabled && entity instanceof ServerPlayerEntity player) {
            ServerPlayNetworking.send(player, new PoopOverlayPayload());
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.discard();
        }
    }
}
