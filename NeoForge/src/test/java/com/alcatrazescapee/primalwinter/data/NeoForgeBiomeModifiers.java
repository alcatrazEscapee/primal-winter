package com.alcatrazescapee.primalwinter.data;

import com.alcatrazescapee.primalwinter.ForgePrimalWinter;
import com.alcatrazescapee.primalwinter.util.Helpers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class NeoForgeBiomeModifiers
{
    final HolderGetter<PlacedFeature> placedFeatures;

    public NeoForgeBiomeModifiers(BootstrapContext<BiomeModifier> context)
    {
        this.placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        context.register(
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Helpers.identifier("instance")),
            new ForgePrimalWinter.Instance(
                HolderSet.direct(
                    placedFeature(BuiltinPlacedFeatures.ICE_SPIKES),
                    placedFeature(BuiltinPlacedFeatures.ICE_PATCH),
                    placedFeature(BuiltinPlacedFeatures.SNOW_PATCH),
                    placedFeature(BuiltinPlacedFeatures.POWDER_SNOW_PATCH)
                ),
                HolderSet.direct(
                    placedFeature(BuiltinPlacedFeatures.FREEZE_TOP_LAYER)
                )
            )
        );
    }

    Holder<PlacedFeature> placedFeature(ResourceKey<PlacedFeature> key)
    {
        return placedFeatures.getOrThrow(key);
    }
}
