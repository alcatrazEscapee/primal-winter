package com.alcatrazescapee.primalwinter.platform;

import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;

public final class ForgePlatform implements XPlatform
{
    private final ForgeConfig config = new ForgeConfig();

    @Override
    public <T> RegistryInterface<T> registryInterface(Registry<T> registry)
    {
        return new ForgeRegistryInterface<>(registry);
    }

    @Override
    public CreativeModeTab.Builder creativeTab()
    {
        return CreativeModeTab.builder();
    }

    @Override
    public Config config()
    {
        return config;
    }
}
