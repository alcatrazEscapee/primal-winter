package com.alcatrazescapee.primalwinter.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import com.alcatrazescapee.primalwinter.util.Helpers;
import org.jetbrains.annotations.Nullable;

public final class FabricRegistryInterface<T> implements RegistryInterface<T>
{
    private final Registry<T> registry;
    private final List<Holder<? extends T>> holders;

    public FabricRegistryInterface(Registry<T> registry)
    {
        this.registry = registry;
        this.holders = new ArrayList<>();
    }

    @Override
    public void lateSetup()
    {
        holders.forEach(holder -> holder.register(registry));
        holders.clear();
    }

    @Override
    public <V extends T> RegistryHolder<V> register(String name, Supplier<? extends V> factory)
    {
        final Holder<V> holder = new Holder<>(Helpers.identifier(name), factory);
        holders.add(holder);
        return holder;
    }

    private static class Holder<T> implements RegistryHolder<T>
    {
        private final ResourceLocation id;
        private final Supplier<? extends T> factory;
        private @Nullable T value;

        Holder(ResourceLocation id, Supplier<? extends T> factory)
        {
            this.id = id;
            this.factory = factory;
        }

        @Override
        public T get()
        {
            if (value == null) throw new NullPointerException("Registry object not present: " + id);
            return value;
        }

        @Override
        public ResourceLocation id()
        {
            return id;
        }

        private void register(Registry<? super T> registry)
        {
            value = Registry.register(registry, id, factory.get());
        }
    }
}
