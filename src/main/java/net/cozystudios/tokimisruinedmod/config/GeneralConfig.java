package net.cozystudios.tokimisruinedmod.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "general")
public class GeneralConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    public ParticleOptions particles = new ParticleOptions();

    @ConfigEntry.Gui.CollapsibleObject
    public CombatOptions combat = new CombatOptions();

    @ConfigEntry.Gui.CollapsibleObject
    public EffectOptions effects = new EffectOptions();

    @ConfigEntry.Gui.CollapsibleObject
    public SoundOptions sounds = new SoundOptions();

    @ConfigEntry.Gui.CollapsibleObject
    public EnchantmentOptions enchantments = new EnchantmentOptions();

    public static class ParticleOptions {
        @ConfigEntry.Gui.Tooltip
        public boolean flyParticlesOnPoop = true;

        @ConfigEntry.Gui.Tooltip
        public boolean flyParticlesInJar = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public SpawningOptions spawning = new SpawningOptions();

    @ConfigEntry.Gui.CollapsibleObject
    public MushlingOptions mushling = new MushlingOptions();

    @ConfigEntry.Gui.CollapsibleObject
    public PigOptions pigs = new PigOptions();

    @ConfigEntry.Gui.CollapsibleObject
    public MobOptions mobs = new MobOptions();

    public static class SpawningOptions {
        @ConfigEntry.Gui.Tooltip
        public boolean footSpawningEnabled = true;
    }

    public static class MushlingOptions {
        @ConfigEntry.Gui.Tooltip
        public boolean mushlingSpawningEnabled = true;
    }

    public static class PigOptions {
        @ConfigEntry.Gui.Tooltip
        public boolean flyingPigsEnabled = true;

        @ConfigEntry.Gui.Tooltip
        public boolean explodingPigsEnabled = true;

        @ConfigEntry.Gui.Tooltip
        public boolean flyExplodePigsEnabled = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int flyingPigChancePercent = 25;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int explodingPigChancePercent = 25;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int flyExplodePigChancePercent = 15;
    }

    public static class MobOptions {
        @ConfigEntry.Gui.Tooltip
        public boolean creeperDoorOpeningEnabled = true;
    }

    public static class CombatOptions {
        @ConfigEntry.Gui.Tooltip
        public boolean goldenPoopInstaKill = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public int goldenPoopDamageHearts = 6;

        @ConfigEntry.Gui.Tooltip
        public float poopThrowDamageHearts = 2.5f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int poopExplosiveChancePercent = 20;

        @ConfigEntry.Gui.Tooltip
        public boolean poopEatingKills = true;
    }

    public static class EffectOptions {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 120)
        public int poopyFeetDurationSeconds = 30;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
        public int nauseaDurationSeconds = 10;

        @ConfigEntry.Gui.Tooltip
        public boolean poopOverlayEnabled = true;
    }

    public static class SoundOptions {
        @ConfigEntry.Gui.Tooltip
        public boolean fartSoundEnabled = true;
    }

    public static class EnchantmentOptions {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public int shartedLevel1ChancePercent = 10;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public int shartedLevel2ChancePercent = 15;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public int shartedLevel3ChancePercent = 25;
    }

    public static GeneralConfig get() {
        return ModConfig.get().generalConfig;
    }
}
