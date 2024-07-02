package com.alcatrazescapee.primalwinter.blocks;

import com.alcatrazescapee.primalwinter.util.PrimalWinterBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SnowyCactusBlock extends CactusBlock
{
    public SnowyCactusBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        // No random ticks, frozen plants don't grow
    }

    @Override
    @SuppressWarnings("deprecation")
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            final BlockState adjacentState = level.getBlockState(pos.relative(direction));
            if (adjacentState.isSolid() || level.getFluidState(pos.relative(direction)).is(FluidTags.LAVA))
            {
                return false;
            }
        }

        // Override to change this from just sand + cactus to a tag which includes other blocks
        return level.getBlockState(pos.below()).is(PrimalWinterBlockTags.SNOWY_CACTUS_SURVIVES_ON)
            && !level.getBlockState(pos.above()).liquid();
    }
}
