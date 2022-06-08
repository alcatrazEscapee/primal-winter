package com.alcatrazescapee.primalwinter.world;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import com.alcatrazescapee.primalwinter.platform.RegistryHolder;
import com.alcatrazescapee.primalwinter.platform.RegistryInterface;
import com.alcatrazescapee.primalwinter.platform.XPlatform;
import com.alcatrazescapee.primalwinter.util.Helpers;
import com.mojang.serialization.Codec;

public final class PrimalWinterWorldGen
{
    public static final class Features
    {
        public static final RegistryInterface<Feature<?>> FEATURES = XPlatform.INSTANCE.registryInterface(Registry.FEATURE);

        public static final RegistryHolder<ImprovedFreezeTopLayerFeature> FREEZE_TOP_LAYER = register("freeze_top_layer", ImprovedFreezeTopLayerFeature::new, NoneFeatureConfiguration.CODEC);
        public static final RegistryHolder<ImprovedIceSpikeFeature> ICE_SPIKES = register("ice_spikes", ImprovedIceSpikeFeature::new, NoneFeatureConfiguration.CODEC);
        public static final RegistryHolder<ImprovedIcePatchFeature> ICE_PATCH = register("ice_patch", ImprovedIcePatchFeature::new, DiskConfiguration.CODEC);

        private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryHolder<F> register(String name, Function<Codec<C>, F> feature, Codec<C> codec)
        {
            return FEATURES.register(name, () -> feature.apply(codec));
        }
    }

    public static final class Configured
    {
        public static final RegistryInterface<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = XPlatform.INSTANCE.registryInterface(BuiltinRegistries.CONFIGURED_FEATURE);

        public static final RegistryHolder<ConfiguredFeature<?, ?>> FREEZE_TOP_LAYER = register("freeze_top_layer", Features.FREEZE_TOP_LAYER, NoneFeatureConfiguration.INSTANCE);
        public static final RegistryHolder<ConfiguredFeature<?, ?>> ICE_SPIKES = register("ice_spikes", Features.ICE_SPIKES, NoneFeatureConfiguration.INSTANCE);
        public static final RegistryHolder<ConfiguredFeature<?, ?>> ICE_PATCH = register("ice_patch", Features.ICE_PATCH, new DiskConfiguration(Blocks.PACKED_ICE.defaultBlockState(), UniformInt.of(2, 3), 1, List.of(Blocks.DIRT.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState(), Blocks.PODZOL.defaultBlockState(), Blocks.COARSE_DIRT.defaultBlockState(), Blocks.MYCELIUM.defaultBlockState(), Blocks.SNOW_BLOCK.defaultBlockState(), Blocks.ICE.defaultBlockState())));

        private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryHolder<ConfiguredFeature<?, ?>> register(String name, Supplier<F> feature, C config)
        {
            return CONFIGURED_FEATURES.register(name, () -> new ConfiguredFeature<>(feature.get(), config));
        }
    }

    public static final class Placed
    {
        public static final RegistryInterface<PlacedFeature> PLACED_FEATURES = XPlatform.INSTANCE.registryInterface(BuiltinRegistries.PLACED_FEATURE);

        public static final RegistryHolder<PlacedFeature> FREEZE_TOP_LAYER = register("freeze_top_layer", Configured.FREEZE_TOP_LAYER);
        public static final RegistryHolder<PlacedFeature> ICE_SPIKES = register("ice_spikes", Configured.ICE_SPIKES);
        public static final RegistryHolder<PlacedFeature> ICE_PATCH = register("ice_patch", Configured.ICE_PATCH);

        private static RegistryHolder<PlacedFeature> register(String name, RegistryHolder<ConfiguredFeature<?, ?>> feature)
        {
            return PLACED_FEATURES.register(name, () -> new PlacedFeature(Holder.hackyErase(feature.holder()), new ArrayList<>()));
        }
    }

    /**
     * For certain vanilla features which do a check at the starting position, if it's a full block of snow (and thus only generating in very specific locations), we need to fake the initial conditions for other biomes.
     */
    public static void adjustPosForIceFeature(FeaturePlaceContext<?> context)
    {
        final WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        while (level.isEmptyBlock(pos) && pos.getY() > level.getMinBuildHeight() + 2)
        {
            pos = pos.below();
        }
        final BlockState originalState = level.getBlockState(pos);
        if (!Helpers.is(originalState, BlockTags.LEAVES) && !Helpers.is(originalState, BlockTags.LOGS))
        {
            level.setBlock(pos, Blocks.SNOW_BLOCK.defaultBlockState(), 2);
        }
    }
}
