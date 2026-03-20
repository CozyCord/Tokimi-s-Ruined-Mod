package net.cozystudios.tokimisruinedmod.registry;

import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.cozystudios.tokimisruinedmod.block.FootBlock;
import net.cozystudios.tokimisruinedmod.block.GoldenPoopBlock;
import net.cozystudios.tokimisruinedmod.block.PoopBlock;
import net.cozystudios.tokimisruinedmod.block.PoopJarBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block POOP_BLOCK = Registry.register(
            Registries.BLOCK,
            Identifier.of(TokimisRuinedMod.MOD_ID, "poop_block"),
            new PoopBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.MUD)
                    .strength(0.5f)
                    .nonOpaque()
            )
    );

    public static final Block GOLDEN_POOP_BLOCK = Registry.register(
            Registries.BLOCK,
            Identifier.of(TokimisRuinedMod.MOD_ID, "golden_poop_block"),
            new GoldenPoopBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.METAL)
                    .strength(3.0f, 6.0f)
                    .nonOpaque()
                    .requiresTool()
            )
    );

    public static final Block FOOT_BLOCK = Registry.register(
            Registries.BLOCK,
            Identifier.of(TokimisRuinedMod.MOD_ID, "foot_block"),
            new FootBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.MOSS_CARPET)
                    .strength(0.3f)
                    .nonOpaque()
            )
    );

    public static final Block POOP_JAR = Registry.register(
            Registries.BLOCK,
            Identifier.of(TokimisRuinedMod.MOD_ID, "poop_jar"),
            new PoopJarBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .strength(0.3f)
                    .nonOpaque()
            )
    );

    public static void register() {
        TokimisRuinedMod.LOGGER.info("Registering blocks");
    }
}
