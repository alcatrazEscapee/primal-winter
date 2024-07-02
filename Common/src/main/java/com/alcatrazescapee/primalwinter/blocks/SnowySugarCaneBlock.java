package com.alcatrazescapee.primalwinter.blocks;

import com.alcatrazescapee.primalwinter.util.PrimalWinterBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SnowySugarCaneBlock extends SugarCaneBlock
{
    public SnowySugarCaneBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        // No random ticks, frozen plants don't grow
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        // Override to vastly simplify the survive check. Snowy sugarcane does not grow, and has no growth requirements,
        // so we don't check for nearby water or ice. There is a separate check for world generation to stop sugar cane from
        // just appearing anywhere.
        final BlockState stateBelow = level.getBlockState(pos.below());
        return stateBelow.is(PrimalWinterBlockTags.SNOWY_SUGAR_CANE_SURVIVES_ON);
    }
}
