package com.alcatrazescapee.primalwinter.platform.client;

@FunctionalInterface
public interface FogDensityCallback
{
    void accept(float nearPlane, float farPlane);
}
