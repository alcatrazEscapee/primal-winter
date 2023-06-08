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
import com.alcatrazescapee.primalwinter.world.PrimalWinterFeatures;
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
            weather.setPrecipitation(true);
            weather.setTemperatureModifier(Biome.TemperatureModifier.NONE);

            final BiomeModificationContext.EffectsContext effects = context.getEffects();
            effects.setWaterColor(0x3938C9);
            effects.setWaterFogColor(0x050533);

            final BiomeModificationContext.GenerationSettingsContext settings = context.getGenerationSettings();
            settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PrimalWinterFeatures.Keys.ICE_SPIKES);
            settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PrimalWinterFeatures.Keys.ICE_PATCH);
            settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PrimalWinterFeatures.Keys.SNOW_PATCH);
            settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PrimalWinterFeatures.Keys.POWDER_SNOW_PATCH);
            settings.removeFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MiscOverworldPlacements.FREEZE_TOP_LAYER);
            settings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PrimalWinterFeatures.Keys.FREEZE_TOP_LAYER);

            final BiomeModificationContext.SpawnSettingsContext spawns = context.getSpawnSettings();
            spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 60, 1, 3));
            spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.STRAY, 60, 1, 3));
        });
    }
}
