/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.world;

import java.util.function.UnaryOperator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BlockReplacingWorld implements IWorldDelegate
{
    private final IWorld delegate;
    private final UnaryOperator<BlockState> replacementStrategy;

    public BlockReplacingWorld(IWorld delegate, UnaryOperator<BlockState> replacementStrategy)
    {
        this.delegate = delegate;
        this.replacementStrategy = replacementStrategy;
    }

    @Override
    public IWorld getDelegate()
    {
        return delegate;
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState newState, int flags)
    {
        return IWorldDelegate.super.setBlockState(pos, replacementStrategy.apply(newState), flags);
    }
}
