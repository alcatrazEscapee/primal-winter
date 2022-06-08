package com.alcatrazescapee.primalwinter;

import com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks;
import com.alcatrazescapee.primalwinter.client.PrimalWinterAmbience;
import com.alcatrazescapee.primalwinter.util.Config;
import com.alcatrazescapee.primalwinter.world.PrimalWinterWorldGen;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class PrimalWinter
{
    public static final String MOD_ID = "primalwinter";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void earlySetup()
    {
        LOGGER.info("Early XPlatform Setup");

        Config.INSTANCE.earlySetup();

        PrimalWinterBlocks.BLOCKS.earlySetup();
        PrimalWinterBlocks.ITEMS.earlySetup();
        PrimalWinterWorldGen.Features.FEATURES.earlySetup();
        PrimalWinterWorldGen.Configured.CONFIGURED_FEATURES.earlySetup();
        PrimalWinterWorldGen.Placed.PLACED_FEATURES.earlySetup();
        PrimalWinterAmbience.PARTICLE_TYPES.earlySetup();
        PrimalWinterAmbience.SOUND_EVENTS.earlySetup();
    }

    public static void lateSetup()
    {
        LOGGER.info("Late XPlatform Setup");

        PrimalWinterBlocks.BLOCKS.lateSetup();
        PrimalWinterBlocks.ITEMS.lateSetup();
        PrimalWinterWorldGen.Features.FEATURES.lateSetup();
        PrimalWinterWorldGen.Configured.CONFIGURED_FEATURES.lateSetup();
        PrimalWinterWorldGen.Placed.PLACED_FEATURES.lateSetup();
        PrimalWinterAmbience.PARTICLE_TYPES.lateSetup();
        PrimalWinterAmbience.SOUND_EVENTS.lateSetup();

        PrimalWinterBlocks.registerAxeStrippables();
    }
}