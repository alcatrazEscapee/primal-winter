/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.biome;

import net.minecraft.world.biome.BiomeEffects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeEffects.class)
public interface IBiomeEffects
{
    @Accessor(value = "waterColor")
    void setWaterColor(int waterColor);

    @Accessor(value = "waterFogColor")
    void setFogWaterColor(int waterFogColor);
}
