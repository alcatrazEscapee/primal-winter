package com.alcatrazescapee.primalwinter.platform.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import com.alcatrazescapee.primalwinter.platform.client.XPlatformClient;

public final class ForgePlatformClient implements XPlatformClient
{
    @Override
    @SuppressWarnings("removal")
    public void setRenderType(Block block, RenderType type)
    {
        ItemBlockRenderTypes.setRenderLayer(block, type);
    }
}
