package com.alcatrazescapee.primalwinter;

import java.util.function.Function;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import com.alcatrazescapee.primalwinter.client.ClientEventHandler;
import com.alcatrazescapee.primalwinter.platform.client.ParticleProviderCallback;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public final class FabricPrimalWinterClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ClientEventHandler.setupClient();
        ClientEventHandler.setupBlockColors(ColorProviderRegistry.BLOCK::register);
        ClientEventHandler.setupItemColors(ColorProviderRegistry.ITEM::register);
        ClientEventHandler.setupParticleFactories(new ParticleProviderCallback() {
            @Override
            public <T extends ParticleOptions> void accept(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider)
            {
                ParticleFactoryRegistry.getInstance().register(type, provider::apply);
            }
        });
    }
}
