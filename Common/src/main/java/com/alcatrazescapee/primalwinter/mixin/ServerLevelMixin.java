package com.alcatrazescapee.primalwinter.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;

import com.alcatrazescapee.primalwinter.util.Helpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin
{
    @Inject(method = "tickChunk", at = @At(value = "RETURN"))
    public void placeExtraSnow(LevelChunk chunk, int tickSpeed, CallbackInfo ci)
    {
        Helpers.placeExtraSnow((ServerLevel) (Object) this, chunk);
    }
}
