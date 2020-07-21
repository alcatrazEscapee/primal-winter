/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */


package com.alcatrazescapee.primalwinter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;

@Config(name = PrimalWinter.MOD_ID)
public class ModConfig implements ConfigData
{
    public static final ModConfig INSTANCE;

    static
    {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    static void init() {}

    public double fogDensity = 0.1;
    public int snowParticleDensity = 15;
    public boolean enableSnowSounds = true;
    public boolean enableWindSounds = true;
    public boolean enableWeatherRenderChanges = true;
    public boolean enableSkyRenderChanges = true;
    public int fogColorDay = 0xbfbfd8;
    public int fogColorNight = 0x0c0c19;
    public String nonWinterBiomes = Stream.of(
        Biomes.THE_END, Biomes.SMALL_END_ISLANDS, Biomes.END_BARRENS, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS,
        Biomes.NETHER_WASTES, Biomes.SOUL_SAND_VALLEY, Biomes.CRIMSON_FOREST, Biomes.WARPED_FOREST, Biomes.BASALT_DELTAS
    ).map(biome -> Registry.BIOME.getId(biome).toString()).collect(Collectors.joining(","));
}
