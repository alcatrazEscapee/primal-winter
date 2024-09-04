package com.alcatrazescapee.primalwinter.platform;

import java.util.ServiceLoader;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;

public interface XPlatform
{
    XPlatform INSTANCE = find(XPlatform.class);

    static <T> T find(Class<T> clazz)
    {
        return ServiceLoader.load(clazz)
            .findFirst()
            .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }

    <T> RegistryInterface<T> registryInterface(Registry<T> registry);

    CreativeModeTab.Builder creativeTab();

    Config config();
}
