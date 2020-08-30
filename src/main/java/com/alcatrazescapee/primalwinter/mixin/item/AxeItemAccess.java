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

/**
 * In order to add log stripping recipes
 */
@Mixin(AxeItem.class)
public interface AxeItemAccess
{
    @Accessor(value = "BLOCK_STRIPPING_MAP")
    static Map<Block, Block> getBlockStrippingMap() { return null; }

    @Accessor(value = "BLOCK_STRIPPING_MAP")
    static void setBlockStrippingMap(Map<Block, Block> map) {}
}
