/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FreezeTopLayerFeature;

import com.alcatrazescapee.primalwinter.world.ModFeatures;
import com.mojang.serialization.Codec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FreezeTopLayerFeature.class)
public abstract class FreezeTopLayerFeatureMixin extends Feature<DefaultFeatureConfig>
{
    private FreezeTopLayerFeatureMixin(Codec<DefaultFeatureConfig> configCodec)
    {
        super(configCodec);
    }

    @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
    public void generate(StructureWorldAccess worldIn, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig defaultFeatureConfig, CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(ModFeatures.FREEZE_EVERYTHING.generate(worldIn, chunkGenerator, random, pos, defaultFeatureConfig)); // Just call the better version of this feature instead
    }
}
