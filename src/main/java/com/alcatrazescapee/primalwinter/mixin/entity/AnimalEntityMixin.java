/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.entity;

import java.util.Random;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import com.alcatrazescapee.primalwinter.common.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity
{
    @Inject(method = "isValidNaturalSpawn", at = @At("HEAD"), cancellable = true)
    private static void primalwinter_isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(ModTags.Blocks.ANIMAL_SPAWNS_ON.contains(world.getBlockState(pos.down()).getBlock()) && world.getBaseLightLevel(pos, 0) > 8);
    }

    private AnimalEntityMixin(EntityType<? extends PassiveEntity> entityType, World world)
    {
        super(entityType, world);
    }
}
