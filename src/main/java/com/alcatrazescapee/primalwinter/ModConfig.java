/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter;

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
        //formatter:off
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
        // todo: do something about this nonsense
        /*
        @RequiresRestart public String nonWinterBiomes = Stream.of(
            Biomes.THE_END, Biomes.SMALL_END_ISLANDS, Biomes.END_BARRENS, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS,
            Biomes.NETHER_WASTES, Biomes.SOUL_SAND_VALLEY, Biomes.CRIMSON_FOREST, Biomes.WARPED_FOREST, Biomes.BASALT_DELTAS
        ).map(biome -> Registry.BIOME.getId(biome).toString()).collect(Collectors.joining(","));
        */
        //formatter:on
    }
}
