package com.alcatrazescapee.primalwinter.world;

import java.util.function.Function;
import com.alcatrazescapee.primalwinter.util.Helpers;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.DiskFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import com.alcatrazescapee.primalwinter.platform.RegistryHolder;
import com.alcatrazescapee.primalwinter.platform.RegistryInterface;
import com.alcatrazescapee.primalwinter.platform.XPlatform;

@SuppressWarnings("unused")
public final class PrimalWinterFeatures
{
    public static final RegistryInterface<Feature<?>> FEATURES = XPlatform.INSTANCE.registryInterface(BuiltInRegistries.FEATURE);

    public static final RegistryHolder<ImprovedFreezeTopLayerFeature> FREEZE_TOP_LAYER = register("freeze_top_layer", ImprovedFreezeTopLayerFeature::new, NoneFeatureConfiguration.CODEC);
    public static final RegistryHolder<ImprovedIceSpikeFeature> ICE_SPIKES = register("ice_spikes", ImprovedIceSpikeFeature::new, NoneFeatureConfiguration.CODEC);
    public static final RegistryHolder<DiskFeature> DISK = register("disk", DiskFeature::new, DiskConfiguration.CODEC);

    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryHolder<F> register(String name, Function<Codec<C>, F> feature, Codec<C> codec)
    {
        return FEATURES.register(name, () -> feature.apply(codec));
    }

    public static final class Keys
    {
        public static final ResourceKey<PlacedFeature> FREEZE_TOP_LAYER = key("freeze_top_layer");
        public static final ResourceKey<PlacedFeature> ICE_SPIKES = key("ice_spikes");
        public static final ResourceKey<PlacedFeature> ICE_PATCH = key("ice_patch");
        public static final ResourceKey<PlacedFeature> SNOW_PATCH = key("snow_patch");
        public static final ResourceKey<PlacedFeature> POWDER_SNOW_PATCH = key("powder_snow_patch");

        private static ResourceKey<PlacedFeature> key(String name)
        {
            return ResourceKey.create(Registries.PLACED_FEATURE, Helpers.identifier(name));
        }
    }
}
