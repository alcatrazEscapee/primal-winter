package com.alcatrazescapee.primalwinter.data;

import java.util.function.Supplier;
import com.alcatrazescapee.primalwinter.util.Helpers;
import com.alcatrazescapee.primalwinter.world.PrimalWinterFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;

public final class BuiltinFeatures
{
    public static final ResourceKey<ConfiguredFeature<?, ?>> FREEZE_TOP_LAYER = key("freeze_top_layer");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_PATCH = key("ice_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_SPIKES = key("ice_spikes");
    public static final ResourceKey<ConfiguredFeature<?, ?>> POWDER_SNOW_PATCH = key("powder_snow_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SNOW_PATCH = key("snow_patch");

    static ResourceKey<ConfiguredFeature<?, ?>> key(String id)
    {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Helpers.identifier(id));
    }

    final BootstrapContext<ConfiguredFeature<?, ?>> context;

    public BuiltinFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context)
    {
        this.context = context;

        register(FREEZE_TOP_LAYER, PrimalWinterFeatures.FREEZE_TOP_LAYER, NoneFeatureConfiguration.NONE);
        register(ICE_PATCH, PrimalWinterFeatures.DISK, disk(Blocks.ICE));
        register(ICE_SPIKES, PrimalWinterFeatures.ICE_SPIKES, NoneFeatureConfiguration.NONE);
        register(POWDER_SNOW_PATCH, PrimalWinterFeatures.DISK, disk(Blocks.POWDER_SNOW));
        register(SNOW_PATCH, PrimalWinterFeatures.DISK, disk(Blocks.SNOW_BLOCK));
    }

    DiskConfiguration disk(Block block)
    {
        return new DiskConfiguration(
            RuleBasedBlockStateProvider.simple(block),
            BlockPredicate.matchesTag(BuiltinTags.Block.REPLACEABLE_WITH_SNOWY_STUFF),
            UniformInt.of(2, 3), 1
        );
    }

    <C extends FeatureConfiguration> void register(ResourceKey<ConfiguredFeature<?, ?>> key, Supplier<? extends Feature<C>> feature, C config)
    {
        context.register(key, new ConfiguredFeature<>(feature.get(), config));
    }
}
