package net.cozystudios.tokimisruinedmod.entity;

import net.cozystudios.tokimisruinedmod.registry.ModItems;
import net.cozystudios.tokimisruinedmod.registry.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FootEntity extends HostileEntity {
    private boolean hasLanded = false;
    private int fuseTimer = -1;
    private static final int FUSE_TIME = 30;
    private int shearedCount = 0;
    private static final int MAX_SHEARS = 5;

    public FootEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(5, new LookAroundGoal(this));

        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true, false) {
            @Override
            public boolean canStart() {
                return super.canStart() && !(this.targetEntity instanceof PlayerEntity p && (p.isCreative() || p.isSpectator()));
            }
        });
    }

    public static DefaultAttributeContainer.Builder createFootAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(Items.SHEARS) && shearedCount < MAX_SHEARS) {
            if (!this.getWorld().isClient) {
                shearedCount++;
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.HOSTILE, 1.0f, 1.0f);
                ItemEntity toenail = new ItemEntity(this.getWorld(),
                        this.getX(), this.getY() + 0.5, this.getZ(),
                        new ItemStack(ModItems.TOENAIL));
                this.getWorld().spawnEntity(toenail);
                stack.damage(1, player, player.getActiveHand() == Hand.MAIN_HAND
                        ? net.minecraft.entity.EquipmentSlot.MAINHAND
                        : net.minecraft.entity.EquipmentSlot.OFFHAND);
            }
            return ActionResult.success(this.getWorld().isClient);
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            if (!hasLanded && this.isOnGround()) {
                hasLanded = true;
            }

            if (this.getTarget() instanceof PlayerEntity target) {
                if (target.isCreative() || target.isSpectator()) {
                    this.setTarget(null);
                } else {
                    double dist = this.squaredDistanceTo(target);
                    if (dist < 4.0) {
                        if (fuseTimer == -1) {
                            fuseTimer = FUSE_TIME;
                        }
                    } else if (fuseTimer > 0) {
                        fuseTimer = -1;
                    }
                }
            }

            if (fuseTimer > 0) {
                fuseTimer--;
            } else if (fuseTimer == 0) {
                this.explode();
            }
        }
    }

    private void explode() {
        this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(),
                3.0f, World.ExplosionSourceType.MOB);
        this.discard();
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        if (!hasLanded) {
            return false;
        }
        return super.handleFallDamage(fallDistance, damageMultiplier, damageSource);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                ModSounds.FOOTSTEP, SoundCategory.HOSTILE, 1.0f, 0.9f + this.getWorld().getRandom().nextFloat() * 0.2f);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        super.dropLoot(damageSource, causedByPlayer);
        this.dropItem(ModItems.FOOT_BLOCK_ITEM);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("HasLanded", hasLanded);
        nbt.putInt("ShearedCount", shearedCount);
        nbt.putInt("FuseTimer", fuseTimer);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        hasLanded = nbt.getBoolean("HasLanded");
        shearedCount = nbt.getInt("ShearedCount");
        fuseTimer = nbt.getInt("FuseTimer");
    }

    public int getFuseTimer() {
        return fuseTimer;
    }

    public int getShearedCount() {
        return shearedCount;
    }
}
