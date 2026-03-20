package net.cozystudios.tokimisruinedmod.util;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PigEntity;

public class PigVariantHelper {
    public static final TrackedData<Integer> PIG_VARIANT = DataTracker.registerData(
            PigEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public static final TrackedData<Boolean> PIG_AGGRO = DataTracker.registerData(
            PigEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public static int getVariant(PigEntity pig) {
        return pig.getDataTracker().get(PIG_VARIANT);
    }

    public static boolean isAggro(PigEntity pig) {
        return pig.getDataTracker().get(PIG_AGGRO);
    }

    public static void setAggro(PigEntity pig, boolean aggro) {
        pig.getDataTracker().set(PIG_AGGRO, aggro);
    }
}
