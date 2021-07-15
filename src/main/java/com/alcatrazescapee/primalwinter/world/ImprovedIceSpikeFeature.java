/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.world;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IceSpikeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import com.mojang.serialization.Codec;

/**
 * The standard ice spike feature does not place unless it encounters a snow block. This avoids that restriction.
 */
public class ImprovedIceSpikeFeature extends IceSpikeFeature
{
    public ImprovedIceSpikeFeature(Codec<NoFeatureConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader worldIn, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoFeatureConfig config)
    {
        while (worldIn.isEmptyBlock(pos) && pos.getY() > 2)
        {
            pos = pos.below();
        }
        BlockState originalState = worldIn.getBlockState(pos);
        if (!BlockTags.LEAVES.contains(originalState.getBlock()) && !BlockTags.LOGS.contains(originalState.getBlock()))
        {
            worldIn.setBlock(pos, Blocks.SNOW_BLOCK.defaultBlockState(), 2);
        }
        return super.place(worldIn, chunkGenerator, random, pos, config);
    }
}
