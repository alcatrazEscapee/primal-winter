package com.alcatrazescapee.primalwinter.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import com.alcatrazescapee.primalwinter.PrimalWinter;

public final class Helpers
{
    public static ResourceLocation identifier(String name)
    {
        return ResourceLocation.fromNamespaceAndPath(PrimalWinter.MOD_ID, name);
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
    private static <T extends Comparable<T>> BlockState copyProperty(Property<T> property, BlockState original, BlockState replacement)
    {
        return replacement.setValue(property, original.getValue(property));
    }
}
