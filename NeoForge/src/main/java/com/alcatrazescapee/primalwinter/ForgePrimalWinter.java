package com.alcatrazescapee.primalwinter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ClimateSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.alcatrazescapee.primalwinter.platform.XPlatform;
import com.alcatrazescapee.primalwinter.util.Config;
import com.alcatrazescapee.primalwinter.util.EventHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(PrimalWinter.MOD_ID)
public final class ForgePrimalWinter
{
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, PrimalWinter.MOD_ID);
    public static final RegistryObject<Codec<? extends Instance>> CODEC = BIOME_MODIFIERS.register("instance", () -> RecordCodecBuilder.create(instance -> instance.group(
        PlacedFeature.LIST_CODEC.fieldOf("surface_structures").forGetter(c -> c.surfaceStructures),
        PlacedFeature.LIST_CODEC.fieldOf("top_layer_modification").forGetter(c -> c.topLayerModification)
    ).apply(instance, Instance::new)));

    public ForgePrimalWinter()
    {
        PrimalWinter.earlySetup();

        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        modBus.addListener((FMLCommonSetupEvent event) -> PrimalWinter.lateSetup());
        BIOME_MODIFIERS.register(modBus);

        forgeBus.addListener((RegisterCommandsEvent event) -> EventHandler.registerCommands(event.getDispatcher()));
        forgeBus.addListener((LevelEvent.Load event) -> EventHandler.setLevelToThunder(event.getLevel()));

        if (XPlatform.INSTANCE.isDedicatedClient())
        {
            ForgePrimalWinterClient.setupClient();
        }
    }

    record Instance(HolderSet<PlacedFeature> surfaceStructures, HolderSet<PlacedFeature> topLayerModification) implements BiomeModifier
    {
        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
        {
            if (biome.unwrapKey().map(k -> !Config.INSTANCE.isWinterBiome(k.location())).orElse(true) || phase != Phase.MODIFY)
            {
                return;
            }

            final ClimateSettingsBuilder climate = builder.getClimateSettings();
            climate.setHasPrecipitation(true);
            climate.setTemperature(-0.5f);
            climate.setTemperatureModifier(Biome.TemperatureModifier.NONE);

            builder.getSpecialEffects()
                .waterColor(0x3938C9)
                .waterFogColor(0x050533);

            final BiomeGenerationSettingsBuilder settings = builder.getGenerationSettings();
            for (Holder<PlacedFeature> feature : surfaceStructures)
            {
                settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, feature);
            }

            settings.getFeatures(GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
                .removeIf(holder -> holder.unwrapKey().map(key -> key == MiscOverworldPlacements.FREEZE_TOP_LAYER).orElse(false));
            for (Holder<PlacedFeature> feature : topLayerModification)
            {
                settings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, feature);
            }

            final MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
            spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 60, 1, 3));
            spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.STRAY, 60, 1, 3));
        }

        @Override
        public Codec<? extends BiomeModifier> codec()
        {
            return CODEC.get();
        }
    }
}
