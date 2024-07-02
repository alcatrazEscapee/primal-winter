package com.alcatrazescapee.primalwinter;

import java.util.function.Function;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import com.alcatrazescapee.primalwinter.client.ClientEventHandler;
import com.alcatrazescapee.primalwinter.platform.client.ParticleProviderCallback;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class ForgePrimalWinterClient
{
    public static void setupClient()
    {
        ForgePrimalWinter.EVENT_BUS.addListener((FMLClientSetupEvent event) -> ClientEventHandler.setupClient());
        ForgePrimalWinter.EVENT_BUS.addListener((RegisterColorHandlersEvent.Block event) -> ClientEventHandler.setupBlockColors(event::register));
        ForgePrimalWinter.EVENT_BUS.addListener((RegisterColorHandlersEvent.Item event) -> ClientEventHandler.setupItemColors(event::register));
        ForgePrimalWinter.EVENT_BUS.addListener((RegisterParticleProvidersEvent event) -> ClientEventHandler.setupParticleFactories(new ParticleProviderCallback()
        {
            @Override
            public <T extends ParticleOptions> void accept(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider)
            {
                event.registerSpriteSet(type, provider::apply);
            }
        }));

        NeoForge.EVENT_BUS.addListener((ViewportEvent.ComputeFogColor event) -> ClientEventHandler.renderFogColors(event.getCamera(), (float) event.getPartialTick(), (red, green, blue) -> {
            event.setRed(red);
            event.setBlue(blue);
            event.setGreen(green);
        }));

        NeoForge.EVENT_BUS.addListener((ViewportEvent.RenderFog event) -> ClientEventHandler.renderFogDensity(event.getCamera(), (nearPlane, farPlane) -> {
            event.scaleNearPlaneDistance(nearPlane);
            event.scaleFarPlaneDistance(farPlane);
            event.setCanceled(true);
        }));
    }
}
