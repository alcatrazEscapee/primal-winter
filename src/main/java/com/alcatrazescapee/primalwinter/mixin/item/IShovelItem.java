/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.item;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.ShovelItem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShovelItem.class)
public interface IShovelItem
{
    @Accessor(value = "EFFECTIVE_BLOCKS")
    static Set<Block> getEffectiveBlocks() { return null; }
}
