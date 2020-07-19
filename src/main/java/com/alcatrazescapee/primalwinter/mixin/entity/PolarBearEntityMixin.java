/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.entity;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PolarBearEntity.class)
public abstract class PolarBearEntityMixin extends AnimalEntity
{
    @Inject(method = "canSpawn", at = @At("HEAD"), cancellable = true)
    private static void primalwinter_canSpawn(EntityType<PolarBearEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir)
    {
        Biome biome = world.getBiome(pos);
        if (biome.getCategory() != Biome.Category.OCEAN)
        {
            cir.setReturnValue(AnimalEntity.isValidNaturalSpawn(type, world, spawnReason, pos, random));
        }
        else
        {
            cir.setReturnValue(world.getBaseLightLevel(pos, 0) > 8 && world.getBlockState(pos.down()).isOf(Blocks.ICE));
        }
    }

    protected PolarBearEntityMixin(EntityType<? extends AnimalEntity> entityType, World world)
    {
        super(entityType, world);
    }
}
