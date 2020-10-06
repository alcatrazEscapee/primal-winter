/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.entity.passive;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Modify the polar bear spawn logic to include all oceans, not just frozen ocean
 */
@Mixin(PolarBearEntity.class)
public abstract class PolarBearEntityMixin extends AnimalEntity
{
    @Inject(method = "checkPolarBearSpawnRules", at = @At("HEAD"), cancellable = true)
    private static void inject$checkPolarBearSpawnRules(EntityType<PolarBearEntity> type, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir)
    {
        Biome biome = world.getBiome(pos);
        if (biome.getBiomeCategory() != Biome.Category.OCEAN)
        {
            cir.setReturnValue(AnimalEntity.checkMobSpawnRules(type, world, spawnReason, pos, random));
        }
        else
        {
            cir.setReturnValue(world.getRawBrightness(pos, 0) > 8 && world.getBlockState(pos.below()).is(Blocks.ICE));
        }
    }

    private PolarBearEntityMixin(EntityType<? extends AnimalEntity> entityType, World world)
    {
        super(entityType, world);
    }
}