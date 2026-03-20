package net.cozystudios.tokimisruinedmod;

import net.cozystudios.tokimisruinedmod.config.ModConfig;
import net.cozystudios.tokimisruinedmod.entity.FootEntity;
import net.cozystudios.tokimisruinedmod.entity.MushlingEntity;
import net.cozystudios.tokimisruinedmod.event.FootSpawnHandler;
import net.cozystudios.tokimisruinedmod.event.PlayerShearHandler;
import net.cozystudios.tokimisruinedmod.event.WoodenSwordAttackHandler;
import net.cozystudios.tokimisruinedmod.network.PoopOverlayPayload;
import net.cozystudios.tokimisruinedmod.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.cozystudios.tokimisruinedmod.config.GeneralConfig;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemGroups;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokimisRuinedMod implements ModInitializer {
    public static final String MOD_ID = "tokimis_ruined_mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Tokimi's Ruined Mod Initialized");

        ModConfig.register();

        ModBlocks.register();
        ModItems.register();
        ModSounds.register();
        ModEntities.register();
        ModParticles.register();
        ModStatusEffects.register();
        ModEnchantments.register();
        ModItemGroup.register();

        FabricDefaultAttributeRegistry.register(ModEntities.FOOT, FootEntity.createFootAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.MUSHLING, MushlingEntity.createMushlingAttributes());

        SpawnRestriction.register(ModEntities.MUSHLING,
                SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                (entity, world, reason, pos, random) -> MobEntity.canMobSpawn(entity, world, reason, pos, random));

        BiomeModifications.addSpawn(
                context -> GeneralConfig.get().mushling.mushlingSpawningEnabled &&
                        BiomeSelectors.includeByKey(
                                BiomeKeys.FOREST,
                                BiomeKeys.FLOWER_FOREST,
                                BiomeKeys.BIRCH_FOREST,
                                BiomeKeys.OLD_GROWTH_BIRCH_FOREST,
                                BiomeKeys.DARK_FOREST,
                                BiomeKeys.MUSHROOM_FIELDS
                        ).test(context),
                SpawnGroup.CREATURE,
                ModEntities.MUSHLING,
                4,
                1,
                3
        );

        PayloadTypeRegistry.playS2C().register(PoopOverlayPayload.ID, PoopOverlayPayload.CODEC);

        AttackEntityCallback.EVENT.register(WoodenSwordAttackHandler::onAttack);
        UseEntityCallback.EVENT.register(PlayerShearHandler::onInteract);
        UseItemCallback.EVENT.register(PlayerShearHandler::onSelfShear);
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                FootSpawnHandler.tick(serverWorld);
            }
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(ModItems.FOOT_SPAWN_EGG);
        });

        CompostingChanceRegistry.INSTANCE.add(ModItems.POOP_ITEM, 7.0f / 12.0f);
    }
}
