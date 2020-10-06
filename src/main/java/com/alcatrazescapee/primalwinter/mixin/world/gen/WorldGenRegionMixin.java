/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.WorldGenRegion;

import com.alcatrazescapee.primalwinter.common.ModBlocks;
import com.alcatrazescapee.primalwinter.util.Helpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * This is a *very* bull-in-china-shop hack of replacing all possible blocks of interest with their winter counterparts
 * The alternative is to try and catch every single tree or tree related feature... no thank you
 */
@Mixin(WorldGenRegion.class)
public abstract class WorldGenRegionMixin
{
    @ModifyVariable(method = "setBlock", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private BlockState modifyVariable$setBlock(BlockState stateIn)
    {
        Block replacementBlock = ModBlocks.SNOWY_TREE_BLOCKS.getOrDefault(stateIn.getBlock(), () -> null).get();
        if (replacementBlock != null)
        {
            BlockState replacementState = replacementBlock.defaultBlockState();
            return Helpers.copyProperties(stateIn, replacementState);
        }
        return stateIn;
    }
}