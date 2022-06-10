package com.alcatrazescapee.primalwinter.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.IceSpikeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * The standard ice spike feature does not place unless it encounters a snow block. This avoids that restriction.
 */
public class ImprovedIceSpikeFeature extends IceSpikeFeature
{
    public ImprovedIceSpikeFeature(Codec<NoneFeatureConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        final WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        while (level.isEmptyBlock(pos) && pos.getY() > level.getMinBuildHeight() + 2)
        {
            pos = pos.below();
        }
        final BlockState originalState = level.getBlockState(pos);
        if (!originalState.is(BlockTags.LEAVES) && !originalState.is(BlockTags.LOGS))
        {
            level.setBlock(pos, Blocks.SNOW_BLOCK.defaultBlockState(), 2);
        }
        return super.place(context);
    }
}
