package com.alcatrazescapee.primalwinter.platform.client;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;

@FunctionalInterface
public interface ItemColorCallback
{
    void accept(ItemColor color, ItemLike... items);
}
