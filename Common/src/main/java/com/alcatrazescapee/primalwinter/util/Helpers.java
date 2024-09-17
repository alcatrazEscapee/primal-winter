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
        return newState.getBlock().withPropertiesOf(oldState);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void throwAsUnchecked(Throwable exception) throws E
    {
        throw (E) exception;
    }
}
