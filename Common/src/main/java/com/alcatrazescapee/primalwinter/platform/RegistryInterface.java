package com.alcatrazescapee.primalwinter.platform;

import java.util.function.Supplier;

/**
 * @param <T> The type of the registry
 */
public interface RegistryInterface<T>
{
    default void earlySetup() {}

    default void lateSetup() {}

    /**
     * @param <V> The type of the underlying object
     */
    <V extends T> RegistryHolder<V> register(String name, Supplier<? extends V> factory);
}
