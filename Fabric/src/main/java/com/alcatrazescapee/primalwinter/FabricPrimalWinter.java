package com.alcatrazescapee.primalwinter;

import java.util.function.Consumer;
import com.alcatrazescapee.primalwinter.platform.FabricConfig;
import com.alcatrazescapee.primalwinter.platform.NetworkSetupCallback;
import com.alcatrazescapee.primalwinter.platform.XPlatform;
import com.alcatrazescapee.primalwinter.util.EventHandler;
import com.alcatrazescapee.primalwinter.util.Helpers;
import com.alcatrazescapee.primalwinter.world.PrimalWinterFeatures;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;

public final class FabricPrimalWinter implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        PrimalWinter.earlySetup();
        PrimalWinter.lateSetup();
        ((FabricConfig) XPlatform.INSTANCE.config()).load();
        PrimalWinter.networkingSetup(new NetworkSetupCallback() {
            @Override
            public <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<ByteBuf, T> codec, Consumer<T> handler)
            {
                PayloadTypeRegistry.playS2C().register(type, codec);
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> EventHandler.registerCommands(dispatcher));
        ServerWorldEvents.LOAD.register((server, level) -> EventHandler.setLevelToThunder(level));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> EventHandler.onPlayerJoinWorld(handler.player));

        BiomeModifications.create(Helpers.identifier("winterize")).add(
            ModificationPhase.REPLACEMENTS,
            context -> XPlatform.INSTANCE.config().isWinterBiome(context.getBiomeKey()),
            context -> {
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

                PrimalWinterFeatures.addSpawns(context.getSpawnSettings()::addSpawn);
            });
    }
}
