package com.alcatrazescapee.primalwinter.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.ImmutableSet;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public record ConfigPacket(Collection<ResourceKey<Level>> winterDimensions) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<ConfigPacket> TYPE = new CustomPacketPayload.Type<>(Helpers.identifier("config"));
    public static final StreamCodec<ByteBuf, ConfigPacket> CODEC = ResourceKey.streamCodec(Registries.DIMENSION)
        .<Collection<ResourceKey<Level>>>apply(ByteBufCodecs.collection(ArrayList::new))
        .map(ConfigPacket::new, ConfigPacket::winterDimensions);

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
