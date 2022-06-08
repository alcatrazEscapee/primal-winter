package com.alcatrazescapee.primalwinter.util;

import java.util.stream.Stream;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public final class Helpers
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        final boolean enable = Config.INSTANCE.enableWeatherCommand.get();
        LOGGER.info("Vanilla /weather enabled = {}", enable);
        if (!enable)
        {
            // Vanilla weather command... NOT ALLOWED
            dispatcher.getRoot().getChildren().removeIf(node -> node.getName().equals("weather"));
            dispatcher.register(Commands.literal("weather").executes(source -> {
                source.getSource().sendSuccess(new TextComponent("Not even a command can overcome this storm... (This command is disabled by Primal Winter)"), false);
                return 0;
            }));
        }
    }

    public static void setLevelToThunder(LevelAccessor levelIn)
    {
        if (levelIn instanceof ServerLevel level && isWinterDimension(level.dimension().location()))
        {
            // Copied from WeatherCommand
            level.setWeatherParameters(0, Integer.MAX_VALUE, true, true);
        }
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

    public static boolean isWinterDimension(ResourceLocation id)
    {
        final String name = id.toString();
        final Stream<? extends String> stream = Config.INSTANCE.nonWinterDimensions.get().stream();
        return Config.INSTANCE.invertNonWinterDimensions.get() ? stream.anyMatch(name::equals) : stream.noneMatch(name::equals);
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
