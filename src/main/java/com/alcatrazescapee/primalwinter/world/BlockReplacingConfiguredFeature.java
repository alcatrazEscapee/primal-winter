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
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.*;

public class BlockReplacingConfiguredFeature extends ConfiguredFeature<NoFeatureConfig, Feature<NoFeatureConfig>>
{
    private final List<ConfiguredFeature<?, ?>> delegates;
    private final UnaryOperator<IWorld> replacementStrategy;

    public BlockReplacingConfiguredFeature(UnaryOperator<IWorld> replacementStrategy)
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
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos)
    {
        IWorld world = replacementStrategy.apply(worldIn);
        delegates.forEach(feature -> feature.place(world, generator, rand, pos));
        return true;
    }
}
