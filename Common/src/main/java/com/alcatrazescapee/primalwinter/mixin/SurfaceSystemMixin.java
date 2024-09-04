package com.alcatrazescapee.primalwinter.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(SurfaceSystem.class)
public abstract class SurfaceSystemMixin
{
    @WrapOperation(
        method = "buildSurface",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;is(Lnet/minecraft/resources/ResourceKey;)Z", ordinal = 0),
        slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/SurfaceSystem;erodedBadlandsExtension(Lnet/minecraft/world/level/chunk/BlockColumn;IIILnet/minecraft/world/level/LevelHeightAccessor;)V"))
    )
    private boolean useIcebergExtensionOnAllOceans(Holder<Biome> biome, ResourceKey<Biome> key, Operation<Boolean> original)
    {
        return biome.is(BiomeTags.IS_OCEAN) || original.call(biome, key);
    }
}
