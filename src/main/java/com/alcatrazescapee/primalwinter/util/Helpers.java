/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.util;

import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;

public final class Helpers
{
    public static void copyProperties(BlockState oldState, BlockState newState)
    {
        for (IProperty<?> property : oldState.getProperties())
        {
            if (newState.has(property))
            {
                newState = copyProperty(property, oldState, newState);
            }
        }
    }

    /**
     * This gets around both arguments being a {@code IProperty<?>}
     */
    public static <T extends Comparable<T>> BlockState copyProperty(IProperty<T> property, BlockState original, BlockState replacement)
    {
        return replacement.with(property, original.get(property));
    }
}
