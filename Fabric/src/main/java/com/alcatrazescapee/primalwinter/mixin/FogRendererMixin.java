package com.alcatrazescapee.primalwinter.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.alcatrazescapee.primalwinter.client.ClientEventHandler;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin
{
    @Shadow private static float fogRed;
    @Shadow private static float fogBlue;
    @Shadow private static float fogGreen;

    @Inject(method = "setupColor", at = @At("TAIL"))
    private static void modifyFogColor(Camera camera, float partialTick, ClientLevel level, int unused1, float unused2, CallbackInfo ci)
    {
        ClientEventHandler.renderFogColors(camera, partialTick, (red, green, blue) -> {
            fogRed = red;
            fogBlue = blue;
            fogGreen = green;
            RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0f);
        });
    }

    @Inject(method = "setupFog", at = @At("TAIL"))
    private static void modifyFogDensity(Camera camera, FogRenderer.FogMode mode, float unused1, boolean unused2, float unused3, CallbackInfo ci)
    {
        ClientEventHandler.renderFogDensity(camera, (nearPlane, farPlane) -> {
            RenderSystem.setShaderFogStart(nearPlane * RenderSystem.getShaderFogStart());
            RenderSystem.setShaderFogEnd(farPlane * RenderSystem.getShaderFogEnd());
        });
    }
}
