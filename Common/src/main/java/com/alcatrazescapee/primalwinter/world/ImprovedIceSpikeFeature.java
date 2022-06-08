/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.world;

import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.IceSpikeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import com.mojang.serialization.Codec;

/**
 * The standard ice spike feature does not place unless it encounters a snow block. This avoids that restriction.
 */
public class ImprovedIceSpikeFeature extends IceSpikeFeature
{
    public ImprovedIceSpikeFeature(Codec<NoneFeatureConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        PrimalWinterWorldGen.adjustPosForIceFeature(context);
        return super.place(context);
    }
}
