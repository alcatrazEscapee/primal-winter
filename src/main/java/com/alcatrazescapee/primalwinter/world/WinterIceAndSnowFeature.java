package com.alcatrazescapee.primalwinter.world;

import java.util.*;
import java.util.function.Function;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.TallBlockItem;
import net.minecraft.state.IProperty;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import com.alcatrazescapee.primalwinter.common.ModBlocks;
import com.alcatrazescapee.primalwinter.util.Vec2i;
import com.mojang.datafixers.Dynamic;

public class WinterIceAndSnowFeature extends Feature<NoFeatureConfig>
{
    public WinterIceAndSnowFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
    {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
    {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        // First, find the highest and lowest exposed y pos in the chunk
        int maxY = 0, minY = Integer.MAX_VALUE;
        for (int x = 0; x < 16; ++x)
        {
            for (int z = 0; z < 16; ++z)
            {
                int y = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING, pos.getX() + x, pos.getZ() + z);
                if (maxY < y)
                {
                    maxY = y;
                }
            }
        }

        // Then, step downwards, tracking the exposure to sky at each step
        int[] skyLights = new int[16 * 16], prevSkyLights = new int[16 * 16];
        Arrays.fill(prevSkyLights, 7);
        for (int y = maxY; y >= 0; y--)
        {
            for (int x = 0; x < 16; ++x)
            {
                for (int z = 0; z < 16; ++z)
                {
                    int skyLight = prevSkyLights[x + 16 * z];
                    mutablePos.setPos(pos.getX() + x, y, pos.getZ() + z);
                    BlockState state = worldIn.getBlockState(mutablePos);
                    if (state.isAir(worldIn, mutablePos))
                    {
                        // Continue sky light downwards
                        skyLights[x + 16 * z] = prevSkyLights[x + 16 * z];
                        extendSkyLights(skyLights, x, z);
                    }
                    if (skyLight > 0)
                    {
                        //replaceTreeBlocks(worldIn, mutablePos, state);
                        placeSnowAndIce(worldIn, mutablePos, state, rand, skyLight);
                    }
                }
            }

            // Break early if all possible sky light is gone
            boolean hasSkyLight = false;
            for (int i = 0; i < 16 * 16; i++)
            {
                if (skyLights[i] > 0)
                {
                    hasSkyLight = true;
                    break; // exit checking loop, continue with y loop
                }
            }
            if (!hasSkyLight)
            {
                break; // exit y loop
            }

            // Copy sky lights into previous and reset current sky lights
            System.arraycopy(skyLights, 0, prevSkyLights, 0, skyLights.length);
            Arrays.fill(skyLights, 0);
        }
        return true;
    }

    private void placeSnowAndIce(IWorld worldIn, BlockPos.Mutable mutablePos, BlockState state, Random random, int skyLight)
    {
        IFluidState fluidState = worldIn.getFluidState(mutablePos);
        if (fluidState.getFluid() == Fluids.WATER && state.getBlock() instanceof FlowingFluidBlock || state.getMaterial().isReplaceable())
        {
            if (!(state.getBlock() instanceof FlowingFluidBlock))
            {
                worldIn.destroyBlock(mutablePos, false);
            }
            worldIn.setBlockState(mutablePos, Blocks.ICE.getDefaultState(), 2);
        }
        else if (fluidState.getFluid() == Fluids.LAVA && state.getBlock() instanceof FlowingFluidBlock)
        {
            worldIn.setBlockState(mutablePos, Blocks.OBSIDIAN.getDefaultState(), 2);
        }
        else if (Blocks.SNOW.getDefaultState().isValidPosition(worldIn, mutablePos) && state.getMaterial().isReplaceable())
        {
            int layers = MathHelper.clamp(skyLight - random.nextInt(3) - countExposedFaces(worldIn, mutablePos), 1, 7);
            worldIn.destroyBlock(mutablePos, false);
            worldIn.setBlockState(mutablePos, Blocks.SNOW.getDefaultState().with(BlockStateProperties.LAYERS_1_8, layers), 3);

            // Replace the below block as well
            mutablePos.move(Direction.DOWN);
            BlockState stateDown = worldIn.getBlockState(mutablePos);
            Block replacementBlock = ModBlocks.SNOWY_TERRAIN_BLOCKS.getOrDefault(stateDown.getBlock(), () -> null).get();
            if (replacementBlock != null)
            {
                BlockState replacementState = replacementBlock.getDefaultState();
                worldIn.setBlockState(mutablePos, replacementState, 2);
            }
            mutablePos.move(Direction.UP); // move back
        }
    }

    private int countExposedFaces(IWorld world, BlockPos pos)
    {
        int count = 0;
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            BlockPos posAt = pos.offset(direction);
            if (!world.getBlockState(posAt).isSolidSide(world, posAt, direction.getOpposite()))
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Simple BFS that extends a sky light source outwards within the array
     */
    private void extendSkyLights(int[] skyLights, int startX, int startZ)
    {
        List<Vec3i> positions = new ArrayList<>();
        Set<Vec2i> visited = new HashSet<>();
        positions.add(new Vec3i(startX, skyLights[startX + 16 * startZ], startZ));
        visited.add(new Vec2i(startX, startZ));
        while (!positions.isEmpty())
        {
            Vec3i position = positions.remove(0);
            for (Direction direction : Direction.Plane.HORIZONTAL)
            {
                int nextX = position.getX() + direction.getXOffset();
                int nextZ = position.getZ() + direction.getZOffset();
                int nextSkyLight = position.getY() - 1;
                if (nextX >= 0 && nextX < 16 && nextZ >= 0 && nextZ < 16 && skyLights[nextX + 16 * nextZ] < nextSkyLight)
                {
                    Vec2i nextVisited = new Vec2i(nextX, nextZ);
                    if (!visited.contains(nextVisited))
                    {
                        skyLights[nextX + 16 * nextZ] = nextSkyLight;
                        positions.add(new Vec3i(nextX, nextSkyLight, nextZ));
                        visited.add(nextVisited);
                    }
                }
            }
        }
    }
}
