/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

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
    private ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryKey, DimensionType dimensionType, Supplier<Profiler> supplier, boolean bl, boolean bl2, long l)
    {
        super(properties, registryKey, dimensionType, supplier, bl, bl2, l);
    }

    /**
     * Place additional snow layers
     */
    @Inject(method = "tickChunk", at = @At(value = "TAIL"))
    public void tickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci)
    {
        if (random.nextInt(16) == 0)
        {
            int blockX = chunk.getPos().getStartX();
            int blockZ = chunk.getPos().getStartZ();
            BlockPos pos = getTopPosition(Heightmap.Type.MOTION_BLOCKING, getRandomPosInChunk(blockX, 0, blockZ, 15));
            Helpers.placeExtraSnowOnTickChunk((ServerWorld) (Object) this, pos);
        }
    }
}
