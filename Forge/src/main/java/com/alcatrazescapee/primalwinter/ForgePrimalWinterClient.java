package com.alcatrazescapee.primalwinter;

import java.util.function.Function;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.alcatrazescapee.primalwinter.client.ClientEventHandler;
import com.alcatrazescapee.primalwinter.platform.client.ParticleProviderCallback;

public final class ForgePrimalWinterClient
{
    public static void setupClient()
    {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        modBus.addListener((FMLClientSetupEvent event) -> ClientEventHandler.setupClient());
        modBus.addListener((RegisterColorHandlersEvent.Block event) -> ClientEventHandler.setupBlockColors(event::register));
        modBus.addListener((RegisterColorHandlersEvent.Item event) -> ClientEventHandler.setupItemColors(event::register));
        modBus.addListener((RegisterParticleProvidersEvent event) -> ClientEventHandler.setupParticleFactories(new ParticleProviderCallback()
        {
            @Override
            public <T extends ParticleOptions> void accept(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider)
            {
                event.registerSpriteSet(type, provider::apply);
            }
        }));

        forgeBus.addListener((ViewportEvent.ComputeFogColor event) -> ClientEventHandler.renderFogColors(event.getCamera(), (float) event.getPartialTick(), (red, green, blue) -> {
            event.setRed(red);
            event.setBlue(blue);
            event.setGreen(green);
        }));

        forgeBus.addListener((ViewportEvent.RenderFog event) -> ClientEventHandler.renderFogDensity(event.getCamera(), (nearPlane, farPlane) -> {
            event.scaleNearPlaneDistance(nearPlane);
            event.scaleFarPlaneDistance(farPlane);
            event.setCanceled(true);
        }));
    }
}
