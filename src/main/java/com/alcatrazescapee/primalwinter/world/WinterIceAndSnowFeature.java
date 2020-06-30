package com.alcatrazescapee.primalwinter.world;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import com.alcatrazescapee.primalwinter.common.ModBlocks;
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

        for (int x = 0; x < 16; ++x)
        {
            for (int z = 0; z < 16; ++z)
            {
                boolean placedSurface = false;
                int yPos = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING, pos.getX() + x, pos.getZ() + z);
                mutablePos.setPos(pos.getX() + x, yPos, pos.getZ() + z);
                int lightLevel = worldIn.getLightFor(LightType.SKY, mutablePos);
                while (mutablePos.getY() >= generator.getSeaLevel() - 1 && lightLevel > 5)
                {
                    BlockState stateAt = worldIn.getBlockState(mutablePos);

                    // Snow blocks immediately under the surface
                    if (!placedSurface)
                    {
                        Block replacementBlock = ModBlocks.SNOWY_TERRAIN_BLOCKS.getOrDefault(stateAt.getBlock(), () -> null).get();
                        if (replacementBlock != null)
                        {
                            BlockState replacementState = replacementBlock.getDefaultState();
                            if (replacementState.has(BlockStateProperties.SNOWY))
                            {
                                replacementState = replacementState.with(BlockStateProperties.SNOWY, true);
                            }
                            worldIn.setBlockState(mutablePos, replacementState, 2);
                        }
                        placedSurface = true;
                    }

                    // Snow layers
                    // Freeze both water and lava
                    BlockState state = worldIn.getBlockState(mutablePos);
                    IFluidState fluidState = worldIn.getFluidState(mutablePos);
                    if (fluidState.getFluid() == Fluids.WATER && state.getBlock() instanceof FlowingFluidBlock)
                    {
                        worldIn.setBlockState(mutablePos, Blocks.ICE.getDefaultState(), 2);
                    }
                    else if (fluidState.getFluid() == Fluids.LAVA && state.getBlock() instanceof FlowingFluidBlock)
                    {
                        worldIn.setBlockState(mutablePos, Blocks.OBSIDIAN.getDefaultState(), 2);
                    }
                    else if (Blocks.SNOW.getDefaultState().isValidPosition(worldIn, mutablePos) && stateAt.getMaterial().isReplaceable())
                    {
                        int layers = 5 + rand.nextInt(3) - countExposedFaces(worldIn, mutablePos);
                        worldIn.setBlockState(mutablePos, Blocks.SNOW.getDefaultState().with(BlockStateProperties.LAYERS_1_8, layers), 3); // cause a block update since we replace replaceable blocks such as double tall grass which need to update
                        placedSurface = false;
                    }

                    mutablePos.move(Direction.DOWN);
                    lightLevel = worldIn.getLightFor(LightType.SKY, mutablePos);
                }
            }
        }
        return true;
    }

    public static int countExposedFaces(IWorld world, BlockPos pos)
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
}
