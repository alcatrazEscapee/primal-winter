package com.alcatrazescapee.primalwinter;

import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import com.alcatrazescapee.primalwinter.util.Helpers;
import com.alcatrazescapee.primalwinter.world.PrimalWinterWorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public final class FabricPrimalWinter implements ModInitializer {

    @Override
    public void onInitialize()
    {
        PrimalWinter.earlySetup();
        PrimalWinter.lateSetup();

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> Helpers.registerCommands(dispatcher));

        BiomeModifications.create(Helpers.identifier("winterize")).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.foundInOverworld(), context -> {
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
            settings.removeFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MiscOverworldPlacements.FREEZE_TOP_LAYER.unwrapKey().orElseThrow());
            settings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PrimalWinterWorldGen.Placed.FREEZE_TOP_LAYER.key());
        });
    }
}
