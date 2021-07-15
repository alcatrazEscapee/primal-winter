/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.biome;

import net.minecraft.world.biome.BiomeAmbience;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * {@link BiomeAmbience} has no builder exposed to {@link net.minecraftforge.event.world.BiomeLoadingEvent}. This is easier than having to convert the ambience back to a builder ourselves.
 */
@Mixin(BiomeAmbience.class)
public interface BiomeAmbienceAccessor
{
    @Accessor(value = "waterColor")
    void accessor$setWaterColor(int waterColor);

    @Accessor(value = "waterFogColor")
    void accessor$setFogWaterColor(int waterFogColor);
}