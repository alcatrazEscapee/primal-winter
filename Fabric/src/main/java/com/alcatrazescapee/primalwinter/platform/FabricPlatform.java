package com.alcatrazescapee.primalwinter.platform;

import java.nio.file.Path;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public final class FabricPlatform implements XPlatform
{
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
    public boolean isDedicatedClient()
    {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public Path configDir()
    {
        return FabricLoader.getInstance().getConfigDir();
    }
}
