/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IceAndSnowFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import com.alcatrazescapee.primalwinter.world.ModFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This serves as a simple redirect from this feature to the better version of it.
 * Since replacing vanilla features in biomes is not simple or supported, we just co-opt this to call the improved feature
 */
@Mixin(IceAndSnowFeature.class)
public abstract class IceAndSnowFeatureMixin
{
    @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
    public void inject_generate(ISeedReader worldIn, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoFeatureConfig config, CallbackInfoReturnable<Boolean> cir)
    {
        ModFeatures.FREEZE_EVERYTHING.get().generate(worldIn, chunkGenerator, random, pos, config);
        cir.setReturnValue(true);
    }
}
