package com.alcatrazescapee.primalwinter.mixin;

import com.alcatrazescapee.primalwinter.platform.Config;
import com.alcatrazescapee.primalwinter.platform.ForgePlatform;
import com.alcatrazescapee.primalwinter.platform.XPlatform;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLifecycleHooks.class)
public class ServerLifecycleHooksMixin
{
    /**
     * @see Config#loadWinterBiomes(MinecraftServer)
     */
    @Inject(
        method = "handleServerAboutToStart",
        at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/server/ServerLifecycleHooks;runModifiers(Lnet/minecraft/server/MinecraftServer;)V", remap = false),
        cancellable = true,
        remap = false
    )
    private static void preventDoublePostingOfEvent(MinecraftServer server, CallbackInfo ci)
    {
        NeoForge.EVENT_BUS.post(new ServerAboutToStartEvent(server)); // Event First (TerraBlender)
        XPlatform.INSTANCE.config().loadWinterBiomes(server); // Primal Winter second
        ForgePlatform.runBiomeModifiers(server); // Biome modifiers third
        ci.cancel(); // Avoid the rest of the method
    }
}
