/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.IcePatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;

/**
 * The standard ice patch feature does not place unless it encounters a snow block. This avoids that restriction.
 */
public class ImprovedIcePatchFeature extends IcePatchFeature
{
    public ImprovedIcePatchFeature(Codec<DiskConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<DiskConfiguration> context)
    {
        PrimalWinterWorldGen.adjustPosForIceFeature(context);
        return super.place(context);
    }
}
