package com.alcatrazescapee.primalwinter.platform;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import com.alcatrazescapee.primalwinter.PrimalWinter;

public final class ForgeRegistryInterface<T> implements RegistryInterface<T>
{
    private final Registry<T> registry;
    private final DeferredRegister<T> deferred;

    public ForgeRegistryInterface(Registry<T> registry)
    {
        this.registry = registry;
        this.deferred = DeferredRegister.create(registry.key(), PrimalWinter.MOD_ID);
    }

    @Override
    public void earlySetup()
    {
        deferred.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends T> RegistryHolder<V> register(String name, Supplier<? extends V> factory)
    {
        return new Holder<>(deferred.register(name, factory), (Registry<V>) registry);
    }

    record Holder<T>(RegistryObject<T> obj, Registry<T> registry) implements RegistryHolder<T>
    {
        @Override
        public T get()
        {
            return obj.get();
        }

        @Override
        public ResourceLocation id()
        {
            return obj.getId();
        }
    }
}
