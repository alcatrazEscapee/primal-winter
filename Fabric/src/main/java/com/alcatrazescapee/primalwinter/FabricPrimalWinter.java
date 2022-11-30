package com.alcatrazescapee.primalwinter;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.GenerationStep;

import com.alcatrazescapee.primalwinter.util.Config;
import com.alcatrazescapee.primalwinter.util.EventHandler;
import com.alcatrazescapee.primalwinter.util.Helpers;
import com.alcatrazescapee.primalwinter.world.PrimalWinterWorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class FabricPrimalWinter implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        PrimalWinter.earlySetup();
        PrimalWinter.lateSetup();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> EventHandler.registerCommands(dispatcher));
        ServerWorldEvents.LOAD.register((server, level) -> EventHandler.setLevelToThunder(level));

        BiomeModifications.create(Helpers.identifier("winterize")).add(ModificationPhase.REPLACEMENTS, context -> {
            final ResourceLocation id = context.getBiomeKey().location();
            return context.canGenerateIn(LevelStem.OVERWORLD) && Config.INSTANCE.isWinterBiome(id);
        }, context -> {
            final BiomeModificationContext.WeatherContext weather = context.getWeather();
            weather.setTemperature(-0.5f);
            weather.setPrecipitation(Biome.Precipitation.SNOW);
            weather.setTemperatureModifier(Biome.TemperatureModifier.NONE);

            final BiomeModificationContext.EffectsContext effects = context.getEffects();
            effects.setWaterColor(0x3938C9);
            effects.setWaterFogColor(0x050533);

            final BiomeModificationContext.GenerationSettingsContext settings = context.getGenerationSettings();
            settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PrimalWinterWorldGen.Placed.ICE_SPIKES.key());
            settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PrimalWinterWorldGen.Placed.ICE_PATCH.key());
            settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PrimalWinterWorldGen.Placed.SNOW_PATCH.key());
            settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PrimalWinterWorldGen.Placed.POWDER_SNOW_PATCH.key());
            settings.removeFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MiscOverworldPlacements.FREEZE_TOP_LAYER.unwrapKey().orElseThrow());
            settings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PrimalWinterWorldGen.Placed.FREEZE_TOP_LAYER.key());

            final BiomeModificationContext.SpawnSettingsContext spawns = context.getSpawnSettings();
            spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 60, 1, 3));
            spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.STRAY, 60, 1, 3));
        });
    }
}
