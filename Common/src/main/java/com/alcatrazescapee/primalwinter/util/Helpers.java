package com.alcatrazescapee.primalwinter.util;

import java.util.stream.Stream;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks;
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

    /**
     * During {@link ServerLevel#tickChunk(LevelChunk, int)}, places additional snow layers
     */
    public static void placeExtraSnow(ServerLevel level, ChunkAccess chunk)
    {
        if (Config.INSTANCE.enableSnowAccumulationDuringWeather.get() && level.random.nextInt(16) == 0)
        {
            final int blockX = chunk.getPos().getMinBlockX();
            final int blockZ = chunk.getPos().getMinBlockZ();
            final BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, level.getBlockRandomPos(blockX, 0, blockZ, 15));
            final BlockState state = level.getBlockState(pos);
            final Biome biome = level.getBiome(pos).value();
            if (level.isRaining() && biome.coldEnoughToSnow(pos) && level.getBrightness(LightLayer.BLOCK, pos) < 10)
            {
                if (state.getBlock() == Blocks.SNOW)
                {
                    // Stack snow layers
                    final int layers = state.getValue(BlockStateProperties.LAYERS);
                    if (layers < 5)
                    {
                        level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LAYERS, 1 + layers));
                    }

                    final BlockPos belowPos = pos.below();
                    final BlockState belowState = level.getBlockState(belowPos);
                    final Block replacementBlock = PrimalWinterBlocks.SNOWY_TERRAIN_BLOCKS.getOrDefault(belowState.getBlock(), () -> null).get();
                    if (replacementBlock != null)
                    {
                        level.setBlockAndUpdate(belowPos, replacementBlock.defaultBlockState());
                    }
                }
            }
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
