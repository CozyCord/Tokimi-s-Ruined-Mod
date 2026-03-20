package net.cozystudios.tokimisruinedmod.entity;

import net.cozystudios.tokimisruinedmod.registry.ModEntities;
import net.cozystudios.tokimisruinedmod.registry.ModSounds;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class MushlingEntity extends PathAwareEntity {

    private static final TrackedData<Integer> PHASE = DataTracker.registerData(
            MushlingEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    private int subscribeCooldown;
    private boolean soundPlayedThisPhase;
    private int explosionCountdown = -1;
    private boolean isMinion = false;

    public MushlingEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.subscribeCooldown = 40 + this.random.nextInt(21);
        this.soundPlayedThisPhase = false;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(PHASE, 0);
    }

    public static DefaultAttributeContainer.Builder createMushlingAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 16.0F));

        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, false, false));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean result = super.damage(source, amount);
        if (result && this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSoundFromEntity(null, this, ModSounds.MUSHLING_HURT, SoundCategory.HOSTILE, 1.0f, 1.0f);
        }
        if (source.getAttacker() instanceof PlayerEntity player) {
            if (!player.isCreative() && !player.isSpectator()) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
            }
        }
        return result;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSoundFromEntity(null, this, ModSounds.MUSHLING_DEATH, SoundCategory.HOSTILE, 1.0f, 1.0f);
        }
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            setupAnimationStates();
            return;
        }

        int currentPhase = this.dataTracker.get(PHASE);

        if (isMinion) {
            if (explosionCountdown > 0) {
                explosionCountdown--;
            } else if (explosionCountdown == 0) {
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(),
                        3.0f, World.ExplosionSourceType.MOB);
                this.discard();
            }
            return;
        }

        if (explosionCountdown > 0) {
            explosionCountdown--;
            return;
        } else if (explosionCountdown == 0) {
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(),
                    6.0f, World.ExplosionSourceType.MOB);
            for (var player : this.getWorld().getEntitiesByClass(PlayerEntity.class,
                    this.getBoundingBox().expand(8), p -> !p.isCreative() && !p.isSpectator())) {
                player.damage(this.getDamageSources().explosion(this, this), Float.MAX_VALUE);
                if (player.isAlive()) player.kill();
            }
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                for (int i = 0; i < 30; i++) {
                    MushlingEntity minion = new MushlingEntity(ModEntities.MUSHLING, serverWorld);
                    if (minion != null) {
                        double angle = (i / 30.0) * Math.PI * 2;
                        double radius = 1.5 + this.random.nextDouble() * 3.0;
                        double mx = this.getX() + Math.cos(angle) * radius;
                        double mz = this.getZ() + Math.sin(angle) * radius;
                        minion.refreshPositionAndAngles(mx, this.getY(), mz, this.random.nextFloat() * 360f, 0f);
                        minion.isMinion = true;
                        minion.explosionCountdown = this.random.nextInt(30);
                        serverWorld.spawnEntity(minion);
                    }
                }
            }
            this.discard();
            return;
        }

        if (this.subscribeCooldown > 0) {
            this.subscribeCooldown--;
        } else if (!soundPlayedThisPhase) {
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                float volume = 1.0f + currentPhase * 0.2f;
                serverWorld.playSoundFromEntity(null, this, ModSounds.SUBSCRIBE[currentPhase], SoundCategory.HOSTILE, volume, 1.0f);
            }
            soundPlayedThisPhase = true;

            int soundLengthTicks = switch (currentPhase) {
                case 0 -> 18;
                case 1 -> 48;
                case 2 -> 159;
                case 3 -> 101;
                case 4 -> 54;
                case 5 -> 146;
                case 6 -> 92;
                default -> 40;
            };
            this.subscribeCooldown = soundLengthTicks + 40;

            if (currentPhase >= 6) {
                explosionCountdown = soundLengthTicks;
            }
        } else if (currentPhase < 6) {
            this.dataTracker.set(PHASE, currentPhase + 1);
            soundPlayedThisPhase = false;
            this.subscribeCooldown = 0;
        }
    }

    public float getAngerLevel() {
        return this.dataTracker.get(PHASE) / 6.0f;
    }

    public int getCurrentPhase() {
        return this.dataTracker.get(PHASE);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("SubscribeCooldown", this.subscribeCooldown);
        nbt.putInt("CurrentPhase", this.dataTracker.get(PHASE));
        nbt.putBoolean("SoundPlayedThisPhase", this.soundPlayedThisPhase);
        nbt.putInt("ExplosionCountdown", this.explosionCountdown);
        nbt.putBoolean("IsMinion", this.isMinion);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("SubscribeCooldown")) this.subscribeCooldown = nbt.getInt("SubscribeCooldown");
        if (nbt.contains("CurrentPhase")) this.dataTracker.set(PHASE, nbt.getInt("CurrentPhase"));
        if (nbt.contains("SoundPlayedThisPhase")) this.soundPlayedThisPhase = nbt.getBoolean("SoundPlayedThisPhase");
        if (nbt.contains("ExplosionCountdown")) this.explosionCountdown = nbt.getInt("ExplosionCountdown");
        if (nbt.contains("IsMinion")) this.isMinion = nbt.getBoolean("IsMinion");
    }
}
