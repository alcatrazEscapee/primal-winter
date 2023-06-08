package com.alcatrazescapee.primalwinter.blocks;

import java.util.ArrayList;
import java.util.List;
import com.alcatrazescapee.primalwinter.platform.RegistryHolder;
import com.alcatrazescapee.primalwinter.platform.RegistryInterface;
import com.alcatrazescapee.primalwinter.platform.XPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class PrimalWinterItemGroups
{
    public static final RegistryInterface<CreativeModeTab> TABS = XPlatform.INSTANCE.registryInterface(BuiltInRegistries.CREATIVE_MODE_TAB);
    public static final List<RegistryHolder<? extends ItemLike>> ENTRIES = new ArrayList<>();

    public static final RegistryHolder<CreativeModeTab> TAB = TABS.register("items", () -> XPlatform.INSTANCE.creativeTab()
        .icon(() -> new ItemStack(PrimalWinterBlocks.SNOWY_DIRT.get()))
        .title(Component.translatable("primalwinter.items"))
        .displayItems((params, output) -> {
            for (RegistryHolder<? extends ItemLike> entry : ENTRIES)
            {
                output.accept(entry.get());
            }
        })
        .build());
}
