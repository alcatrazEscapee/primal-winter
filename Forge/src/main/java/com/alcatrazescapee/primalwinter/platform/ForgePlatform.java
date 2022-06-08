package com.alcatrazescapee.primalwinter.platform;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;

import com.alcatrazescapee.primalwinter.util.Config;

public final class ForgePlatform implements XPlatform
{
    @Override
    public <T> RegistryInterface<T> registryInterface(Registry<T> registry)
    {
        return new ForgeRegistryInterface<>(registry);
    }

    @Override
    public Config config()
    {
        return ForgeConfig.create();
    }

    @Override
    public CreativeModeTab creativeTab(ResourceLocation id, Supplier<ItemStack> icon)
    {
        return new CreativeModeTab(id.getNamespace() + "." + id.getPath()) {
            @Override
            public ItemStack makeIcon()
            {
                return icon.get();
            }
        };
    }

    @Override
    public boolean isDedicatedClient()
    {
        return FMLEnvironment.dist == Dist.CLIENT;
    }
}
