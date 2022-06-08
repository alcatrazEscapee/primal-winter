package com.alcatrazescapee.primalwinter;

import com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks;
import com.alcatrazescapee.primalwinter.client.PrimalWinterAmbience;
import com.alcatrazescapee.primalwinter.platform.XPlatform;
import com.alcatrazescapee.primalwinter.world.PrimalWinterWorldGen;

public final class PrimalWinter
{
    public static final String MOD_ID = "primalwinter";

    public static void earlySetup()
    {
        PrimalWinterBlocks.BLOCKS.earlySetup();
        PrimalWinterBlocks.ITEMS.earlySetup();
        PrimalWinterWorldGen.FEATURES.earlySetup();
        PrimalWinterWorldGen.CONFIGURED_FEATURES.earlySetup();
        PrimalWinterWorldGen.PLACED_FEATURES.earlySetup();
        PrimalWinterAmbience.PARTICLE_TYPES.earlySetup();
        PrimalWinterAmbience.SOUND_EVENTS.earlySetup();
    }

    public static void lateSetup()
    {
        PrimalWinterBlocks.BLOCKS.lateSetup();
        PrimalWinterBlocks.ITEMS.lateSetup();
        PrimalWinterWorldGen.FEATURES.lateSetup();
        PrimalWinterWorldGen.CONFIGURED_FEATURES.lateSetup();
        PrimalWinterWorldGen.PLACED_FEATURES.lateSetup();
        PrimalWinterAmbience.PARTICLE_TYPES.lateSetup();
        PrimalWinterAmbience.SOUND_EVENTS.lateSetup();

        PrimalWinterBlocks.registerAxeStrippables();
    }
}