package com.alcatrazescapee.primalwinter.platform;

import java.nio.file.Path;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;

import net.fabricmc.loader.api.FabricLoader;

public final class FabricPlatform implements XPlatform
{
    @Override
    public <T> RegistryInterface<T> registryInterface(Registry<T> registry)
    {
        return new FabricRegistryInterface<>(registry);
    }

    @Override
    public CreativeModeTab.Builder creativeTab()
    {
        return FabricItemGroup.builder();
    }

    @Override
    public Path configDir()
    {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CustomPacketPayload packet)
    {
        ServerPlayNetworking.send(player, packet);
    }

    @Override
    public void sendToAll(MinecraftServer server, CustomPacketPayload packet)
    {
        PlayerLookup.all(server).forEach(player -> ServerPlayNetworking.send(player, packet));
    }
}
