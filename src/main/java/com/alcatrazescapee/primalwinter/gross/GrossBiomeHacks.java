/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.gross;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;

import com.alcatrazescapee.primalwinter.ModConfig;
import com.alcatrazescapee.primalwinter.mixin.world.biome.*;
import com.alcatrazescapee.primalwinter.world.ModConfiguredFeatures;

public class GrossBiomeHacks
{
    public static void modifyBiomes(Registry<Biome> registry)
    {
        // Manually editing biomes... so gross
        if (ModConfig.INSTANCE.enableGrossBiomeHacks)
        {
            Set<Identifier> nonWinterBiomeKeys = Arrays.stream(ModConfig.INSTANCE.nonWinterBiomes.split(",")).map(Identifier::new).collect(Collectors.toSet());
            registry.getEntries().forEach(entry -> {
                Identifier biomeKey = entry.getKey().getValue();
                if (!nonWinterBiomeKeys.contains(biomeKey))
                {
                    modifyBiome(entry.getValue());
                }
            });
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void modifyBiome(Biome biome)
    {
        // Modify biome properties through IBiome
        IBiome biomeAccess = (IBiome) (Object) biome;
        IBiomeWeather weatherAccess = (IBiomeWeather) biomeAccess.getWeather();
        weatherAccess.setTemperature(-0.5f);
        weatherAccess.setPrecipitation(Biome.Precipitation.SNOW);
        weatherAccess.setTemperatureModifier(Biome.TemperatureModifier.NONE); // The frozen modifier has large >0.15 zones which result in non-ztormy behavior... bad!

        // Modify biome effects
        IBiomeEffects effectsAccess = (IBiomeEffects) biome.getEffects();
        effectsAccess.setWaterColor(0x3938C9);
        effectsAccess.setFogWaterColor(0x050533);

        // Modify spawn settings
        ISpawnSettings spawnSettingsAccess = (ISpawnSettings) biome.getSpawnSettings();

        // Need to hack in mutability to this map first
        Map<SpawnGroup, List<SpawnSettings.SpawnEntry>> spawners = new HashMap<>(spawnSettingsAccess.getSpawners());
        spawnSettingsAccess.setSpawners(spawners);

        // Winter mobs
        // Map is already mutable, need to mutate each list as well
        List<SpawnSettings.SpawnEntry> monsterEntries = mutableList(spawners.get(SpawnGroup.MONSTER));
        spawners.put(SpawnGroup.MONSTER, monsterEntries);

        monsterEntries.add(new SpawnSettings.SpawnEntry(EntityType.STRAY, 320, 4, 4));

        List<SpawnSettings.SpawnEntry> creatureEntries = mutableList(spawners.get(SpawnGroup.CREATURE));
        spawners.put(SpawnGroup.CREATURE, creatureEntries);

        creatureEntries.add(new SpawnSettings.SpawnEntry(EntityType.POLAR_BEAR, 4, 1, 2));
        creatureEntries.add(new SpawnSettings.SpawnEntry(EntityType.SNOW_GOLEM, 4, 4, 8));

        // Need to mutate the list, and also every entry
        IGenerationSettings generationSettingsAccess = (IGenerationSettings) biome.getGenerationSettings();
        List<List<Supplier<ConfiguredFeature<?, ?>>>> features = mutableListList(biome.getGenerationSettings().getFeatures());
        generationSettingsAccess.setFeatures(features);
        fillList(features, GenerationStep.Feature.SURFACE_STRUCTURES.ordinal(), ArrayList::new);

        // Ice spikes (although less frequent)
        features.get(GenerationStep.Feature.SURFACE_STRUCTURES.ordinal()).add(() -> ModConfiguredFeatures.RARE_ICE_PATCH);
        features.get(GenerationStep.Feature.SURFACE_STRUCTURES.ordinal()).add(() -> ModConfiguredFeatures.RARE_ICE_SPIKES);

        // Need to mutate the list
        List<Supplier<ConfiguredStructureFeature<?, ?>>> structureFeatures = mutableList(biome.getGenerationSettings().getStructureFeatures());
        generationSettingsAccess.setStructureFeatures(structureFeatures);

        // Igloos
        structureFeatures.add(() -> ConfiguredStructureFeatures.IGLOO);
    }

    private static <T> void fillList(List<T> list, int index, Supplier<T> elementFactory)
    {
        while (list.size() <= index)
        {
            list.add(elementFactory.get());
        }
    }

    private static <T> List<T> mutableList(Collection<? extends T> collection)
    {
        return collection != null ? new ArrayList<>(collection) : new ArrayList<>();
    }

    private static <T> List<List<T>> mutableListList(Collection<? extends Collection<? extends T>> collection)
    {
        return collection != null ? collection.stream().map((Function<Collection<? extends T>, List<T>>) GrossBiomeHacks::mutableList).collect(Collectors.toList()) : new ArrayList<>();
    }
}
