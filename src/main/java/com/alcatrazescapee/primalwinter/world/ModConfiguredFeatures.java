/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;

public final class ModConfiguredFeatures
{
    public static final ConfiguredFeature<?, ?> FREEZE_TOP_LAYER = register("freeze_top_layer", ModFeatures.FREEZE_TOP_LAYER.get().configured(NoFeatureConfig.INSTANCE));
    public static final ConfiguredFeature<?, ?> ICE_SPIKES = register("ice_spikes", ModFeatures.ICE_SPIKES.get().configured(NoFeatureConfig.INSTANCE).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(3));
    public static final ConfiguredFeature<?, ?> ICE_PATCH = register("ice_patch", ModFeatures.ICE_PATCH.get().configured(new SphereReplaceConfig(Blocks.PACKED_ICE.defaultBlockState(), FeatureSpread.of(2, 1), 1, ImmutableList.of(Blocks.DIRT.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState(), Blocks.PODZOL.defaultBlockState(), Blocks.COARSE_DIRT.defaultBlockState(), Blocks.MYCELIUM.defaultBlockState(), Blocks.SNOW_BLOCK.defaultBlockState(), Blocks.ICE.defaultBlockState()))).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(3));

    public static void setup() {} // Register features during setup

    private static <FC extends IFeatureConfig> ConfiguredFeature<?, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature)
    {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, id, configuredFeature);
    }
}