package com.alcatrazescapee.primalwinter.mixin;

import java.util.function.Supplier;
import com.alcatrazescapee.primalwinter.util.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks;
import com.alcatrazescapee.primalwinter.util.Helpers;

@Mixin(WorldGenRegion.class)
public abstract class WorldGenRegionMixin
{
    @Shadow @Final private ServerLevel level;

    @ModifyVariable(method = "setBlock", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private BlockState replaceAllBlocksWithSnowyOnes(BlockState stateIn)
    {
        if (!Config.INSTANCE.isWinterDimension(level.dimension()))
        {
            return stateIn;
        }
        final Supplier<? extends Block> block = PrimalWinterBlocks.SNOWY_TREE_BLOCKS.get(stateIn.getBlock());
        if (block == null)
        {
            return stateIn;
        }
        final BlockState replacementState = block.get().defaultBlockState();
        return Helpers.copyProperties(stateIn, replacementState);
    }
}
