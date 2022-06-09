package com.alcatrazescapee.primalwinter.util;

import java.util.stream.Stream;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.alcatrazescapee.primalwinter.PrimalWinter;

public final class Helpers
{
    public static boolean isWinterDimension(ResourceLocation id)
    {
        final String name = id.toString();
        final Stream<? extends String> stream = Config.INSTANCE.nonWinterDimensions.get().stream();
        return Config.INSTANCE.invertNonWinterDimensions.get() ? stream.anyMatch(name::equals) : stream.noneMatch(name::equals);
    }
    public static boolean isWinterBiome(@Nullable ResourceLocation id)
    {
        if (id != null)
        {
            final String name = id.toString();
            final Stream<? extends String> stream = Config.INSTANCE.nonWinterBiomes.get().stream();
            return Config.INSTANCE.invertNonWinterBiomes.get() ? stream.anyMatch(name::equals) : stream.noneMatch(name::equals);
        }
        return false;
    }
    public static ResourceLocation identifier(String name)
    {
        return new ResourceLocation(PrimalWinter.MOD_ID, name);
    }

    public static boolean is(BlockState block, TagKey<Block> tag)
    {
        return block.is(tag);
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
