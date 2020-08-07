/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.biome;

import net.minecraft.world.biome.Biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Biome.Weather.class)
public interface IBiomeWeather
{
    @Accessor(value = "precipitation")
    void setPrecipitation(Biome.Precipitation precipitation);

    @Accessor(value = "temperature")
    void setTemperature(float temperature);
}
