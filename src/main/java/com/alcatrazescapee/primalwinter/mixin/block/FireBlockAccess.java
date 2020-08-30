/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.block.FireBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireBlock.class)
public interface FireBlockAccess
{
    @Invoker(value = "setFireInfo")
    void callSetFireInfo(Block block, int encouragement, int flammability);
}
