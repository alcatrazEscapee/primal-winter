/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.world;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IcePathFeature;
import net.minecraft.world.gen.feature.SphereReplaceConfig;

import com.alcatrazescapee.primalwinter.util.Helpers;
import com.mojang.serialization.Codec;

/**
 * The standard ice patch feature does not place unless it encounters a snow block. This avoids that restriction.
 */
public class ImprovedIcePatchFeature extends IcePathFeature
{
    public ImprovedIcePatchFeature(Codec<SphereReplaceConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader worldIn, ChunkGenerator chunkGenerator, Random random, BlockPos pos, SphereReplaceConfig config)
    {
        return super.place(worldIn, chunkGenerator, random, Helpers.adjustPosForIceFeature(worldIn, pos), config);
    }
}
