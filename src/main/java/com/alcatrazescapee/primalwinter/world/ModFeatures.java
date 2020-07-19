/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.world;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

public final class ModFeatures
{
    public static final FreezeEverythingFeature FREEZE_EVERYTHING = register("freeze_everything", new FreezeEverythingFeature(DefaultFeatureConfig.CODEC));

    private static <F extends Feature<?>> F register(String name, F feature)
    {
        return Registry.register(Registry.FEATURE, new Identifier(MOD_ID, name), feature);
    }
}
