package com.alcatrazescapee.primalwinter.data;

import java.util.List;
import com.alcatrazescapee.primalwinter.util.Helpers;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import static net.minecraft.world.level.levelgen.placement.BiomeFilter.*;
import static net.minecraft.world.level.levelgen.placement.HeightmapPlacement.*;
import static net.minecraft.world.level.levelgen.placement.InSquarePlacement.*;
import static net.minecraft.world.level.levelgen.placement.RarityFilter.*;

public final class BuiltinPlacedFeatures
{
    public static final ResourceKey<PlacedFeature> FREEZE_TOP_LAYER = key("freeze_top_layer");
    public static final ResourceKey<PlacedFeature> ICE_PATCH = key("ice_patch");
    public static final ResourceKey<PlacedFeature> ICE_SPIKES = key("ice_spikes");
    public static final ResourceKey<PlacedFeature> POWDER_SNOW_PATCH = key("powder_snow_patch");
    public static final ResourceKey<PlacedFeature> SNOW_PATCH = key("snow_patch");

    private static ResourceKey<PlacedFeature> key(String id)
    {
        return ResourceKey.create(Registries.PLACED_FEATURE, Helpers.identifier(id));
    }

    final BootstrapContext<PlacedFeature> context;
    final HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures;

    public BuiltinPlacedFeatures(BootstrapContext<PlacedFeature> context)
    {
        this.context = context;
        this.configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(FREEZE_TOP_LAYER, BuiltinFeatures.FREEZE_TOP_LAYER);
        register(ICE_PATCH, BuiltinFeatures.ICE_PATCH,
            spread(),
            onHeightmap(Heightmap.Types.OCEAN_FLOOR),
            biome());
        register(ICE_SPIKES, BuiltinFeatures.ICE_SPIKES,
            onAverageOnceEvery(8),
            spread(),
            onHeightmap(Heightmap.Types.OCEAN_FLOOR),
            biome());
        register(POWDER_SNOW_PATCH, BuiltinFeatures.POWDER_SNOW_PATCH,
            spread(),
            onHeightmap(Heightmap.Types.MOTION_BLOCKING),
            biome());
        register(SNOW_PATCH, BuiltinFeatures.SNOW_PATCH,
            spread(),
            onHeightmap(Heightmap.Types.OCEAN_FLOOR),
            biome());
    }

    void register(ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... placements)
    {
        context.register(key, new PlacedFeature(configuredFeatures.getOrThrow(feature), List.of(placements)));
    }
}
