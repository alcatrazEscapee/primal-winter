/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.entity.passive;

import java.util.Random;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import com.alcatrazescapee.primalwinter.common.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Replace the vanilla passive mob spawn check with one that includes our snowy grass block
 */
@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends AgeableEntity
{
    @Inject(method = "checkAnimalSpawnRules", at = @At("HEAD"), cancellable = true)
    private static void checkAnimalSpawnRules(EntityType<? extends AnimalEntity> type, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(ModTags.Blocks.ANIMAL_SPAWNS_ON.contains(world.getBlockState(pos.below()).getBlock()) && world.getRawBrightness(pos, 0) > 8);
    }

    private AnimalEntityMixin(EntityType<? extends AgeableEntity> entityType, World world)
    {
        super(entityType, world);
    }
}