package com.alcatrazescapee.primalwinter.mixin.world;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IcePatchFeature;
import net.minecraft.world.gen.feature.IcePatchFeatureConfig;

import com.mojang.serialization.Codec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IcePatchFeature.class)
public abstract class IcePatchFeatureMixin extends Feature<IcePatchFeatureConfig>
{
    private IcePatchFeatureMixin(Codec<IcePatchFeatureConfig> codec)
    {
        super(codec);
    }

    @Inject(method = "generate", at = @At("HEAD"))
    private void primalwinter_generate(ServerWorldAccess worldIn, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos pos, IcePatchFeatureConfig icePatchFeatureConfig, CallbackInfoReturnable<Boolean> cir)
    {
        while (worldIn.isAir(pos) && pos.getY() > 2)
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
