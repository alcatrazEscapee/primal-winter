package com.alcatrazescapee.primalwinter.platform;

import java.nio.file.Path;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.network.PacketDistributor;

public final class ForgePlatform implements XPlatform
{
    private final ForgeConfig config = new ForgeConfig();

    @Override
    public <T> RegistryInterface<T> registryInterface(Registry<T> registry)
    {
        return new ForgeRegistryInterface<>(registry);
    }

    @Override
    public CreativeModeTab.Builder creativeTab()
    {
        return CreativeModeTab.builder();
    }

    @Override
    public Path configDir()
    {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Config config()
    {
        return config;
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CustomPacketPayload packet)
    {
        PacketDistributor.sendToPlayer(player, packet);
    }

    @Override
    public void sendToAll(MinecraftServer server, CustomPacketPayload packet)
    {
        PacketDistributor.sendToAllPlayers(packet);
    }
}
