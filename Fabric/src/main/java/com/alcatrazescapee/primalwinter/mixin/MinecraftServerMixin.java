package com.alcatrazescapee.primalwinter.mixin;

import com.alcatrazescapee.primalwinter.FabricPrimalWinter;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class, priority = 500) // Before Fabric API's mixin into the server, so we come first
public class MinecraftServerMixin
{
    @Inject(method = "<init>", at = @At("RETURN"))
    private void captureServerEarly(CallbackInfo ci)
    {
        FabricPrimalWinter.server = (MinecraftServer) (Object) this;
    }
}
