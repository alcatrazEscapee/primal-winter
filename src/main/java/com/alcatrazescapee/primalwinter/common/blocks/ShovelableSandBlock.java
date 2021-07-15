/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.SandBlock;
import net.minecraftforge.common.ToolType;

public class ShovelableSandBlock extends SandBlock
{
    public ShovelableSandBlock(int dustColor, Properties properties)
    {
        super(dustColor, properties);
    }

    @Override
    public boolean isToolEffective(BlockState state, ToolType tool)
    {
        return tool == ToolType.SHOVEL;
    }
}