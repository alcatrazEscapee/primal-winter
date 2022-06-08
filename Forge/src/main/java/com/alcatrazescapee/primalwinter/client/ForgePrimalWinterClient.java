package com.alcatrazescapee.primalwinter.client;

import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.alcatrazescapee.primalwinter.platform.client.ParticleProviderCallback;

public final class ForgePrimalWinterClient
{
    public static void setup()
    {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener((FMLClientSetupEvent event) -> ClientEventHandler.setupClient());
        modBus.addListener((ColorHandlerEvent.Block event) -> ClientEventHandler.setupBlockColors(event.getBlockColors()::register));
        modBus.addListener((ColorHandlerEvent.Item event) -> ClientEventHandler.setupItemColors(event.getItemColors()::register));
        modBus.addListener((ParticleFactoryRegisterEvent event) -> ClientEventHandler.setupParticleFactories(new ParticleProviderCallback() {
            @Override
            public <T extends ParticleOptions> void accept(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider)
            {
                Minecraft.getInstance().particleEngine.register(type, provider::apply);
            }
        }));
    }
}
