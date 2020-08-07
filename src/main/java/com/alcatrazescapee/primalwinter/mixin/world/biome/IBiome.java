/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.biome;

import net.minecraft.world.biome.Biome;

import com.mojang.serialization.Codec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Biome.class)
public interface IBiome
{
    @Accessor(value = "CODEC")
    static void setCodec(Codec<Biome> codec) {}

    @Accessor(value = "weather")
    Biome.Weather getWeather();
}
