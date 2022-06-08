package com.alcatrazescapee.primalwinter.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import com.alcatrazescapee.primalwinter.platform.client.XPlatformClient;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;

public final class FabricPlatformClient implements XPlatformClient
{
    @Override
    public void setRenderType(Block block, RenderType type)
    {
        BlockRenderLayerMap.INSTANCE.putBlock(block, type);
    }
}
