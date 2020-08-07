/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.*;

public class ModConfiguredFeatures
{
    public static final ConfiguredFeature<?, ?> RARE_ICE_SPIKES = register("rare_ice_spikes", Feature.ICE_SPIKE.configure(FeatureConfig.DEFAULT).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP.applyChance(3)));
    public static final ConfiguredFeature<?, ?> RARE_ICE_PATCH = register("rare_ice_patch", Feature.ICE_PATCH.configure(new DiskFeatureConfig(Blocks.PACKED_ICE.getDefaultState(), UniformIntDistribution.of(2, 1), 1, ImmutableList.of(Blocks.DIRT.getDefaultState(), Blocks.GRASS_BLOCK.getDefaultState(), Blocks.PODZOL.getDefaultState(), Blocks.COARSE_DIRT.getDefaultState(), Blocks.MYCELIUM.getDefaultState(), Blocks.SNOW_BLOCK.getDefaultState(), Blocks.ICE.getDefaultState()))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).applyChance(3));

    private static <FC extends FeatureConfig> ConfiguredFeature<?, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature)
    {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
    }
}
