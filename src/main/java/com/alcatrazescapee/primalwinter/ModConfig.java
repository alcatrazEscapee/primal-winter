/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.world.biome.BuiltinBiomes;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.BoundedDiscrete;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.ColorPicker;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.RequiresRestart;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;

public class ModConfig
{
    public static final ModConfigData INSTANCE;

    static
    {
        AutoConfig.register(ModConfigData.class, Toml4jConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ModConfigData.class).getConfig();
    }

    static void init() {}

    @Config(name = PrimalWinter.MOD_ID)
    public static class ModConfigData implements ConfigData
    {
        public double fogDensity = 0.1;
        @BoundedDiscrete(min = 1, max = 15)
        public int snowParticleDensity = 15;

        public boolean enableSnowSounds = true;
        public boolean enableWindSounds = true;
        public boolean enableWeatherRenderChanges = true;
        public boolean enableSkyRenderChanges = true;
        public boolean enableVanillaWeatherCommand = false;

        @RequiresRestart
        public boolean enableGrossBiomeHacks = true;

        @ColorPicker
        public int fogColorDay = 0xbfbfd8;
        @ColorPicker
        public int fogColorNight = 0x0c0c19;

        @RequiresRestart
        public String nonWinterBiomes = Stream.of(
            BuiltinBiomes.THE_END, BuiltinBiomes.SMALL_END_ISLANDS, BuiltinBiomes.END_BARRENS, BuiltinBiomes.END_HIGHLANDS, BuiltinBiomes.END_MIDLANDS,
            BuiltinBiomes.NETHER_WASTES, BuiltinBiomes.SOUL_SAND_VALLEY, BuiltinBiomes.CRIMSON_FOREST, BuiltinBiomes.WARPED_FOREST, BuiltinBiomes.BASALT_DELTAS
        ).map(key -> key.getValue().toString()).collect(Collectors.joining(","));
    }
}
