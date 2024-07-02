package com.alcatrazescapee.primalwinter.platform;

import java.util.function.Supplier;

import com.alcatrazescapee.primalwinter.ForgePrimalWinter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.VisibleForTesting;

public final class ForgeRegistryInterface<T> implements RegistryInterface<T>
{
    @VisibleForTesting public final DeferredRegister<T> deferred;

    public ForgeRegistryInterface(Registry<T> registry)
    {
        this.deferred = DeferredRegister.create(registry.key(), PrimalWinter.MOD_ID);
    }

    @Override
    public void earlySetup()
    {
        deferred.register(ForgePrimalWinter.EVENT_BUS);
    }

    @Override
    public <V extends T> RegistryHolder<V> register(String name, Supplier<? extends V> factory)
    {
        return new Holder<>(deferred.register(name, factory));
    }

    record Holder<T, V extends T>(DeferredHolder<T, V> obj) implements RegistryHolder<V>
    {
        @Override
        public V get()
        {
            return obj.value();
        }

        @Override
        public ResourceLocation id()
        {
            return obj.getId();
        }
    }
}
