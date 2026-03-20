package net.cozystudios.tokimisruinedmod.registry;

import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.cozystudios.tokimisruinedmod.item.GoldenPoopItem;
import net.cozystudios.tokimisruinedmod.item.PoopItem;
import net.cozystudios.tokimisruinedmod.item.ToenailItem;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item POOP_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(TokimisRuinedMod.MOD_ID, "poop"),
            new PoopItem(new Item.Settings()
                    .food(new FoodComponent.Builder()
                            .alwaysEdible()
                            .nutrition(0)
                            .saturationModifier(0)
                            .build())
            )
    );

    public static final Item GOLDEN_POOP_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(TokimisRuinedMod.MOD_ID, "golden_poop"),
            new GoldenPoopItem(new Item.Settings())
    );

    public static final Item TOENAIL = Registry.register(
            Registries.ITEM,
            Identifier.of(TokimisRuinedMod.MOD_ID, "toenail"),
            new ToenailItem(new Item.Settings()
                    .food(new FoodComponent.Builder()
                            .alwaysEdible()
                            .nutrition(1)
                            .saturationModifier(0.5f)
                            .build())
            )
    );

    public static final Item RAW_SKIN = Registry.register(
            Registries.ITEM,
            Identifier.of(TokimisRuinedMod.MOD_ID, "raw_skin"),
            new Item(new Item.Settings()
                    .food(new FoodComponent.Builder()
                            .alwaysEdible()
                            .nutrition(1)
                            .saturationModifier(0.5f)
                            .build())
            )
    );

    public static final Item COOKED_SKIN = Registry.register(
            Registries.ITEM,
            Identifier.of(TokimisRuinedMod.MOD_ID, "cooked_skin"),
            new Item(new Item.Settings()
                    .food(new FoodComponent.Builder()
                            .nutrition(2)
                            .saturationModifier(2.0f)
                            .build())
            )
    );

    public static final Item FOOT_BLOCK_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(TokimisRuinedMod.MOD_ID, "foot_block"),
            new net.minecraft.item.BlockItem(ModBlocks.FOOT_BLOCK, new Item.Settings())
    );

    public static final Item POOP_JAR_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(TokimisRuinedMod.MOD_ID, "poop_jar"),
            new net.minecraft.item.BlockItem(ModBlocks.POOP_JAR, new Item.Settings())
    );

    public static final Item MUSHLING_SPAWN_EGG = Registry.register(
            Registries.ITEM,
            Identifier.of(TokimisRuinedMod.MOD_ID, "mushling_spawn_egg"),
            new SpawnEggItem(ModEntities.MUSHLING, 0x8B4513, 0xCD853F, new Item.Settings())
    );

    public static final Item FOOT_SPAWN_EGG = Registry.register(
            Registries.ITEM,
            Identifier.of(TokimisRuinedMod.MOD_ID, "foot_spawn_egg"),
            new SpawnEggItem(ModEntities.FOOT, 0xE8C9A0, 0xD4956B, new Item.Settings())
    );

    public static void register() {
        TokimisRuinedMod.LOGGER.info("Registering items");
    }
}
