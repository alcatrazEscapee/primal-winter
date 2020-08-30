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

/**
 * In order to add our soil block variants to shovels
 */
@Mixin(ShovelItem.class)
public interface ShovelItemAccess
{
    @Accessor(value = "EFFECTIVE_ON")
    static Set<Block> getEffectiveBlocks() { return null; }
}
