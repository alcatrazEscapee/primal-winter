package com.alcatrazescapee.primalwinter;

import java.util.function.Function;

import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import com.alcatrazescapee.primalwinter.client.ClientEventHandler;
import com.alcatrazescapee.primalwinter.platform.client.ParticleProviderCallback;
import com.alcatrazescapee.primalwinter.util.Helpers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.inventory.InventoryMenu;

public final class FabricPrimalWinterClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ClientEventHandler.setupClient();
        ClientEventHandler.setupBlockColors(ColorProviderRegistry.BLOCK::register);
        ClientEventHandler.setupItemColors(ColorProviderRegistry.ITEM::register);

        ClientSpriteRegistryCallback.event(InventoryMenu.BLOCK_ATLAS).register((atlasTexture, registry) -> {
            registry.register(Helpers.identifier("particle/snow_0"));
            registry.register(Helpers.identifier("particle/snow_1"));
            registry.register(Helpers.identifier("particle/snow_2"));
            registry.register(Helpers.identifier("particle/snow_3"));
        });

        ClientEventHandler.setupParticleFactories(new ParticleProviderCallback() {
            @Override
            public <T extends ParticleOptions> void accept(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider)
            {
                ParticleFactoryRegistry.getInstance().register(type, provider::apply);
            }
        });
    }
}
