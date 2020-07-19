/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.item;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.AxeItem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AxeItem.class)
public interface IAxeItem
{
    @Accessor(value = "STRIPPED_BLOCKS")
    static void primalwinter_setStrippedBlocks(Map<Block, Block> map) {}

    @Accessor(value = "STRIPPED_BLOCKS")
    static Map<Block, Block> primalwinter_getStrippedBlocks() { return null; }
}
