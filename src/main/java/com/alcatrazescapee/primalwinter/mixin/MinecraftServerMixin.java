package com.alcatrazescapee.primalwinter.mixin;

import java.util.Map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.World;

import com.alcatrazescapee.primalwinter.util.Helpers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantThreadExecutor<ServerTask>
{
    @Shadow
    @Final
    private Map<RegistryKey<World>, ServerWorld> worlds;

    private MinecraftServerMixin(String string)
    {
        super(string);
    }

    /**
     * On world load, set the overworld to endless storm
     */
    @Inject(method = "createWorlds", at = @At("RETURN"))
    public void primalwinter_createWorlds(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci)
    {
        Helpers.setWeatherToEndlessWinter(worlds.get(World.OVERWORLD));
    }
}
