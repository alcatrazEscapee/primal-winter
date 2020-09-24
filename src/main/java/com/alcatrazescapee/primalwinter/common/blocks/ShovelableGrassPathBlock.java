/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.GrassPathBlock;
import net.minecraftforge.common.ToolType;

public class ShovelableGrassPathBlock extends GrassPathBlock
{
    public ShovelableGrassPathBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean isToolEffective(BlockState state, ToolType tool)
    {
        return tool == ToolType.SHOVEL;
    }
}