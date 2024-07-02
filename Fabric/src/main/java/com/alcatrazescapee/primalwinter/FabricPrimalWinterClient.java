package com.alcatrazescapee.primalwinter;

import java.util.function.Consumer;
import java.util.function.Function;

import com.alcatrazescapee.primalwinter.platform.NetworkSetupCallback;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import com.alcatrazescapee.primalwinter.client.ClientEventHandler;
import com.alcatrazescapee.primalwinter.platform.client.ParticleProviderCallback;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

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
        PrimalWinter.networkingSetup(new NetworkSetupCallback() {
            @Override
            public <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<ByteBuf, T> codec, Consumer<T> handler)
            {
                ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> context.client().execute(() -> handler.accept(payload)));
            }
        });
    }
}
