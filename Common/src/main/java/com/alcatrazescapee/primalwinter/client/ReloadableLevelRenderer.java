package com.alcatrazescapee.primalwinter.client;

public interface ReloadableLevelRenderer
{
    /**
     * Reloads config-dependent cached values, such as "is this dimension a winter dimension"
     */
    void primalWinter$reload();
}
