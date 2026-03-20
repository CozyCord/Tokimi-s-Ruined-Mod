package net.cozystudios.tokimisruinedmod.mixin;

import net.cozystudios.tokimisruinedmod.config.GeneralConfig;
import net.cozystudios.tokimisruinedmod.util.PigVariantHelper;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityInitializeMixin {

    @Inject(method = "initialize", at = @At("RETURN"))
    private void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty,
                               SpawnReason reason, EntityData data,
                               CallbackInfoReturnable<EntityData> cir) {
        if (!((Object) this instanceof PigEntity pig)) return;

        GeneralConfig.PigOptions cfg = GeneralConfig.get().pigs;
        float flyExplodeChance  = cfg.flyExplodePigsEnabled  ? cfg.flyExplodePigChancePercent  / 100f : 0f;
        float explodeChance     = cfg.explodingPigsEnabled   ? cfg.explodingPigChancePercent   / 100f : 0f;
        float flyChance         = cfg.flyingPigsEnabled      ? cfg.flyingPigChancePercent      / 100f : 0f;

        float r = pig.getRandom().nextFloat();
        int variant;
        if (r < flyExplodeChance)                          variant = 3;
        else if (r < flyExplodeChance + explodeChance)     variant = 2;
        else if (r < flyExplodeChance + explodeChance + flyChance) variant = 1;
        else                                               variant = 0;

        pig.getDataTracker().set(PigVariantHelper.PIG_VARIANT, variant);
        if (variant == 1 || variant == 3) {
            pig.setNoGravity(true);
        }
    }
}
