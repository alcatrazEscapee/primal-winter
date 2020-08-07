/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.biome;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpawnSettings.class)
public interface ISpawnSettings
{
    @Accessor(value = "spawners")
    Map<SpawnGroup, List<SpawnSettings.SpawnEntry>> getSpawners();

    @Accessor(value = "spawners")
    void setSpawners(Map<SpawnGroup, List<SpawnSettings.SpawnEntry>> spawners);

}
