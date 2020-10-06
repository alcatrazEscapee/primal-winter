/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter;

import net.minecraft.command.Commands;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.alcatrazescapee.primalwinter.mixin.world.biome.BiomeAmbienceAccessor;
import com.alcatrazescapee.primalwinter.world.ModConfiguredFeatures;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEventHandler
{
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event)
    {
        if (Config.COMMON.disableWeatherCommand.get())
        {
            // Vanilla weather command... NOT ALLOWED
            event.getDispatcher().getRoot().getChildren().removeIf(node -> node.getName().equals("weather"));
            event.getDispatcher().register(Commands.literal("weather").executes(source -> {
                source.getSource().sendSuccess(new StringTextComponent("Not even a command can overcome this storm... (This command is disabled by Primal Winter)"), false);
                return 0;
            }));
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event)
    {
        // todo: check dimension == overworld
        if (event.getWorld() instanceof ServerWorld)
        {
            ServerWorld world = (ServerWorld) event.getWorld();
            world.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, world.getServer());
            world.setWeatherParameters(0, Integer.MAX_VALUE, true, true);
        }
    }

    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event)
    {
        if (Config.COMMON.nonWinterBiomes.get().stream().noneMatch(id -> id.equals(event.getName() == null ? "" : event.getName().toString())))
        {
            // This requires a mixin because forge hasn't exposed any mutators, a constructor, or a builder...
            event.setClimate(new Biome.Climate(Biome.RainType.SNOW, -0.5f, Biome.TemperatureModifier.NONE, event.getClimate().downfall));

            // Modify effects
            BiomeAmbienceAccessor effectsAccess = (BiomeAmbienceAccessor) event.getEffects();
            effectsAccess.accessor$setWaterColor(0x3938C9);
            effectsAccess.accessor$setFogWaterColor(0x050533);

            // Modify spawn settings
            MobSpawnInfoBuilder spawnSettingsBuilder = event.getSpawns();
            spawnSettingsBuilder.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.STRAY, 320, 4, 4));
            spawnSettingsBuilder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.POLAR_BEAR, 4, 1, 2));
            spawnSettingsBuilder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.SNOW_GOLEM, 4, 4, 8));

            // Add features and structures
            BiomeGenerationSettingsBuilder generationSettingsBuilder = event.getGeneration();
            generationSettingsBuilder.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, ModConfiguredFeatures.ICE_SPIKES);
            generationSettingsBuilder.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, ModConfiguredFeatures.ICE_PATCH);
            generationSettingsBuilder.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, ModConfiguredFeatures.FREEZE_TOP_LAYER);

            generationSettingsBuilder.addStructureStart(StructureFeatures.IGLOO);

            // Removals need to access the underlying list for now
            generationSettingsBuilder.getFeatures(GenerationStage.Decoration.TOP_LAYER_MODIFICATION).removeIf(feature -> feature.get() == Features.FREEZE_TOP_LAYER);
        }
    }
}