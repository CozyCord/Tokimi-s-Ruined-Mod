package net.cozystudios.tokimisruinedmod.registry;

import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup TOKIMIS_RUINED_MOD = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(TokimisRuinedMod.MOD_ID, "tokimis_ruined_mod"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup.tokimis_ruined_mod"))
                    .icon(() -> new ItemStack(ModItems.POOP_ITEM))
                    .entries((context, entries) -> {
                        entries.add(ModItems.POOP_ITEM);
                        entries.add(ModItems.GOLDEN_POOP_ITEM);
                        entries.add(ModItems.POOP_JAR_ITEM);
                        entries.add(ModItems.TOENAIL);
                        entries.add(ModItems.FOOT_BLOCK_ITEM);
                        entries.add(ModItems.FOOT_SPAWN_EGG);
                        entries.add(ModItems.MUSHLING_SPAWN_EGG);
                        entries.add(ModItems.RAW_SKIN);
                        entries.add(ModItems.COOKED_SKIN);

                        var enchantmentRegistry = context.lookup().getOptionalWrapper(RegistryKeys.ENCHANTMENT);
                        if (enchantmentRegistry.isPresent()) {
                            var shartedOpt = enchantmentRegistry.get().getOptional(ModEnchantments.SHARTED);
                            if (shartedOpt.isPresent()) {
                                RegistryEntry<Enchantment> sharted = shartedOpt.get();
                                for (int level = 1; level <= 3; level++) {
                                    ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
                                    ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(
                                            ItemEnchantmentsComponent.DEFAULT);
                                    builder.add(sharted, level);
                                    book.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());
                                    entries.add(book);
                                }
                            }
                        }
                    })
                    .build()
    );

    public static void register() {
        TokimisRuinedMod.LOGGER.info("Registering item groups");
    }
}
