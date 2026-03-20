package net.cozystudios.tokimisruinedmod.mixin;

import net.cozystudios.tokimisruinedmod.entity.goal.PigExplodeGoal;
import net.cozystudios.tokimisruinedmod.entity.goal.PigFlyGoal;
import net.cozystudios.tokimisruinedmod.util.PigVariantHelper;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigEntity.class)
public abstract class PigEntityMixin {

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initPigVariant(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(PigVariantHelper.PIG_VARIANT, 0);
        builder.add(PigVariantHelper.PIG_AGGRO, false);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void addPigGoals(CallbackInfo ci) {
        PigEntity pig = (PigEntity)(Object)this;
        GoalSelector goals = ((MobEntityAccessor) pig).getGoalSelector();
        goals.add(1, new PigExplodeGoal(pig));
        goals.add(2, new PigFlyGoal(pig));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writePigVariant(NbtCompound nbt, CallbackInfo ci) {
        PigEntity pig = (PigEntity)(Object)this;
        nbt.putInt("TokimiPigVariant", pig.getDataTracker().get(PigVariantHelper.PIG_VARIANT));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readPigVariant(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("TokimiPigVariant")) {
            PigEntity pig = (PigEntity)(Object)this;
            int variant = nbt.getInt("TokimiPigVariant");
            pig.getDataTracker().set(PigVariantHelper.PIG_VARIANT, variant);
            if (variant == 1 || variant == 3) {
                pig.setNoGravity(true);
            }
        }
    }
}
