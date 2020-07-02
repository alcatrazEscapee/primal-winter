/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.primalwinter.client.ModParticleTypes;
import com.alcatrazescapee.primalwinter.client.ModSoundEvents;
import com.alcatrazescapee.primalwinter.common.ModBlocks;
import com.alcatrazescapee.primalwinter.common.ModItems;
import com.alcatrazescapee.primalwinter.util.Helpers;
import com.alcatrazescapee.primalwinter.world.BlockReplacingConfiguredFeature;
import com.alcatrazescapee.primalwinter.world.BlockReplacingWorld;
import com.alcatrazescapee.primalwinter.world.ModFeatures;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

@Mod(MOD_ID)
public final class PrimalWinter
{
    public static final String MOD_ID = "primalwinter";

    private static final Logger LOGGER = LogManager.getLogger();

    public PrimalWinter()
    {
        LOGGER.debug("Constructing");

        // Setup config
        Config.init();

        // Register event handlers
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.register(this);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);

        ModFeatures.FEATURES.register(modEventBus);

        ModSoundEvents.SOUND_EVENTS.register(modEventBus);
        ModParticleTypes.PARTICLE_TYPES.register(modEventBus);
    }

    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.debug("Setup");

        DeferredWorkQueue.runLater(() -> ForgeRegistries.BIOMES.getValues().stream().filter(Config.COMMON.getNonWinterBiomesFilter()).forEach(biome -> {

            // Everything is winter now
            biome.temperature = -0.5f;
            biome.precipitation = Biome.RainType.SNOW;
            biome.waterColor = 0x3938C9;
            biome.waterFogColor = 0x050533;

            // Winter mobs
            biome.spawns.computeIfAbsent(EntityClassification.MONSTER, key -> new ArrayList<>()).add(new Biome.SpawnListEntry(EntityType.STRAY, 320, 4, 4));
            biome.spawns.computeIfAbsent(EntityClassification.CREATURE, key -> new ArrayList<>()).add(new Biome.SpawnListEntry(EntityType.POLAR_BEAR, 4, 1, 2));
            biome.spawns.computeIfAbsent(EntityClassification.CREATURE, key -> new ArrayList<>()).add(new Biome.SpawnListEntry(EntityType.SNOW_GOLEM, 8, 1, 1));

            // Freeze a bit more than just the top layer
            // Remove the original feature because it sucks
            biome.getFeatures(GenerationStage.Decoration.TOP_LAYER_MODIFICATION).removeIf(feature -> feature.config instanceof DecoratedFeatureConfig && ((DecoratedFeatureConfig) feature.config).feature.feature instanceof IceAndSnowFeature);
            biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, ModFeatures.FREEZE_EVERYTHING.get().withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(NoPlacementConfig.NO_PLACEMENT_CONFIG)));

            // Delegate all vegetation modifications through the block replacement
            // This allows us to capture all tree features and force them to use snowy versions of all their blocks.
            List<ConfiguredFeature<?, ?>> features = biome.getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);
            BlockReplacingConfiguredFeature featureWrapper = new BlockReplacingConfiguredFeature(world -> new BlockReplacingWorld(world, stateIn -> {
                Block replacementBlock = ModBlocks.SNOWY_TREE_BLOCKS.getOrDefault(stateIn.getBlock(), () -> null).get();
                if (replacementBlock != null)
                {
                    BlockState replacementState = replacementBlock.getDefaultState();
                    return Helpers.copyProperties(stateIn, replacementState);
                }
                return stateIn;
            }));
            features.forEach(featureWrapper::addDelegate);
            features.clear();
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, featureWrapper);

            // Ice bergs have migrated
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN))
            {
                DefaultBiomeFeatures.addIcebergs(biome);
                DefaultBiomeFeatures.addBlueIce(biome);
            }

            // Ice spikes (although less frequent)
            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Feature.ICE_SPIKE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(3))));
            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Feature.ICE_PATCH.withConfiguration(new FeatureRadiusConfig(2)).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(2))));

            // Igloos
            biome.addStructure(Feature.IGLOO.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        }));

        ModBlocks.setup();
    }
}
