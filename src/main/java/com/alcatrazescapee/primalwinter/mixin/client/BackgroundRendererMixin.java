/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.client;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.dimension.DimensionType;

import com.alcatrazescapee.primalwinter.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin
{
    //@formatter:off
    @Shadow private static float red;
    @Shadow private static float green;
    @Shadow private static float blue;
    //@formatter:on

    @Inject(method = "render", at = @At("RETURN"))
    private static void render(Camera camera, float tickDelta, ClientWorld world, int i, float f, CallbackInfo ci)
    {
        if (camera.getFocusedEntity() instanceof PlayerEntity && ModConfig.INSTANCE.enableSkyRenderChanges)
        {
            PlayerEntity player = (PlayerEntity) camera.getFocusedEntity();
            BlockPos pos = player.getBlockPos();
            int light = player.world.getLightLevel(LightType.SKY, pos);
            if (light > 3 && player.world.isRaining() && player.world.getBiome(pos).getTemperature(pos) < 0.15f && camera.getSubmergedFluidState().getFluid() == Fluids.EMPTY && player.world.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY)
            {
                // Calculate color based on time of day
                float angle = player.world.getSkyAngleRadians(tickDelta);
                float height = MathHelper.cos(angle);
                float delta = MathHelper.clamp((height + 0.4f) / 0.8f, 0, 1);

                int colorDay = ModConfig.INSTANCE.fogColorDay;
                int colorNight = ModConfig.INSTANCE.fogColorNight;
                red = (((colorDay >> 16) & 0xFF) * delta + ((colorNight >> 16) & 0xFF) * (1 - delta)) / 255f;
                green = (((colorDay >> 8) & 0xFF) * delta + ((colorNight >> 8) & 0xFF) * (1 - delta)) / 255f;
                blue = ((colorDay & 0xFF) * delta + (colorNight & 0xFF) * (1 - delta)) / 255f;

                RenderSystem.clearColor(red, green, blue, 0f);
            }
        }
    }

    /**
     * Override fog density calculations in winter areas
     */
    @SuppressWarnings("deprecation")
    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci)
    {
        if (camera.getFocusedEntity() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) camera.getFocusedEntity();
            BlockPos pos = player.getBlockPos();
            int light = player.world.getLightLevel(LightType.SKY, pos);
            if (light > 3 && player.world.isRaining() && player.world.getBiome(pos).getTemperature(pos) < 0.15f && camera.getSubmergedFluidState().getFluid() == Fluids.EMPTY && player.world.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY)
            {
                float densityConfigValue = (float) ModConfig.INSTANCE.fogDensity;
                float density = ((light - 3) * densityConfigValue / 13f);
                RenderSystem.fogDensity(density);
                ci.cancel();
            }
        }
    }
}
