package com.alcatrazescapee.primalwinter.platform;

import java.util.Collection;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import com.alcatrazescapee.primalwinter.client.ReloadableLevelRenderer;
import com.alcatrazescapee.primalwinter.util.ConfigPacket;
import com.google.common.collect.ImmutableSet;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.CheckReturnValue;
import org.slf4j.Logger;

@SuppressWarnings("NotNullFieldNotInitialized") // Initialized by subclasses
public abstract class Config
{
    public static final Logger LOGGER = LogUtils.getLogger();

    // Common
    public BooleanSupplier enableWeatherCommand;

    public BooleanSupplier enableSnowAccumulationDuringWorldgen;
    public BooleanSupplier enableSnowAccumulationDuringWeather;

    // Client
    public DoubleSupplier fogDensity;
    public IntSupplier snowDensity;
    public BooleanSupplier windSounds;
    public BooleanSupplier snowSounds;

    public IntSupplier fogColorDay;
    public IntSupplier fogColorNight;

    // Server Synced
    private Set<ResourceKey<Biome>> winterBiomesView = Set.of();
    private Set<ResourceKey<Level>> winterDimensionsView = Set.of();

    public final void loadWinterBiomes(MinecraftServer server)
    {
        winterDimensionsView = ImmutableSet.copyOf(winterDimensions());
        winterBiomesView = ImmutableSet.copyOf(server.registryAccess()
            .registryOrThrow(Registries.LEVEL_STEM)
            .entrySet()
            .stream()
            .filter(e -> isWinterDimension(ResourceKey.create(Registries.DIMENSION, e.getKey().location())))
            .flatMap(e -> e.getValue()
                .generator()
                .getBiomeSource()
                .possibleBiomes()
                .stream())
            .flatMap(holder -> holder.unwrapKey().stream())
            .toList());
        LOGGER.info("Loaded winter dimensions={}, biomes={}", winterDimensionsView.size(), winterBiomesView.size());
    }

    @CheckReturnValue
    public final ConfigPacket createSyncPacket()
    {
        return new ConfigPacket(winterDimensionsView);
    }

    public final void onSync(ConfigPacket packet)
    {
        winterDimensionsView = packet.winterDimensions();
        ((ReloadableLevelRenderer) Minecraft.getInstance().levelRenderer).primalWinter$reload();
    }

    /**
     * @return {@code true} if this dimensions is a winter dimension.
     */
    public final boolean isWinterDimension(ResourceKey<Level> dimension)
    {
        return winterDimensionsView.contains(dimension);
    }

    /**
     * @return {@code true} if this is a winter biome, when queried from server. <strong>Not valid on client!</strong>
     */
    public final boolean isWinterBiome(ResourceKey<Biome> biome)
    {
        assert winterDimensionsView.isEmpty() || !winterBiomesView.isEmpty(); // Config loaded check, allowing for no enabled winter dimensions
        return winterBiomesView.contains(biome);
    }

    protected abstract Collection<ResourceKey<Level>> winterDimensions();
}
