package com.alcatrazescapee.primalwinter.platform;

import java.util.function.Consumer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface NetworkSetupCallback
{
    /**
     * Register a server to client packet, that is handled on main thread on receipt on client.
     */
    <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<ByteBuf, T> codec, Consumer<T> handler);
}
