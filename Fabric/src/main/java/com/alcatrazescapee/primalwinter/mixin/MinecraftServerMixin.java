package com.alcatrazescapee.primalwinter.mixin;

import com.alcatrazescapee.primalwinter.platform.Config;
import com.alcatrazescapee.primalwinter.platform.XPlatform;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class, priority = 998)
public abstract class MinecraftServerMixin
{
    /**
     * @see Config#loadWinterBiomes(MinecraftServer)
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void loadBiomesFromConfig(CallbackInfo ci)
    {
        XPlatform.INSTANCE.config().loadWinterBiomes((MinecraftServer) (Object) this);
    }
}
