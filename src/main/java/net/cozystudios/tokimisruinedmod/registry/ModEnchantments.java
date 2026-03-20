package net.cozystudios.tokimisruinedmod.registry;

import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> SHARTED = RegistryKey.of(
            RegistryKeys.ENCHANTMENT,
            Identifier.of(TokimisRuinedMod.MOD_ID, "sharted")
    );

    public static void register() {
        TokimisRuinedMod.LOGGER.info("Registering enchantments");
    }
}
