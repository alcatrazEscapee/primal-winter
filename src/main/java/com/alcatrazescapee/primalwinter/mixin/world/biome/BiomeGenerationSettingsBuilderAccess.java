/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.biome;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeGenerationSettings.Builder.class)
public interface BiomeGenerationSettingsBuilderAccess
{
    @Accessor(value = "features")
    List<List<Supplier<ConfiguredFeature<?, ?>>>> getFeatures();
}
