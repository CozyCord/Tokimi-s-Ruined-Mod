package net.cozystudios.tokimisruinedmod.registry;

import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.cozystudios.tokimisruinedmod.effect.BleedingEffect;
import net.cozystudios.tokimisruinedmod.effect.PoopyFeetEffect;
import net.cozystudios.tokimisruinedmod.effect.ScratchyThroatEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModStatusEffects {
    public static final RegistryEntry<StatusEffect> POOPY_FEET = Registry.registerReference(
            Registries.STATUS_EFFECT,
            Identifier.of(TokimisRuinedMod.MOD_ID, "poopy_feet"),
            new PoopyFeetEffect()
    );

    public static final RegistryEntry<StatusEffect> SCRATCHY_THROAT = Registry.registerReference(
            Registries.STATUS_EFFECT,
            Identifier.of(TokimisRuinedMod.MOD_ID, "scratchy_throat"),
            new ScratchyThroatEffect()
    );

    public static final RegistryEntry<StatusEffect> BLEEDING = Registry.registerReference(
            Registries.STATUS_EFFECT,
            Identifier.of(TokimisRuinedMod.MOD_ID, "bleeding"),
            new BleedingEffect()
    );

    public static void register() {
        TokimisRuinedMod.LOGGER.info("Registering status effects");
    }
}
