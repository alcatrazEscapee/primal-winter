/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public final class Helpers
{
    /**
     * Called from {@link ServerWorld#tickChunk(WorldChunk, int)} via mixin, places additional snow layers
     */
    public static void placeExtraSnowOnTickChunk(ServerWorld world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        if (world.isRaining() && state.getBlock() == Blocks.SNOW && state.getValue(BlockStateProperties.LAYERS) < 5)
        {
            world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LAYERS, state.getValue(BlockStateProperties.LAYERS) + 1));
        }
    }

    public static BlockState copyProperties(BlockState oldState, BlockState newState)
    {
        for (Property<?> property : oldState.getProperties())
        {
            if (newState.getProperties().contains(property))
            {
                newState = copyProperty(property, oldState, newState);
            }
        }
        return newState;
    }

    /**
     * This gets around both arguments being a {@code IProperty<?>}
     */
    public static <T extends Comparable<T>> BlockState copyProperty(Property<T> property, BlockState original, BlockState replacement)
    {
        return replacement.setValue(property, original.getValue(property));
    }
}