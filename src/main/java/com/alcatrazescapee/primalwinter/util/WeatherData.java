/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.util;

import javax.annotation.Nullable;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import com.alcatrazescapee.primalwinter.Config;
import com.alcatrazescapee.primalwinter.PrimalWinter;

public class WeatherData implements ICapabilitySerializable<ByteNBT>
{
    @CapabilityInject(WeatherData.class)
    public static final Capability<WeatherData> CAPABILITY = Helpers.notNull();
    public static final ResourceLocation ID = new ResourceLocation(PrimalWinter.MOD_ID, "weather_data");

    public static void setup()
    {
        CapabilityManager.INSTANCE.register(WeatherData.class, new NoopStorage<>(), () -> {
            throw new UnsupportedOperationException();
        });
    }

    public static void trySetEndlessStorm(ServerWorld world)
    {
        final WeatherData cap = world.getCapability(CAPABILITY).orElseThrow(() -> new IllegalStateException("Expected WeatherData to exist on World " + world.dimension() + " / " + world.dimensionType()));
        if (!cap.alreadySetWorldToWinter)
        {
            cap.alreadySetWorldToWinter = true;
            if (Config.COMMON.isWinterDimension(world.dimension().location()))
            {
                world.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, world.getServer());
                world.setWeatherParameters(0, Integer.MAX_VALUE, true, true);
            }
        }
    }

    private final LazyOptional<WeatherData> capability;
    private boolean alreadySetWorldToWinter;

    public WeatherData()
    {
        capability = LazyOptional.of(() -> this);
        alreadySetWorldToWinter = false;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        return cap == CAPABILITY ? capability.cast() : LazyOptional.empty();
    }

    @Override
    public ByteNBT serializeNBT()
    {
        return ByteNBT.valueOf(alreadySetWorldToWinter);
    }

    @Override
    public void deserializeNBT(ByteNBT nbt)
    {
        alreadySetWorldToWinter = nbt.getAsByte() == 1;
    }
}
