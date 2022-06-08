package com.alcatrazescapee.primalwinter.platform.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import com.alcatrazescapee.primalwinter.platform.XPlatform;

public interface XPlatformClient
{
    XPlatformClient INSTANCE = XPlatform.find(XPlatformClient.class);

    void setRenderType(Block block, RenderType type);
}
