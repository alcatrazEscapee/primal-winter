/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.GravelBlock;
import net.minecraftforge.common.ToolType;

public class ShovelableGravelBlock extends GravelBlock
{
    public ShovelableGravelBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean isToolEffective(BlockState state, ToolType tool)
    {
        return tool == ToolType.SHOVEL;
    }
}