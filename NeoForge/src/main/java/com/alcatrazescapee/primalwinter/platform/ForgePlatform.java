package com.alcatrazescapee.primalwinter.platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.alcatrazescapee.primalwinter.util.Helpers;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class ForgePlatform implements XPlatform
{
    public static void runBiomeModifiers(MinecraftServer server)
    {
        try
        {
            final Method method = ServerLifecycleHooks.class.getDeclaredMethod("runModifiers", MinecraftServer.class);
            method.setAccessible(true);
            method.invoke(null, server);
        }
        catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e)
        {
            Helpers.throwAsUnchecked(e);
        }
    }

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
