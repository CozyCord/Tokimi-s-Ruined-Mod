package net.cozystudios.tokimisruinedmod.mixin;

import net.cozystudios.tokimisruinedmod.config.GeneralConfig;
import net.cozystudios.tokimisruinedmod.registry.ModBlocks;
import net.cozystudios.tokimisruinedmod.registry.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {

    @Inject(method = "onSyncedBlockEvent", at = @At("HEAD"), cancellable = true)
    private void onPlayNote(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
        BlockState below = world.getBlockState(pos.down());
        if (below.isOf(ModBlocks.POOP_BLOCK)) {
            var config = GeneralConfig.get();
            if (config.sounds.fartSoundEnabled) {
                int note = state.get(NoteBlock.NOTE);
                float pitch = (float) Math.pow(2.0, (note - 12) / 12.0);
                world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        ModSounds.FART, SoundCategory.RECORDS, 3.0f, pitch, world.random.nextLong());
                world.addParticle(ParticleTypes.NOTE,
                        pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5,
                        note / 24.0, 0, 0);
            }
            cir.setReturnValue(true);
        }
    }
}
