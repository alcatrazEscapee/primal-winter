/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.AbstractSphereReplaceConfig;
import net.minecraft.world.gen.feature.IcePathFeature;
import net.minecraft.world.gen.feature.SphereReplaceConfig;

import com.mojang.serialization.Codec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The standard ice patch feature does not place unless it encounters a snow block. This avoids that restriction.
 *
 * This is used as an alternative to replacing the feature, or replacing the feature in biomes (as both of those are much harder due to world gen changes and lacking forge API (registry replacement of features seems to not work correctly with default world gen settings?)
 */
@Mixin(IcePathFeature.class)
public abstract class IcePatchFeatureMixin extends AbstractSphereReplaceConfig
{
    private IcePatchFeatureMixin(Codec<SphereReplaceConfig> codec)
    {
        super(codec);
    }

    @Inject(method = "generate", at = @At("HEAD"))
    public void inject_generate(ISeedReader worldIn, ChunkGenerator chunkGenerator, Random random, BlockPos pos, SphereReplaceConfig config, CallbackInfoReturnable<Boolean> ci)
    {
        while (worldIn.isAirBlock(pos) && pos.getY() > 2)
        {
            pos = pos.down();
        }
        BlockState originalState = worldIn.getBlockState(pos);
        if (!BlockTags.LEAVES.contains(originalState.getBlock()) && !BlockTags.LOGS.contains(originalState.getBlock()))
        {
            worldIn.setBlockState(pos, Blocks.SNOW_BLOCK.getDefaultState(), 2);
        }
    }
}
