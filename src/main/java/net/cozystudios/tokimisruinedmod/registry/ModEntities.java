package net.cozystudios.tokimisruinedmod.registry;

import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.cozystudios.tokimisruinedmod.entity.FootEntity;
import net.cozystudios.tokimisruinedmod.entity.MushlingEntity;
import net.cozystudios.tokimisruinedmod.entity.ThrownGoldenPoopEntity;
import net.cozystudios.tokimisruinedmod.entity.ThrownPoopEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<ThrownPoopEntity> THROWN_POOP = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TokimisRuinedMod.MOD_ID, "thrown_poop"),
            EntityType.Builder.<ThrownPoopEntity>create(ThrownPoopEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25f, 0.25f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
                    .build()
    );

    public static final EntityType<ThrownGoldenPoopEntity> THROWN_GOLDEN_POOP = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TokimisRuinedMod.MOD_ID, "thrown_golden_poop"),
            EntityType.Builder.<ThrownGoldenPoopEntity>create(ThrownGoldenPoopEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25f, 0.25f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
                    .build()
    );

    public static final EntityType<FootEntity> FOOT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TokimisRuinedMod.MOD_ID, "foot"),
            EntityType.Builder.create(FootEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.5f, 0.3f)
                    .maxTrackingRange(8)
                    .build()
    );

    public static final EntityType<MushlingEntity> MUSHLING = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TokimisRuinedMod.MOD_ID, "mushling"),
            EntityType.Builder.create(MushlingEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.6f, 0.9f)
                    .maxTrackingRange(8)
                    .build()
    );

    public static void register() {
        TokimisRuinedMod.LOGGER.info("Registering entities");
    }
}
