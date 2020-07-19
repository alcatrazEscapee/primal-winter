/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Biome.class)
public interface IBiome
{
    //@formatter:off
    @Accessor(value = "temperature") void primalwinter_setTemperature(float temperature);
    @Accessor(value = "precipitation") void primalwinter_setPrecipitation(Biome.Precipitation precipitation);

    @Accessor(value = "spawns") Map<SpawnGroup, List<Biome.SpawnEntry>> primalwinter_getSpawns();
    //@formatter:on
}
