package com.alcatrazescapee.primalwinter.platform.client;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface BlockColorCallback
{
    void accept(BlockColor color, Block... blocks);
}
