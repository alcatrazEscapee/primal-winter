package com.alcatrazescapee.primalwinter.mixin;

import com.alcatrazescapee.primalwinter.platform.XPlatform;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Both NeoForge + Fabric biome modifications happen earlier than any relevant events posted from both loaders. So,
 * in order to initialize server-based config elements we inject into the server constructor, as soon as registries
 * are ready, in order to do pre-initialization
 */
@Mixin(value = MinecraftServer.class, priority = 500)  // Before Fabric API's mixin into the server, so we come first
public abstract class MinecraftServerMixin
{
    @Inject(method = "<init>", at = @At("RETURN"))
    private void loadBiomesFromConfig(CallbackInfo ci)
    {
        XPlatform.INSTANCE.config().loadWinterBiomes((MinecraftServer) (Object) this);
    }
}
