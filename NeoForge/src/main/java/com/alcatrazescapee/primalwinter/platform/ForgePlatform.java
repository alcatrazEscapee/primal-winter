package com.alcatrazescapee.primalwinter.platform;

import java.nio.file.Path;

import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

public final class ForgePlatform implements XPlatform
{
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
    public boolean isDedicatedClient()
    {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    @Override
    public Path configDir()
    {
        return FMLPaths.CONFIGDIR.get();
    }
}
