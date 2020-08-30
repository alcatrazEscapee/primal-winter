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
    public static final ConfiguredFeature<?, ?> RARE_ICE_SPIKES = register("rare_ice_spikes", Feature.ICE_SPIKE.configure(NoFeatureConfig.INSTANCE).decorate(Features.Placements.SQUARE_HEIGHTMAP.applyChance(3)));
    public static final ConfiguredFeature<?, ?> RARE_ICE_PATCH = register("rare_ice_patch", Feature.ICE_PATCH.configure(new SphereReplaceConfig(Blocks.PACKED_ICE.getDefaultState(), FeatureSpread.of(2, 1), 1, ImmutableList.of(Blocks.DIRT.getDefaultState(), Blocks.GRASS_BLOCK.getDefaultState(), Blocks.PODZOL.getDefaultState(), Blocks.COARSE_DIRT.getDefaultState(), Blocks.MYCELIUM.getDefaultState(), Blocks.SNOW_BLOCK.getDefaultState(), Blocks.ICE.getDefaultState()))).decorate(Features.Placements.SQUARE_HEIGHTMAP).applyChance(3));

    private static <FC extends IFeatureConfig> ConfiguredFeature<?, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature)
    {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, id, configuredFeature);
    }
}
