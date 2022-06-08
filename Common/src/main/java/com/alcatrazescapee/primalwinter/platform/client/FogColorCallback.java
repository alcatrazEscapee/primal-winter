package com.alcatrazescapee.primalwinter.platform.client;

@FunctionalInterface
public interface FogColorCallback
{
    void accept(float red, float green, float blue);
}
