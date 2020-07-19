/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

public class BlockReplacingConfiguredFeature extends ConfiguredFeature<NoFeatureConfig, Feature<NoFeatureConfig>>
{
    private final List<ConfiguredFeature<?, ?>> delegates;
    private final UnaryOperator<ISeedReader> replacementStrategy;

    public BlockReplacingConfiguredFeature(UnaryOperator<ISeedReader> replacementStrategy)
    {
        super(Feature.NO_OP, NoFeatureConfig.NO_FEATURE_CONFIG);
        this.replacementStrategy = replacementStrategy;
        this.delegates = new ArrayList<>();
    }

    public void addDelegate(ConfiguredFeature<?, ?> feature)
    {
        delegates.add(feature);
    }

    @Override
    public boolean func_236265_a_(ISeedReader worldIn, StructureManager structureManager, ChunkGenerator generator, Random rand, BlockPos pos)
    {
        ISeedReader world = replacementStrategy.apply(worldIn);
        delegates.forEach(feature -> feature.func_236265_a_(world, structureManager, generator, rand, pos));
        return true;
    }
}
