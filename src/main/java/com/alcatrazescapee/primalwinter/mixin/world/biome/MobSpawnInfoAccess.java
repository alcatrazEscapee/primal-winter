/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.biome;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.MobSpawnInfo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobSpawnInfo.class)
public interface MobSpawnInfoAccess
{
    @Accessor(value = "spawners")
    Map<EntityClassification, List<MobSpawnInfo.Spawners>> getSpawners();

    @Accessor(value = "spawners")
    void setSpawners(Map<EntityClassification, List<MobSpawnInfo.Spawners>> spawners);
}
