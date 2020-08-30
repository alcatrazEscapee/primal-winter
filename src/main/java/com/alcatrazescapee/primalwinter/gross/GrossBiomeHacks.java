/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.gross;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.StructureFeatures;

import com.alcatrazescapee.primalwinter.Config;
import com.alcatrazescapee.primalwinter.mixin.world.biome.*;
import com.alcatrazescapee.primalwinter.world.ModConfiguredFeatures;

public class GrossBiomeHacks
{
    public static void modifyBiomes(Registry<Biome> registry)
    {
        // Manually editing biomes... so gross
        if (Config.COMMON.enableGrossBiomeHacks.get())
        {
            Set<ResourceLocation> nonWinterBiomeKeys = Config.COMMON.nonWinterBiomes.get().stream().map(ResourceLocation::new).collect(Collectors.toSet());
            registry.getEntries().forEach(entry -> {
                ResourceLocation biomeKey = entry.getKey().getValue();
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
        BiomeAccess biomeAccess = (BiomeAccess) (Object) biome;
        BiomeClimateAccess weatherAccess = (BiomeClimateAccess) biomeAccess.getWeather();
        weatherAccess.setTemperature(-0.5f);
        weatherAccess.setPrecipitation(Biome.RainType.SNOW);
        weatherAccess.setTemperatureModifier(Biome.TemperatureModifier.NONE); // The frozen modifier has large >0.15 zones which result in non-ztormy behavior... bad!

        // Modify biome effects
        BiomeAmbianceAccess effectsAccess = (BiomeAmbianceAccess) biome.getEffects();
        effectsAccess.setWaterColor(0x3938C9);
        effectsAccess.setFogWaterColor(0x050533);

        // Modify spawn settings
        MobSpawnInfoAccess spawnSettingsAccess = (MobSpawnInfoAccess) biome.getSpawnSettings();

        // Need to hack in mutability to this map first
        Map<EntityClassification, List<MobSpawnInfo.Spawners>> spawners = new HashMap<>(spawnSettingsAccess.getSpawners());
        spawnSettingsAccess.setSpawners(spawners);

        // Winter mobs
        // Map is already mutable, need to mutate each list as well
        List<MobSpawnInfo.Spawners> monsterEntries = mutableList(spawners.get(EntityClassification.MONSTER));
        spawners.put(EntityClassification.MONSTER, monsterEntries);

        monsterEntries.add(new MobSpawnInfo.Spawners(EntityType.STRAY, 320, 4, 4));

        List<MobSpawnInfo.Spawners> creatureEntries = mutableList(spawners.get(EntityClassification.CREATURE));
        spawners.put(EntityClassification.CREATURE, creatureEntries);

        creatureEntries.add(new MobSpawnInfo.Spawners(EntityType.POLAR_BEAR, 4, 1, 2));
        creatureEntries.add(new MobSpawnInfo.Spawners(EntityType.SNOW_GOLEM, 4, 4, 8));

        // Need to mutate the list, and also every entry
        BiomeGenerationSettingsAccess generationSettingsAccess = (BiomeGenerationSettingsAccess) biome.getGenerationSettings();
        List<List<Supplier<ConfiguredFeature<?, ?>>>> features = mutableListList(biome.getGenerationSettings().getFeatures());
        generationSettingsAccess.setFeatures(features);
        fillList(features, GenerationStage.Decoration.SURFACE_STRUCTURES.ordinal(), ArrayList::new);

        // Ice spikes (although less frequent)
        // todo: figure out how tf forge plans to handle this
        features.get(GenerationStage.Decoration.SURFACE_STRUCTURES.ordinal()).add(() -> ModConfiguredFeatures.RARE_ICE_PATCH);
        features.get(GenerationStage.Decoration.SURFACE_STRUCTURES.ordinal()).add(() -> ModConfiguredFeatures.RARE_ICE_SPIKES);

        // Need to mutate the list
        List<Supplier<StructureFeature<?, ?>>> structureFeatures = mutableList(biome.getGenerationSettings().getStructureFeatures());
        generationSettingsAccess.setStructureFeatures(structureFeatures);

        // Igloos
        structureFeatures.add(() -> StructureFeatures.IGLOO);
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
