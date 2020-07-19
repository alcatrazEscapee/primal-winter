/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Iterables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketManager;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.primalwinter.Config;
import com.alcatrazescapee.primalwinter.common.ModBlocks;
import com.alcatrazescapee.primalwinter.common.ModTags;
import com.alcatrazescapee.primalwinter.world.BlockReplacingConfiguredFeature;
import com.alcatrazescapee.primalwinter.world.BlockReplacingServerWorld;
import com.alcatrazescapee.primalwinter.world.ModFeatures;
import com.alcatrazescapee.primalwinter.world.SnowPlacingConfiguredFeature;

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
        if (isRaining && world.rand.nextInt(16) == 0)
        {
            BlockPos pos = world.getHeight(Heightmap.Type.MOTION_BLOCKING, world.getBlockRandomPos(blockX, 0, blockZ, 15));
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == Blocks.SNOW && state.get(BlockStateProperties.LAYERS_1_8) < 5)
            {
                world.setBlockState(pos, state.with(BlockStateProperties.LAYERS_1_8, state.get(BlockStateProperties.LAYERS_1_8) + 1));
            }
        }
    }

    public static void hackWinterBiomes()
    {
        ForgeRegistries.BIOMES.getValues().stream().filter(Config.COMMON.getNonWinterBiomesFilter()).forEach(biome -> {

            // Everything is winter now
            biome.temperature = -0.5f;
            biome.precipitation = Biome.RainType.SNOW;
            biome.func_235089_q_().field_235206_c_ = 0x3938C9; // Water color
            biome.func_235089_q_().field_235207_d_ = 0x050533; // Water fog color

            // Winter mobs
            biome.spawns.computeIfAbsent(EntityClassification.MONSTER, key -> new ArrayList<>()).add(new Biome.SpawnListEntry(EntityType.STRAY, 320, 4, 4));
            biome.spawns.computeIfAbsent(EntityClassification.CREATURE, key -> new ArrayList<>()).add(new Biome.SpawnListEntry(EntityType.POLAR_BEAR, 4, 1, 2));
            biome.spawns.computeIfAbsent(EntityClassification.CREATURE, key -> new ArrayList<>()).add(new Biome.SpawnListEntry(EntityType.SNOW_GOLEM, 4, 4, 8));

            // Freeze a bit more than just the top layer
            // Remove the original feature because it sucks
            biome.getFeatures(GenerationStage.Decoration.TOP_LAYER_MODIFICATION).removeIf(feature -> feature.config instanceof DecoratedFeatureConfig && ((DecoratedFeatureConfig) feature.config).feature.feature instanceof IceAndSnowFeature);
            biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, ModFeatures.FREEZE_EVERYTHING.get().withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(NoPlacementConfig.NO_PLACEMENT_CONFIG)));

            // Delegate all vegetation modifications through the block replacement
            // This allows us to capture all tree features and force them to use snowy versions of all their blocks.
            List<ConfiguredFeature<?, ?>> features = biome.getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);
            BlockReplacingConfiguredFeature featureWrapper = new BlockReplacingConfiguredFeature(world -> new BlockReplacingServerWorld(world, stateIn -> {
                Block replacementBlock = ModBlocks.SNOWY_TREE_BLOCKS.getOrDefault(stateIn.getBlock(), () -> null).get();
                if (replacementBlock != null)
                {
                    BlockState replacementState = replacementBlock.getDefaultState();
                    return Helpers.copyProperties(stateIn, replacementState);
                }
                return stateIn;
            }));
            features.forEach(featureWrapper::addDelegate);
            features.clear();
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, featureWrapper);

            // Ice bergs have migrated
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN))
            {
                DefaultBiomeFeatures.addIcebergs(biome);
                DefaultBiomeFeatures.addBlueIce(biome);
            }

            // Ice spikes (although less frequent)
            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, new SnowPlacingConfiguredFeature<>(Feature.ICE_SPIKE, IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(7))));
            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, new SnowPlacingConfiguredFeature<>(Feature.ICE_PATCH, new FeatureRadiusConfig(2)).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(5))));

            // Igloos
            // addStructure(DefaultBiomeFeatures.IGLOO)
            biome.func_235063_a_(DefaultBiomeFeatures.field_235169_g_);

            // Add cold specific biome dictionary tags
            BiomeDictionary.addTypes(biome, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY);
        });
    }

    /**
     * This is modified from the static init in {@link EntitySpawnPlacementRegistry}
     */
    public static void hackEntitySpawnPlacementRegistry()
    {
        replaceSpawnEntry(EntityType.CHICKEN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.COW, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.DONKEY, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.HORSE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.LLAMA, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.MULE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.PIG, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.POLAR_BEAR, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canPolarBearSpawn);
        replaceSpawnEntry(EntityType.RABBIT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.SHEEP, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.TURTLE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canTurtleSpawnOn);
        replaceSpawnEntry(EntityType.WOLF, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.CAT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.FOX, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.PANDA, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
        replaceSpawnEntry(EntityType.TRADER_LLAMA, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VanillaHacks::canAnimalSpawn);
    }

    /**
     * This is an example of a really cheeky hack / work around.
     * Vanilla will modify the registry map, *then* check if a duplicate registration has occured and throw. So if we just swallow the exception here...
     */
    public static <T extends MobEntity> void replaceSpawnEntry(EntityType<T> entityTypeIn, EntitySpawnPlacementRegistry.PlacementType placementType, Heightmap.Type heightMapType, EntitySpawnPlacementRegistry.IPlacementPredicate<T> predicate)
    {
        try
        {
            EntitySpawnPlacementRegistry.register(entityTypeIn, placementType, heightMapType, predicate);
        }
        catch (IllegalStateException e)
        {
            // Registry is fixed!
        }
    }

    /**
     * {@link AnimalEntity#canAnimalSpawn(EntityType, IWorld, SpawnReason, BlockPos, Random)}
     */
    public static boolean canAnimalSpawn(EntityType<?> animal, IWorld worldIn, SpawnReason reason, BlockPos pos, Random random)
    {
        return ModTags.Blocks.ANIMAL_SPAWNS_ON.contains(worldIn.getBlockState(pos.down()).getBlock()) && worldIn.getLightSubtracted(pos, 0) > 8;
    }

    /**
     * {@link PolarBearEntity#func_223320_c(EntityType, IWorld, SpawnReason, BlockPos, Random)}
     */
    public static boolean canPolarBearSpawn(EntityType<PolarBearEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random)
    {
        Biome biome = world.getBiome(pos);
        if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN))
        {
            return VanillaHacks.canAnimalSpawn(entityType, world, reason, pos, random);
        }
        else
        {
            return world.getLightSubtracted(pos, 0) > 8 && world.getBlockState(pos.down()).getBlock() == Blocks.ICE;
        }
    }

    /**
     * {@link TurtleEntity#canSpawnOn(EntityType, IWorld, SpawnReason, BlockPos, Random)}
     */
    public static boolean canTurtleSpawnOn(EntityType<TurtleEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random random)
    {
        return pos.getY() < world.getSeaLevel() + 4 && ModTags.Blocks.TURTLE_SPAWNS_ON.contains(world.getBlockState(pos.down()).getBlock()) && world.getLightSubtracted(pos, 0) > 8;
    }
}
