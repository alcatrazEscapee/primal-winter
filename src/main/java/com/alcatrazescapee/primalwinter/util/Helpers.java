/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.util;

import java.util.ArrayList;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.level.ServerWorldProperties;

import com.alcatrazescapee.primalwinter.mixin.world.IBiome;
import com.alcatrazescapee.primalwinter.mixin.world.IBiomeEffects;
import com.alcatrazescapee.primalwinter.world.ModFeatures;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;

public final class Helpers
{
    /**
     * Make egregious modifications to biomes
     */
    public static void hackWinterBiomes()
    {
        // todo: config whitelist?
        Registry.BIOME.forEach(Helpers::hackWinterBiome);
        RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> hackWinterBiome(biome));
    }

    /**
     * Sets the weather of a world to an endless winter state
     */
    public static void setWeatherToEndlessWinter(ServerWorld world)
    {
        if (world.getLevelProperties() instanceof ServerWorldProperties)
        {
            ServerWorldProperties properties = ((ServerWorldProperties) world.getLevelProperties());
            world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, world.getServer());
            properties.setClearWeatherTime(0);
            properties.setRainTime(Integer.MAX_VALUE);
            properties.setThunderTime(Integer.MAX_VALUE);
            properties.setRaining(true);
            properties.setThundering(true);
        }
    }

    /**
     * Called from {@link ServerWorld#tickChunk(WorldChunk, int)} via mixin, places additional snow layers
     */
    public static void placeExtraSnowOnTickChunk(ServerWorld world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        if (world.isRaining() && state.getBlock() == Blocks.SNOW && state.get(Properties.LAYERS) < 5)
        {
            world.setBlockState(pos, state.with(Properties.LAYERS, state.get(Properties.LAYERS) + 1));
        }
    }

    public static BlockState copyProperties(BlockState oldState, BlockState newState)
    {
        for (Property<?> property : oldState.getProperties())
        {
            if (newState.getProperties().contains(property))
            {
                newState = copyProperty(property, oldState, newState);
            }
        }
        return newState;
    }

    /**
     * This gets around both arguments being a {@code IProperty<?>}
     */
    public static <T extends Comparable<T>> BlockState copyProperty(Property<T> property, BlockState original, BlockState replacement)
    {
        return replacement.with(property, original.get(property));
    }

    private static void hackWinterBiome(Biome biome)
    {
        // Everything is winter now
        IBiome access = (IBiome) biome;
        access.primalwinter_setTemperature(-0.5f);
        access.primalwinter_setPrecipitation(Biome.Precipitation.SNOW);

        IBiomeEffects effects = (IBiomeEffects) biome.getEffects();
        effects.primalwinter_setWaterColor(0x3938C9);
        effects.primalwinter_setFogWaterColor(0x050533);

        // Winter mobs
        access.primalwinter_getSpawns().computeIfAbsent(SpawnGroup.MONSTER, key -> new ArrayList<>()).add(new Biome.SpawnEntry(EntityType.STRAY, 320, 4, 4));
        access.primalwinter_getSpawns().computeIfAbsent(SpawnGroup.CREATURE, key -> new ArrayList<>()).add(new Biome.SpawnEntry(EntityType.POLAR_BEAR, 4, 1, 2));
        access.primalwinter_getSpawns().computeIfAbsent(SpawnGroup.CREATURE, key -> new ArrayList<>()).add(new Biome.SpawnEntry(EntityType.SNOW_GOLEM, 4, 4, 8));

        // Freeze a bit more than just the top layer
        // Remove the original feature because it sucks
        biome.getFeaturesForStep(GenerationStep.Feature.TOP_LAYER_MODIFICATION).removeIf(feature -> feature.config instanceof DecoratedFeatureConfig && ((DecoratedFeatureConfig) feature.config).feature.feature instanceof FreezeTopLayerFeature);
        biome.addFeature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, ModFeatures.FREEZE_EVERYTHING.configure(DefaultFeatureConfig.DEFAULT).createDecoratedFeature(Decorator.NOPE.configure(NopeDecoratorConfig.DEFAULT)));

        // Ice spikes (although less frequent)
        biome.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Feature.ICE_SPIKE.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP.configure(new ChanceDecoratorConfig(7))));
        biome.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Feature.ICE_PATCH.configure(new IcePatchFeatureConfig(2)).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP.configure(new ChanceDecoratorConfig(5))));

        // Igloos
        biome.addStructureFeature(StructureFeature.IGLOO.configure(DefaultFeatureConfig.INSTANCE));
    }
}
