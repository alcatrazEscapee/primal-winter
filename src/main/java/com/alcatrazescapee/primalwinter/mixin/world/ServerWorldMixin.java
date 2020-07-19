package com.alcatrazescapee.primalwinter.mixin.world;

import java.util.function.Supplier;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;

import com.alcatrazescapee.primalwinter.util.Helpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World
{
    private ServerWorldMixin(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l)
    {
        super(mutableWorldProperties, registryKey, registryKey2, dimensionType, profiler, bl, bl2, l);
    }

    /**
     * When the world is about to place snow, also check to place multi-layer snow instead
     */
    @Inject(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;canSetSnow(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"))
    public void primalwinter_tickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci)
    {
        // Recreates the same position about to be ticked for snow
        int blockX = chunk.getPos().getStartX();
        int blockZ = chunk.getPos().getStartZ();
        int seed = this.lcgBlockSeed >> 2;
        BlockPos pos = getTopPosition(Heightmap.Type.MOTION_BLOCKING, new BlockPos(blockX + (seed & 15), (seed >> 16 & 15), blockZ + (seed >> 8 & 15)));
        Helpers.placeExtraSnowOnTickChunk((ServerWorld) (Object) this, pos);
    }
}
