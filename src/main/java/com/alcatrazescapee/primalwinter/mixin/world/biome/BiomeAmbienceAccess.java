/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.biome;

import net.minecraft.world.biome.BiomeAmbience;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Will be made superfluous by https://github.com/MinecraftForge/MinecraftForge/pull/7336
 */
@Mixin(BiomeAmbience.class)
public interface BiomeAmbienceAccess
{
    @Accessor(value = "waterColor")
    void setWaterColor(int waterColor);

    @Accessor(value = "waterFogColor")
    void setFogWaterColor(int waterFogColor);
}