package com.alcatrazescapee.primalwinter;

import com.alcatrazescapee.primalwinter.blocks.PrimalWinterItemGroups;
import com.alcatrazescapee.primalwinter.platform.NetworkSetupCallback;
import com.alcatrazescapee.primalwinter.util.ConfigPacket;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks;
import com.alcatrazescapee.primalwinter.client.PrimalWinterAmbience;
import com.alcatrazescapee.primalwinter.util.Config;
import com.alcatrazescapee.primalwinter.world.PrimalWinterFeatures;

public final class PrimalWinter
{
    public static final String MOD_ID = "primalwinter";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void earlySetup()
    {
        LOGGER.info("Early XPlatform Setup");

        PrimalWinterBlocks.BLOCKS.earlySetup();
        PrimalWinterBlocks.ITEMS.earlySetup();
        PrimalWinterItemGroups.TABS.earlySetup();
        PrimalWinterFeatures.FEATURES.earlySetup();
        PrimalWinterAmbience.PARTICLE_TYPES.earlySetup();
        PrimalWinterAmbience.SOUND_EVENTS.earlySetup();
    }

    public static void lateSetup()
    {
        LOGGER.info("Late XPlatform Setup");

        PrimalWinterBlocks.BLOCKS.lateSetup();
        PrimalWinterBlocks.ITEMS.lateSetup();
        PrimalWinterItemGroups.TABS.lateSetup();
        PrimalWinterFeatures.FEATURES.lateSetup();
        PrimalWinterAmbience.PARTICLE_TYPES.lateSetup();
        PrimalWinterAmbience.SOUND_EVENTS.lateSetup();

        PrimalWinterBlocks.registerAxeStrippables();

        Config.INSTANCE.load();
    }

    public static void networkingSetup(NetworkSetupCallback callback)
    {
        callback.registerS2C(ConfigPacket.TYPE, ConfigPacket.CODEC, Config.INSTANCE::onSync);
    }
}