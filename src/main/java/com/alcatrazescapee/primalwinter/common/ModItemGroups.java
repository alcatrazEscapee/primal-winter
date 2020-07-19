/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.common;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;

public class ModItemGroups
{
    public static final ItemGroup BLOCKS = FabricItemGroupBuilder.build(new Identifier(PrimalWinter.MOD_ID, "blocks"), () -> new ItemStack(ModBlocks.SNOWY_OAK_LEAVES));
}
