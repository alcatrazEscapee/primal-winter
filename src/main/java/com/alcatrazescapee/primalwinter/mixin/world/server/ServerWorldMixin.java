/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.server;

import java.util.function.Supplier;

import net.minecraft.profiler.IProfiler;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ISpawnWorldInfo;

import com.alcatrazescapee.primalwinter.util.Helpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to intercept chunk / environment ticks
 *
 * Will be made superfluous by https://github.com/MinecraftForge/MinecraftForge/pull/7235
 */
@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World
{
    private ServerWorldMixin(ISpawnWorldInfo worldInfo, RegistryKey<World> worldKey, DimensionType dimensionType, Supplier<IProfiler> profiler, boolean b1, boolean b2, long long1)
    {
        super(worldInfo, worldKey, dimensionType, profiler, b1, b2, long1);
    }

    @Inject(method = "tickChunk", at = @At(value = "RETURN"))
    public void inject$tickChunk(Chunk chunk, int randomTickSpeed, CallbackInfo ci)
    {
        Helpers.placeExtraSnowOnTickChunk((ServerWorld) (Object) this, chunk);
    }
}