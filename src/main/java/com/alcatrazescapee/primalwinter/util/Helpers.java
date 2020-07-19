/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.util;

import net.minecraft.block.BlockState;
import net.minecraft.state.Property;

public final class Helpers
{
    public static BlockState copyProperties(BlockState oldState, BlockState newState)
    {
        for (Property<?> property : oldState.func_235904_r_()) // getProperties
        {
            if (newState.func_235904_r_().contains(property)) // getProperties
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
