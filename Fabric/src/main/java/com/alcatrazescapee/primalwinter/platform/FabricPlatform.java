package com.alcatrazescapee.primalwinter.platform;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;

public final class FabricPlatform implements XPlatform
{
    private final FabricConfig config = new FabricConfig();

    @Override
    public <T> RegistryInterface<T> registryInterface(Registry<T> registry)
    {
        return new FabricRegistryInterface<>(registry);
    }

    @Override
    public CreativeModeTab.Builder creativeTab()
    {
        return FabricItemGroup.builder();
    }

    @Override
    public Config config()
    {
        return config;
    }
}
