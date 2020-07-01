/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.world;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.storage.WorldInfo;

/**
 * A simple extension of {@link IWorld} that allows custom implementations without the bulk of changing every single method
 */
public interface IWorldDelegate extends IWorld
{
    IWorld getDelegate();

    @Override
    default long getSeed()
    {
        return getDelegate().getSeed();
    }

    @Override
    default float getCurrentMoonPhaseFactor()
    {
        return getDelegate().getCurrentMoonPhaseFactor();
    }

    @Override
    default float getCelestialAngle(float partialTicks)
    {
        return getDelegate().getCelestialAngle(partialTicks);
    }

    @Override
    default int getMoonPhase()
    {
        return getDelegate().getMoonPhase();
    }

    @Override
    default ITickList<Block> getPendingBlockTicks()
    {
        return getDelegate().getPendingBlockTicks();
    }

    @Override
    default ITickList<Fluid> getPendingFluidTicks()
    {
        return getDelegate().getPendingFluidTicks();
    }

    @Override
    default World getWorld()
    {
        return getDelegate().getWorld();
    }

    @Override
    default WorldInfo getWorldInfo()
    {
        return getDelegate().getWorldInfo();
    }

    @Override
    default DifficultyInstance getDifficultyForLocation(BlockPos pos)
    {
        return getDelegate().getDifficultyForLocation(pos);
    }

    @Override
    default Difficulty getDifficulty()
    {
        return getDelegate().getDifficulty();
    }

    @Override
    default AbstractChunkProvider getChunkProvider()
    {
        return getDelegate().getChunkProvider();
    }

    @Override
    default boolean chunkExists(int chunkX, int chunkZ)
    {
        return getDelegate().chunkExists(chunkX, chunkZ);
    }

    @Override
    default Random getRandom()
    {
        return getDelegate().getRandom();
    }

    @Override
    default void notifyNeighbors(BlockPos pos, Block blockIn)
    {
        getDelegate().notifyNeighbors(pos, blockIn);
    }

    @Override
    default BlockPos getSpawnPoint()
    {
        return getDelegate().getSpawnPoint();
    }

    @Override
    default void playSound(@Nullable PlayerEntity player, BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch)
    {
        getDelegate().playSound(player, pos, soundIn, category, volume, pitch);
    }

    @Override
    default void addParticle(IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
    {
        getDelegate().addParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    default void playEvent(@Nullable PlayerEntity player, int type, BlockPos pos, int data)
    {
        getDelegate().playEvent(player, type, pos, data);
    }

    @Override
    default void playEvent(int type, BlockPos pos, int data)
    {
        getDelegate().playEvent(type, pos, data);
    }

    @Override
    default Stream<VoxelShape> getEmptyCollisionShapes(@Nullable Entity entityIn, AxisAlignedBB aabb, Set<Entity> entitiesToIgnore)
    {
        return getDelegate().getEmptyCollisionShapes(entityIn, aabb, entitiesToIgnore);
    }

    @Override
    default boolean checkNoEntityCollision(@Nullable Entity entityIn, VoxelShape shape)
    {
        return getDelegate().checkNoEntityCollision(entityIn, shape);
    }

    @Override
    default BlockPos getHeight(Heightmap.Type heightmapType, BlockPos pos)
    {
        return getDelegate().getHeight(heightmapType, pos);
    }

    @Override
    default WorldBorder getWorldBorder()
    {
        return getDelegate().getWorldBorder();
    }

    @Override
    default boolean func_226663_a_(BlockState p_226663_1_, BlockPos p_226663_2_, ISelectionContext p_226663_3_)
    {
        return getDelegate().func_226663_a_(p_226663_1_, p_226663_2_, p_226663_3_);
    }

    @Override
    default boolean checkNoEntityCollision(Entity p_226668_1_)
    {
        return getDelegate().checkNoEntityCollision(p_226668_1_);
    }

    @Override
    default boolean hasNoCollisions(AxisAlignedBB p_226664_1_)
    {
        return getDelegate().hasNoCollisions(p_226664_1_);
    }

    @Override
    default boolean hasNoCollisions(Entity p_226669_1_)
    {
        return getDelegate().hasNoCollisions(p_226669_1_);
    }

    @Override
    default boolean hasNoCollisions(Entity p_226665_1_, AxisAlignedBB p_226665_2_)
    {
        return getDelegate().hasNoCollisions(p_226665_1_, p_226665_2_);
    }

    @Override
    default boolean hasNoCollisions(@Nullable Entity p_226662_1_, AxisAlignedBB p_226662_2_, Set<Entity> p_226662_3_)
    {
        return getDelegate().hasNoCollisions(p_226662_1_, p_226662_2_, p_226662_3_);
    }

    @Override
    default Stream<VoxelShape> getCollisionShapes(@Nullable Entity p_226667_1_, AxisAlignedBB p_226667_2_, Set<Entity> p_226667_3_)
    {
        return getDelegate().getCollisionShapes(p_226667_1_, p_226667_2_, p_226667_3_);
    }

    @Override
    default Stream<VoxelShape> getCollisionShapes(@Nullable Entity p_226666_1_, AxisAlignedBB p_226666_2_)
    {
        return getDelegate().getCollisionShapes(p_226666_1_, p_226666_2_);
    }

    @Nullable
    @Override
    default TileEntity getTileEntity(BlockPos pos)
    {
        return getDelegate().getTileEntity(pos);
    }

    @Override
    default BlockState getBlockState(BlockPos pos)
    {
        return getDelegate().getBlockState(pos);
    }

    @Override
    default IFluidState getFluidState(BlockPos pos)
    {
        return getDelegate().getFluidState(pos);
    }

    @Override
    default int getLightValue(BlockPos pos)
    {
        return getDelegate().getLightValue(pos);
    }

    @Override
    default int getMaxLightLevel()
    {
        return getDelegate().getMaxLightLevel();
    }

    @Override
    default int getHeight()
    {
        return getDelegate().getHeight();
    }

    @Override
    default BlockRayTraceResult rayTraceBlocks(RayTraceContext context)
    {
        return getDelegate().rayTraceBlocks(context);
    }

    @Nullable
    @Override
    default BlockRayTraceResult rayTraceBlocks(Vec3d p_217296_1_, Vec3d p_217296_2_, BlockPos p_217296_3_, VoxelShape p_217296_4_, BlockState p_217296_5_)
    {
        return getDelegate().rayTraceBlocks(p_217296_1_, p_217296_2_, p_217296_3_, p_217296_4_, p_217296_5_);
    }

    @Override
    default List<Entity> getEntitiesInAABBexcluding(@Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate)
    {
        return getDelegate().getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }

    @Override
    default <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> clazz, AxisAlignedBB aabb, @Nullable Predicate<? super T> filter)
    {
        return getDelegate().getEntitiesWithinAABB(clazz, aabb, filter);
    }

    @Override
    default <T extends Entity> List<T> getLoadedEntitiesWithinAABB(Class<? extends T> p_225316_1_, AxisAlignedBB p_225316_2_, @Nullable Predicate<? super T> p_225316_3_)
    {
        return getDelegate().getLoadedEntitiesWithinAABB(p_225316_1_, p_225316_2_, p_225316_3_);
    }

    @Override
    default List<? extends PlayerEntity> getPlayers()
    {
        return getDelegate().getPlayers();
    }

    @Override
    default List<Entity> getEntitiesWithinAABBExcludingEntity(@Nullable Entity entityIn, AxisAlignedBB bb)
    {
        return getDelegate().getEntitiesWithinAABBExcludingEntity(entityIn, bb);
    }

    @Override
    default <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> p_217357_1_, AxisAlignedBB p_217357_2_)
    {
        return getDelegate().getEntitiesWithinAABB(p_217357_1_, p_217357_2_);
    }

    @Override
    default <T extends Entity> List<T> func_225317_b(Class<? extends T> p_225317_1_, AxisAlignedBB p_225317_2_)
    {
        return getDelegate().func_225317_b(p_225317_1_, p_225317_2_);
    }

    @Nullable
    @Override
    default PlayerEntity getClosestPlayer(double x, double y, double z, double distance, @Nullable Predicate<Entity> predicate)
    {
        return getDelegate().getClosestPlayer(x, y, z, distance, predicate);
    }

    @Nullable
    @Override
    default PlayerEntity getClosestPlayer(Entity entityIn, double distance)
    {
        return getDelegate().getClosestPlayer(entityIn, distance);
    }

    @Nullable
    @Override
    default PlayerEntity getClosestPlayer(double x, double y, double z, double distance, boolean creativePlayers)
    {
        return getDelegate().getClosestPlayer(x, y, z, distance, creativePlayers);
    }

    @Nullable
    @Override
    default PlayerEntity getClosestPlayer(double x, double y, double z)
    {
        return getDelegate().getClosestPlayer(x, y, z);
    }

    @Override
    default boolean isPlayerWithin(double x, double y, double z, double distance)
    {
        return getDelegate().isPlayerWithin(x, y, z, distance);
    }

    @Nullable
    @Override
    default PlayerEntity getClosestPlayer(EntityPredicate predicate, LivingEntity target)
    {
        return getDelegate().getClosestPlayer(predicate, target);
    }

    @Nullable
    @Override
    default PlayerEntity getClosestPlayer(EntityPredicate predicate, LivingEntity target, double p_217372_3_, double p_217372_5_, double p_217372_7_)
    {
        return getDelegate().getClosestPlayer(predicate, target, p_217372_3_, p_217372_5_, p_217372_7_);
    }

    @Nullable
    @Override
    default PlayerEntity getClosestPlayer(EntityPredicate predicate, double x, double y, double z)
    {
        return getDelegate().getClosestPlayer(predicate, x, y, z);
    }

    @Nullable
    @Override
    default <T extends LivingEntity> T getClosestEntityWithinAABB(Class<? extends T> entityClazz, EntityPredicate p_217360_2_, @Nullable LivingEntity target, double x, double y, double z, AxisAlignedBB boundingBox)
    {
        return getDelegate().getClosestEntityWithinAABB(entityClazz, p_217360_2_, target, x, y, z, boundingBox);
    }

    @Nullable
    @Override
    default <T extends LivingEntity> T func_225318_b(Class<? extends T> p_225318_1_, EntityPredicate p_225318_2_, @Nullable LivingEntity p_225318_3_, double p_225318_4_, double p_225318_6_, double p_225318_8_, AxisAlignedBB p_225318_10_)
    {
        return getDelegate().func_225318_b(p_225318_1_, p_225318_2_, p_225318_3_, p_225318_4_, p_225318_6_, p_225318_8_, p_225318_10_);
    }

    @Nullable
    @Override
    default <T extends LivingEntity> T getClosestEntity(List<? extends T> entities, EntityPredicate predicate, @Nullable LivingEntity target, double x, double y, double z)
    {
        return getDelegate().getClosestEntity(entities, predicate, target, x, y, z);
    }

    @Override
    default List<PlayerEntity> getTargettablePlayersWithinAABB(EntityPredicate predicate, LivingEntity target, AxisAlignedBB box)
    {
        return getDelegate().getTargettablePlayersWithinAABB(predicate, target, box);
    }

    @Override
    default <T extends LivingEntity> List<T> getTargettableEntitiesWithinAABB(Class<? extends T> p_217374_1_, EntityPredicate p_217374_2_, LivingEntity p_217374_3_, AxisAlignedBB p_217374_4_)
    {
        return getDelegate().getTargettableEntitiesWithinAABB(p_217374_1_, p_217374_2_, p_217374_3_, p_217374_4_);
    }

    @Nullable
    @Override
    default PlayerEntity getPlayerByUuid(UUID uniqueIdIn)
    {
        return getDelegate().getPlayerByUuid(uniqueIdIn);
    }

    @Nullable
    @Override
    default IChunk getChunk(int x, int z, ChunkStatus requiredStatus, boolean nonnull)
    {
        return getDelegate().getChunk(x, z, requiredStatus, nonnull);
    }

    @Override
    default int getHeight(Heightmap.Type heightmapType, int x, int z)
    {
        return getDelegate().getHeight(heightmapType, x, z);
    }

    @Override
    default int getSkylightSubtracted()
    {
        return getDelegate().getSkylightSubtracted();
    }

    @Override
    default BiomeManager getBiomeManager()
    {
        return getDelegate().getBiomeManager();
    }

    @Override
    default Biome getBiome(BlockPos p_226691_1_)
    {
        return getDelegate().getBiome(p_226691_1_);
    }

    @Override
    default int getBlockColor(BlockPos blockPosIn, ColorResolver colorResolverIn)
    {
        return getDelegate().getBlockColor(blockPosIn, colorResolverIn);
    }

    @Override
    default Biome getNoiseBiome(int x, int y, int z)
    {
        return getDelegate().getNoiseBiome(x, y, z);
    }

    @Override
    default Biome getNoiseBiomeRaw(int x, int y, int z)
    {
        return getDelegate().getNoiseBiomeRaw(x, y, z);
    }

    @Override
    default boolean isRemote()
    {
        return getDelegate().isRemote();
    }

    @Override
    default int getSeaLevel()
    {
        return getDelegate().getSeaLevel();
    }

    @Override
    default Dimension getDimension()
    {
        return getDelegate().getDimension();
    }

    @Override
    default boolean isAirBlock(BlockPos pos)
    {
        return getDelegate().isAirBlock(pos);
    }

    @Override
    default boolean canBlockSeeSky(BlockPos pos)
    {
        return getDelegate().canBlockSeeSky(pos);
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    default float getBrightness(BlockPos pos)
    {
        return getDelegate().getBrightness(pos);
    }

    @Override
    default int getStrongPower(BlockPos pos, Direction direction)
    {
        return getDelegate().getStrongPower(pos, direction);
    }

    @Override
    default IChunk getChunk(BlockPos pos)
    {
        return getDelegate().getChunk(pos);
    }

    @Override
    default IChunk getChunk(int chunkX, int chunkZ)
    {
        return getDelegate().getChunk(chunkX, chunkZ);
    }

    @Override
    default IChunk getChunk(int chunkX, int chunkZ, ChunkStatus requiredStatus)
    {
        return getDelegate().getChunk(chunkX, chunkZ, requiredStatus);
    }

    @Nullable
    @Override
    default IBlockReader getBlockReader(int chunkX, int chunkZ)
    {
        return getDelegate().getBlockReader(chunkX, chunkZ);
    }

    @Override
    default boolean hasWater(BlockPos pos)
    {
        return getDelegate().hasWater(pos);
    }

    @Override
    default boolean containsAnyLiquid(AxisAlignedBB bb)
    {
        return getDelegate().containsAnyLiquid(bb);
    }

    @Override
    default int getLight(BlockPos pos)
    {
        return getDelegate().getLight(pos);
    }

    @Override
    default int getNeighborAwareLightSubtracted(BlockPos pos, int amount)
    {
        return getDelegate().getNeighborAwareLightSubtracted(pos, amount);
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    default boolean isBlockLoaded(BlockPos pos)
    {
        return getDelegate().isBlockLoaded(pos);
    }

    @Override
    default boolean isAreaLoaded(BlockPos center, int range)
    {
        return getDelegate().isAreaLoaded(center, range);
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    default boolean isAreaLoaded(BlockPos from, BlockPos to)
    {
        return getDelegate().isAreaLoaded(from, to);
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    default boolean isAreaLoaded(int fromX, int fromY, int fromZ, int toX, int toY, int toZ)
    {
        return getDelegate().isAreaLoaded(fromX, fromY, fromZ, toX, toY, toZ);
    }

    @Override
    default WorldLightManager getLightManager()
    {
        return getDelegate().getLightManager();
    }

    @Override
    default int getLightFor(LightType lightTypeIn, BlockPos blockPosIn)
    {
        return getDelegate().getLightFor(lightTypeIn, blockPosIn);
    }

    @Override
    default int getLightSubtracted(BlockPos blockPosIn, int amount)
    {
        return getDelegate().getLightSubtracted(blockPosIn, amount);
    }

    @Override
    default boolean canSeeSky(BlockPos blockPosIn)
    {
        return getDelegate().canSeeSky(blockPosIn);
    }

    @Override
    default boolean setBlockState(BlockPos pos, BlockState newState, int flags)
    {
        return getDelegate().setBlockState(pos, newState, flags);
    }

    @Override
    default boolean removeBlock(BlockPos pos, boolean isMoving)
    {
        return getDelegate().removeBlock(pos, isMoving);
    }

    @Override
    default boolean destroyBlock(BlockPos pos, boolean dropBlock)
    {
        return getDelegate().destroyBlock(pos, dropBlock);
    }

    @Override
    default boolean destroyBlock(BlockPos pos, boolean isMoving, @Nullable Entity entityIn)
    {
        return getDelegate().destroyBlock(pos, isMoving, entityIn);
    }

    @Override
    default boolean addEntity(Entity entityIn)
    {
        return getDelegate().addEntity(entityIn);
    }

    @Override
    default boolean hasBlockState(BlockPos pos, Predicate<BlockState> filter)
    {
        return getDelegate().hasBlockState(pos, filter);
    }

    @Override
    default int getMaxHeight()
    {
        return getDelegate().getMaxHeight();
    }
}
