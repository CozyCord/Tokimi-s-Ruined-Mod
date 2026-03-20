package net.cozystudios.tokimisruinedmod.registry;

import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final Identifier FART_ID = Identifier.of(TokimisRuinedMod.MOD_ID, "fart");
    public static final SoundEvent FART = SoundEvent.of(FART_ID);

    public static final Identifier FOOTSTEP_ID = Identifier.of(TokimisRuinedMod.MOD_ID, "footstep");
    public static final SoundEvent FOOTSTEP = SoundEvent.of(FOOTSTEP_ID);

    public static final SoundEvent[] SUBSCRIBE = new SoundEvent[7];
    static {
        for (int i = 0; i < 7; i++) {
            Identifier id = Identifier.of(TokimisRuinedMod.MOD_ID, "subscribe_" + (i + 1));
            SUBSCRIBE[i] = SoundEvent.of(id);
        }
    }

    public static final Identifier MUSHLING_HURT_ID = Identifier.of(TokimisRuinedMod.MOD_ID, "mushling_hurt");
    public static final SoundEvent MUSHLING_HURT = SoundEvent.of(MUSHLING_HURT_ID);

    public static final Identifier MUSHLING_DEATH_ID = Identifier.of(TokimisRuinedMod.MOD_ID, "mushling_death");
    public static final SoundEvent MUSHLING_DEATH = SoundEvent.of(MUSHLING_DEATH_ID);

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, FART_ID, FART);
        Registry.register(Registries.SOUND_EVENT, FOOTSTEP_ID, FOOTSTEP);
        for (int i = 0; i < 7; i++) {
            Identifier id = Identifier.of(TokimisRuinedMod.MOD_ID, "subscribe_" + (i + 1));
            Registry.register(Registries.SOUND_EVENT, id, SUBSCRIBE[i]);
        }
        Registry.register(Registries.SOUND_EVENT, MUSHLING_HURT_ID, MUSHLING_HURT);
        Registry.register(Registries.SOUND_EVENT, MUSHLING_DEATH_ID, MUSHLING_DEATH);
        TokimisRuinedMod.LOGGER.info("Registering sounds");
    }
}
