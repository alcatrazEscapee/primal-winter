package com.alcatrazescapee.primalwinter.util;

import java.util.Set;
import java.util.stream.Collectors;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public record ConfigPacket(Set<ResourceKey<Level>> winterDimensions) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<ConfigPacket> TYPE = new CustomPacketPayload.Type<>(Helpers.identifier("config"));
    public static final StreamCodec<ByteBuf, ConfigPacket> CODEC = ResourceLocation.STREAM_CODEC
        .apply(ByteBufCodecs.list())
        .map(
            list -> new ConfigPacket(list.stream().map(id -> ResourceKey.create(Registries.DIMENSION, id)).collect(Collectors.toSet())),
            packet -> packet.winterDimensions.stream().map(ResourceKey::location).toList()
        );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
