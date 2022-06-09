package com.alcatrazescapee.primalwinter;

import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.alcatrazescapee.primalwinter.client.ClientEventHandler;
import com.alcatrazescapee.primalwinter.platform.client.ParticleProviderCallback;

public final class ForgePrimalWinterClient
{
    public static void earlySetup()
    {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        modBus.addListener((FMLClientSetupEvent event) -> ClientEventHandler.setupClient());
        modBus.addListener((ColorHandlerEvent.Block event) -> ClientEventHandler.setupBlockColors(event.getBlockColors()::register));
        modBus.addListener((ColorHandlerEvent.Item event) -> ClientEventHandler.setupItemColors(event.getItemColors()::register));
        modBus.addListener((ParticleFactoryRegisterEvent event) -> ClientEventHandler.setupParticleFactories(new ParticleProviderCallback()
        {
            @Override
            public <T extends ParticleOptions> void accept(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider)
            {
                Minecraft.getInstance().particleEngine.register(type, provider::apply);
            }
        }));

        forgeBus.addListener((EntityViewRenderEvent.FogColors event) -> ClientEventHandler.renderFogColors(event.getCamera(), (float) event.getPartialTicks(), (red, green, blue) -> {
            event.setRed(red);
            event.setBlue(blue);
            event.setGreen(green);
        }));

        forgeBus.addListener((EntityViewRenderEvent.RenderFogEvent event) -> ClientEventHandler.renderFogDensity(event.getCamera(), (nearPlane, farPlane) -> {
            event.scaleNearPlaneDistance(nearPlane);
            event.scaleFarPlaneDistance(farPlane);
            event.setCanceled(true);
        }));
    }
}
