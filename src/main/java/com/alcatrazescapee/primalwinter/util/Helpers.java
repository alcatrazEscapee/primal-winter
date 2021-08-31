/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.util;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

import com.alcatrazescapee.primalwinter.Config;
import com.alcatrazescapee.primalwinter.common.ModBlocks;

public final class Helpers
{
    /**
     * Called from {@link ServerWorld#tickChunk(WorldChunk, int)} via mixin, places additional snow layers
     */
    public static void placeExtraSnowOnTickChunk(ServerWorld world, Chunk chunk)
    {
        if (Config.COMMON.enableSnowAccumulationDuringWeather.get() && world.random.nextInt(16) == 0)
        {
            final int blockX = chunk.getPos().getMinBlockX();
            final int blockZ = chunk.getPos().getMinBlockZ();
            final BlockPos pos = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, world.getBlockRandomPos(blockX, 0, blockZ, 15));
            final BlockState state = world.getBlockState(pos);
            final Biome biome = world.getBiome(pos);
            if (world.isRaining() && biome.getTemperature(pos) < 0.15 && world.getBrightness(LightType.BLOCK, pos) < 10)
            {
                if (state.getBlock() == Blocks.SNOW)
                {
                    // Stack snow layers
                    final int layers = state.getValue(BlockStateProperties.LAYERS);
                    if (layers < 5)
                    {
                        world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LAYERS, 1 + layers));
                    }

                    final BlockPos belowPos = pos.below();
                    final BlockState belowState = world.getBlockState(belowPos);
                    final Block replacementBlock = ModBlocks.SNOWY_TERRAIN_BLOCKS.getOrDefault(belowState.getBlock(), () -> null).get();
                    if (replacementBlock != null)
                    {
                        world.setBlockAndUpdate(belowPos, replacementBlock.defaultBlockState());
                    }
                }
            }
        }
    }

    /**
     * For certain vanilla features which do a check at the starting position, if it's a full block of snow (and thus only generating in very specific locations), we need to fake the initial conditions for other biomes.
     */
    public static BlockPos adjustPosForIceFeature(ISeedReader worldIn, BlockPos pos)
    {
        while (worldIn.isEmptyBlock(pos) && pos.getY() > 2)
        {
            pos = pos.below();
        }
        BlockState originalState = worldIn.getBlockState(pos);
        if (!BlockTags.LEAVES.contains(originalState.getBlock()) && !BlockTags.LOGS.contains(originalState.getBlock()))
        {
            worldIn.setBlock(pos, Blocks.SNOW_BLOCK.defaultBlockState(), 2);
        }
        return pos;
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

    /**
     * Used for static final fields injected by forge, stops IDE nullability warnings
     */
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public static <T> T notNull()
    {
        return null;
    }

    /**
     * Direct casts, avoiding mixin warnings from (T) (Object) obj; casts.
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj)
    {
        return (T) obj;
    }
}