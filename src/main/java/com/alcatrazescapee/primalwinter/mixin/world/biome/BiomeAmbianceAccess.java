/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.biome;

import net.minecraft.world.biome.BiomeAmbience;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeAmbience.class)
public interface BiomeAmbianceAccess
{
    @Accessor(value = "waterColor")
    void setWaterColor(int waterColor);

    @Accessor(value = "waterFogColor")
    void setFogWaterColor(int waterFogColor);
}
