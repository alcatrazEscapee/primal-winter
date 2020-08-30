/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.biome;

import net.minecraft.world.biome.Biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Biome.Climate.class)
public interface BiomeClimateAccess
{
    @Accessor(value = "precipitation")
    void setPrecipitation(Biome.RainType precipitation);

    @Accessor(value = "temperature")
    void setTemperature(float temperature);

    @Accessor(value = "temperatureModifier")
    void setTemperatureModifier(Biome.TemperatureModifier temperatureModifier);
}
