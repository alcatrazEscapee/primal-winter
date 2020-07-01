/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.util;

import com.google.common.collect.Iterables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketManager;

/**
 * This is a collection of vanilla internal methods that are either duplicated, AT'd, or modified in some manner
 * They are kept here as a reference to the original method source they modified.
 */
public final class VanillaHacks
{
    /**
     * The {@code immutableLoadedChunks} field is AT'd here.
     * Instead of {@link ChunkManager#getLoadedChunksIterable()}
     */
    public static Iterable<ChunkHolder> getLoadedChunksIterable(ChunkManager chunkManager)
    {
        return Iterables.unmodifiableIterable(chunkManager.immutableLoadedChunks.values());
    }

    /**
     * Instead of {@link ChunkManager#isOutsideSpawningRadius(ChunkPos)}
     * The {@code ticketManager} and {@code playerGenerationTracker} field are AT'd here.
     */
    public static boolean isOutsideSpawningRadius(ChunkManager chunkManager, ChunkPos chunkPosIn)
    {
        long chunkPosHash = chunkPosIn.asLong();
        return !((TicketManager) chunkManager.ticketManager).isOutsideSpawningRadius(chunkPosHash) || chunkManager.playerGenerationTracker.getGeneratingPlayers(chunkPosHash).noneMatch(playerIn -> !playerIn.isSpectator() && getDistanceSquaredToChunk(chunkPosIn, playerIn) < 16384.0D);
    }

    /**
     * Copied from {@link ChunkManager#getDistanceSquaredToChunk(ChunkPos, Entity)}
     */
    public static double getDistanceSquaredToChunk(ChunkPos chunkPosIn, Entity entityIn)
    {
        double blockX = chunkPosIn.x * 16 + 8;
        double blockZ = chunkPosIn.z * 16 + 8;
        double distX = blockX - entityIn.getPosX();
        double distZ = blockZ - entityIn.getPosZ();
        return distX * distX + distZ * distZ;
    }

    /**
     * This is a section from {@link ServerWorld#tickEnvironment(Chunk, int)}, modified to properly accumulate snow layers
     */
    public static void tickRainAndSnow(ServerWorld world, Chunk chunkIn)
    {
        ChunkPos chunkPos = chunkIn.getPos();
        boolean isRaining = world.isRaining();
        int blockX = chunkPos.getXStart();
        int blockZ = chunkPos.getZStart();
        if (world.dimension.canDoRainSnowIce(chunkIn) && world.rand.nextInt(16) == 0)
        {
            BlockPos pos = world.getHeight(Heightmap.Type.MOTION_BLOCKING, world.getBlockRandomPos(blockX, 0, blockZ, 15));
            BlockState state = world.getBlockState(pos);
            if (isRaining && state.getBlock() == Blocks.SNOW && state.get(BlockStateProperties.LAYERS_1_8) < 5)
            {
                world.setBlockState(pos, state.with(BlockStateProperties.LAYERS_1_8, state.get(BlockStateProperties.LAYERS_1_8) + 1));
            }
        }
    }
}
