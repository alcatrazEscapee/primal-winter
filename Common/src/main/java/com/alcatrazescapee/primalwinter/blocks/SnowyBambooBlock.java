package com.alcatrazescapee.primalwinter.blocks;

import com.alcatrazescapee.primalwinter.util.PrimalWinterBlockTags;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class SnowyBambooBlock extends BambooStalkBlock
{
    public SnowyBambooBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        // Frozen bamboo doesn't drop itself as often as regular bamboo
        if (!state.canSurvive(level, pos))
        {
            level.destroyBlock(pos, random.nextInt(3) == 0);
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state)
    {
        return false; // Prevent growth
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        // Prevent growth
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        // Override to place normal bamboo - it's the only one that interacts properly with bamboo saplings,
        // since placing this on top of a sapling won't react properly.
        final FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        if (fluid.isEmpty())
        {
            final BlockState stateBelow = context.getLevel().getBlockState(context.getClickedPos().below());
            final BlockState defaultState = Blocks.BAMBOO.defaultBlockState();
            if (stateBelow.is(BlockTags.BAMBOO_PLANTABLE_ON))
            {
                if (stateBelow.is(Blocks.BAMBOO_SAPLING))
                {
                    return defaultState.setValue(AGE, 0);
                }
                else if (stateBelow.is(Blocks.BAMBOO))
                {
                    return defaultState.setValue(AGE, stateBelow.getValue(AGE) > 0 ? 1 : 0);
                }
                else
                {
                    final BlockState aboveState = context.getLevel().getBlockState(context.getClickedPos().above());
                    return aboveState.is(Blocks.BAMBOO)
                        ? defaultState.setValue(AGE, aboveState.getValue(AGE))
                        : Blocks.BAMBOO_SAPLING.defaultBlockState();
                }
            }
        }
        return null;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos)
    {
        if (!state.canSurvive(level, pos))
        {
            level.scheduleTick(pos, this, 1);
        }

        // Override to change the `is(Blocks.BAMBOO)` into one that checks snowy bamboo
        if (direction == Direction.UP && neighborState.is(PrimalWinterBlocks.SNOWY_BAMBOO.get()) && neighborState.getValue(AGE) > state.getValue(AGE))
        {
            level.setBlock(pos, state.cycle(AGE), 2);
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        // Override to use a more inclusive tag, as we can exist on more other blocks
        return level.getBlockState(pos.below()).is(PrimalWinterBlockTags.SNOWY_BAMBOO_SURVIVES_ON);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state)
    {
        return false; // Prevent bonemeal
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state)
    {
        return false;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state)
    {
        // No-op
    }
}
