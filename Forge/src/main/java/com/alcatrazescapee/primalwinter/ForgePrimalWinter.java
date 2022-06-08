package com.alcatrazescapee.primalwinter;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.alcatrazescapee.primalwinter.platform.XPlatform;
import com.alcatrazescapee.primalwinter.util.Helpers;
import com.alcatrazescapee.primalwinter.world.PrimalWinterWorldGen;

@Mod(PrimalWinter.MOD_ID)
public final class ForgePrimalWinter
{
    public ForgePrimalWinter()
    {
        PrimalWinter.earlySetup();

        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        modBus.addListener((FMLCommonSetupEvent event) -> PrimalWinter.lateSetup());

        forgeBus.addListener((RegisterCommandsEvent event) -> Helpers.registerCommands(event.getDispatcher()));
        forgeBus.addListener((WorldEvent.Load event) -> Helpers.setLevelToThunder(event.getWorld()));
        forgeBus.addListener(this::modifyBiomes);

        if (XPlatform.INSTANCE.isDedicatedClient())
        {
            ForgePrimalWinterClient.earlySetup();
        }
    }

    private void modifyBiomes(BiomeLoadingEvent event)
    {
        if (!Helpers.isWinterBiome(event.getName()))
        {
            return;
        }

        event.setClimate(new Biome.ClimateSettings(Biome.Precipitation.SNOW, -0.5f, Biome.TemperatureModifier.NONE, event.getClimate().downfall));

        final BiomeSpecialEffects effects = event.getEffects();
        final BiomeSpecialEffects.Builder newEffects = new BiomeSpecialEffects.Builder()
            .waterColor(0x3938C9)
            .waterFogColor(0x050533)
            .fogColor(effects.getFogColor())
            .skyColor(effects.getSkyColor())
            .grassColorModifier(effects.getGrassColorModifier());
        effects.getFoliageColorOverride().ifPresent(newEffects::foliageColorOverride);
        effects.getGrassColorOverride().ifPresent(newEffects::grassColorOverride);
        effects.getAmbientParticleSettings().ifPresent(newEffects::ambientParticle);
        effects.getAmbientLoopSoundEvent().ifPresent(newEffects::ambientLoopSound);
        effects.getAmbientMoodSettings().ifPresent(newEffects::ambientMoodSound);
        effects.getAmbientAdditionsSettings().ifPresent(newEffects::ambientAdditionsSound);
        effects.getBackgroundMusic().ifPresent(newEffects::backgroundMusic);
        event.setEffects(newEffects.build());

        final BiomeGenerationSettingsBuilder settings = event.getGeneration();

        settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PrimalWinterWorldGen.Placed.ICE_SPIKES.holder());
        settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PrimalWinterWorldGen.Placed.ICE_PATCH.holder());
        settings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PrimalWinterWorldGen.Placed.FREEZE_TOP_LAYER.holder());
    }
}
