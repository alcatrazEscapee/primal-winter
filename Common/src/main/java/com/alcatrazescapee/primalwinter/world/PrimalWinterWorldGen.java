package com.alcatrazescapee.primalwinter.world;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.DiskFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks;
import com.alcatrazescapee.primalwinter.platform.RegistryHolder;
import com.alcatrazescapee.primalwinter.platform.RegistryInterface;
import com.alcatrazescapee.primalwinter.platform.XPlatform;

public final class PrimalWinterWorldGen
{
    public static final class Features
    {
        public static final RegistryInterface<Feature<?>> FEATURES = XPlatform.INSTANCE.registryInterface(Registry.FEATURE);

        public static final RegistryHolder<ImprovedFreezeTopLayerFeature> FREEZE_TOP_LAYER = register("freeze_top_layer", ImprovedFreezeTopLayerFeature::new, NoneFeatureConfiguration.CODEC);
        public static final RegistryHolder<ImprovedIceSpikeFeature> ICE_SPIKES = register("ice_spikes", ImprovedIceSpikeFeature::new, NoneFeatureConfiguration.CODEC);
        public static final RegistryHolder<DiskFeature> DISK = register("disk", DiskFeature::new, DiskConfiguration.CODEC);

        private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryHolder<F> register(String name, Function<Codec<C>, F> feature, Codec<C> codec)
        {
            return FEATURES.register(name, () -> feature.apply(codec));
        }
    }

    public static final class Configured
    {
        public static final RegistryInterface<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = XPlatform.INSTANCE.registryInterface(BuiltinRegistries.CONFIGURED_FEATURE);

        public static final RegistryHolder<ConfiguredFeature<?, ?>> FREEZE_TOP_LAYER = register("freeze_top_layer", Features.FREEZE_TOP_LAYER, () -> NoneFeatureConfiguration.INSTANCE);
        public static final RegistryHolder<ConfiguredFeature<?, ?>> ICE_SPIKES = register("ice_spikes", Features.ICE_SPIKES, () -> NoneFeatureConfiguration.INSTANCE);

        public static final RegistryHolder<ConfiguredFeature<?, ?>> ICE_PATCH = register("ice_patch", Features.DISK, () -> new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.PACKED_ICE), snowyReplaceableBlocks(), UniformInt.of(2, 3), 1));
        public static final RegistryHolder<ConfiguredFeature<?, ?>> SNOW_PATCH = register("snow_patch", Features.DISK, () -> new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SNOW_BLOCK), snowyReplaceableBlocks(), UniformInt.of(2, 3), 1));
        public static final RegistryHolder<ConfiguredFeature<?, ?>> POWDER_SNOW_PATCH = register("powder_snow_patch", Features.DISK, () -> new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.POWDER_SNOW), snowyReplaceableBlocks(), UniformInt.of(2, 3), 1));

        private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryHolder<ConfiguredFeature<?, ?>> register(String name, Supplier<F> feature, Supplier<C> config)
        {
            return CONFIGURED_FEATURES.register(name, () -> new ConfiguredFeature<>(feature.get(), config.get()));
        }

        private static BlockPredicate snowyReplaceableBlocks()
        {
            return BlockPredicate.matchesBlocks(
                Blocks.DIRT,
                Blocks.GRASS_BLOCK,
                Blocks.PODZOL,
                Blocks.COARSE_DIRT,
                Blocks.MYCELIUM,
                Blocks.SNOW_BLOCK,
                Blocks.ICE,
                Blocks.SAND,
                Blocks.RED_SAND,
                PrimalWinterBlocks.SNOWY_DIRT.get(),
                PrimalWinterBlocks.SNOWY_COARSE_DIRT.get(),
                PrimalWinterBlocks.SNOWY_SAND.get(),
                PrimalWinterBlocks.SNOWY_RED_SAND.get());
        }
    }

    public static final class Placed
    {
        public static final RegistryInterface<PlacedFeature> PLACED_FEATURES = XPlatform.INSTANCE.registryInterface(BuiltinRegistries.PLACED_FEATURE);

        public static final RegistryHolder<PlacedFeature> FREEZE_TOP_LAYER = register("freeze_top_layer", Configured.FREEZE_TOP_LAYER);
        public static final RegistryHolder<PlacedFeature> ICE_SPIKES = register("ice_spikes", Configured.ICE_SPIKES, RarityFilter.onAverageOnceEvery(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());
        public static final RegistryHolder<PlacedFeature> ICE_PATCH = register("ice_patch", Configured.ICE_PATCH, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        public static final RegistryHolder<PlacedFeature> SNOW_PATCH = register("snow_patch", Configured.SNOW_PATCH, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());
        public static final RegistryHolder<PlacedFeature> POWDER_SNOW_PATCH = register("powder_snow_patch", Configured.POWDER_SNOW_PATCH, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        private static RegistryHolder<PlacedFeature> register(String name, RegistryHolder<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers)
        {
            return PLACED_FEATURES.register(name, () -> new PlacedFeature(Holder.hackyErase(feature.holder()), Arrays.asList(modifiers)));
        }
    }
}
