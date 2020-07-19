package com.alcatrazescapee.primalwinter.mixin.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.ChunkRegion;

import com.alcatrazescapee.primalwinter.common.ModBlocks;
import com.alcatrazescapee.primalwinter.util.Helpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * This is a *very* bull-in-china-shop hack of replacing all possible blocks of interest with their winter counterparts
 * The alternative is to try and catch every single tree or tree related feature... no thank you
 */
@Mixin(ChunkRegion.class)
public abstract class ChunkRegionMixin
{
    @ModifyVariable(method = "setBlockState", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private BlockState primalwinter_setBlockState(BlockState stateIn)
    {
        Block replacementBlock = ModBlocks.SNOWY_TREE_BLOCKS.get(stateIn.getBlock());
        if (replacementBlock != null)
        {
            BlockState replacementState = replacementBlock.getDefaultState();
            return Helpers.copyProperties(stateIn, replacementState);
        }
        return stateIn;
    }
}
