/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.level.ServerWorldProperties;

public final class Helpers
{
    /**
     * Sets the weather of a world to an endless winter state
     */
    public static void setWeatherToEndlessWinter(ServerWorld world)
    {
        if (world != null && world.getLevelProperties() instanceof ServerWorldProperties)
        {
            world.setWeather(0, Integer.MAX_VALUE, true, true); // setWeather
            world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, world.getServer());
        }
    }

    /**
     * Called from {@link ServerWorld#tickChunk(WorldChunk, int)} via mixin, places additional snow layers
     */
    public static void placeExtraSnowOnTickChunk(ServerWorld world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        if (world.isRaining() && state.getBlock() == Blocks.SNOW && state.get(Properties.LAYERS) < 5)
        {
            world.setBlockState(pos, state.with(Properties.LAYERS, state.get(Properties.LAYERS) + 1));
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
        return replacement.with(property, original.get(property));
    }
}
