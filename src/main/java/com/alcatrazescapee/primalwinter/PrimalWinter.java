/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.alcatrazescapee.primalwinter.client.ModParticleTypes;
import com.alcatrazescapee.primalwinter.client.ModSoundEvents;
import com.alcatrazescapee.primalwinter.common.ModBlocks;
import com.alcatrazescapee.primalwinter.common.ModItems;
import com.alcatrazescapee.primalwinter.world.ModFeatures;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

@Mod(MOD_ID)
public final class PrimalWinter
{
    public static final String MOD_ID = "primalwinter";

    private static final Logger LOGGER = LogManager.getLogger();

    public PrimalWinter()
    {
        LOGGER.debug("Constructing");

        // Setup config
        Config.init();

        // Register event handlers
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.register(this);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);

        ModFeatures.FEATURES.register(modEventBus);

        ModSoundEvents.SOUND_EVENTS.register(modEventBus);
        ModParticleTypes.PARTICLE_TYPES.register(modEventBus);
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.debug("Setup");
        ModBlocks.setup();
    }
}
