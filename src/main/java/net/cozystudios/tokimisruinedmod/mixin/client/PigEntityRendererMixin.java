package net.cozystudios.tokimisruinedmod.mixin.client;

import net.cozystudios.tokimisruinedmod.util.PigVariantHelper;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PigEntityRenderer.class)
public abstract class PigEntityRendererMixin {

    private static final Identifier AGGRO_TEXTURE =
            Identifier.of("tokimis_ruined_mod", "textures/entity/pig_aggro.png");

    @Inject(method = "getTexture", at = @At("RETURN"), cancellable = true)
    private void swapAggroTexture(PigEntity pig, CallbackInfoReturnable<Identifier> cir) {
        if (PigVariantHelper.isAggro(pig)) {
            cir.setReturnValue(AGGRO_TEXTURE);
        }
    }
}
