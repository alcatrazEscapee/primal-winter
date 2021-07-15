/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.world;

import java.util.function.Supplier;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

@SuppressWarnings("unused")
public final class ModFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MOD_ID);

    public static final RegistryObject<ImprovedFreezeTopLayerFeature> FREEZE_TOP_LAYER = register("freeze_top_layer", () -> new ImprovedFreezeTopLayerFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<ImprovedIceSpikeFeature> ICE_SPIKES = register("ice_spikes", () -> new ImprovedIceSpikeFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<ImprovedIcePatchFeature> ICE_PATCH = register("ice_patch", () -> new ImprovedIcePatchFeature(SphereReplaceConfig.CODEC));

    private static <C extends IFeatureConfig, F extends Feature<C>> RegistryObject<F> register(String name, Supplier<F> feature)
    {
        return FEATURES.register(name, feature);
    }
}