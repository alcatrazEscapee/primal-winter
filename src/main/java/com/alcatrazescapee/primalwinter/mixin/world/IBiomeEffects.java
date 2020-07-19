package com.alcatrazescapee.primalwinter.mixin.world;

import net.minecraft.world.biome.BiomeEffects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeEffects.class)
public interface IBiomeEffects
{
    @Accessor(value = "waterColor")
    void primalwinter_setWaterColor(int waterColor);

    @Accessor(value = "waterFogColor")
    void primalwinter_setFogWaterColor(int waterFogColor);
}
