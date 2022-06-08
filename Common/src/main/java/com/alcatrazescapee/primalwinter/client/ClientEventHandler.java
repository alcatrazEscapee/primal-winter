package com.alcatrazescapee.primalwinter.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FogType;

import com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks;
import com.alcatrazescapee.primalwinter.platform.client.*;
import com.alcatrazescapee.primalwinter.util.Config;

public final class ClientEventHandler
{
    private static float prevFogDensity = 0f;

    public static void setupClient()
    {
        XPlatformClient.INSTANCE.setRenderType(PrimalWinterBlocks.SNOWY_VINE.get(), RenderType.cutout());
    }

    public static void setupBlockColors(BlockColorCallback colors)
    {
        colors.accept((state, world, pos, tintIndex) -> tintIndex == 0 ? FoliageColor.getEvergreenColor() : 0, PrimalWinterBlocks.SNOWY_SPRUCE_LEAVES.get());
        colors.accept((state, world, pos, tintIndex) -> tintIndex == 0 ? FoliageColor.getBirchColor() : 0, PrimalWinterBlocks.SNOWY_BIRCH_LEAVES.get());
        colors.accept((state, world, pos, tintIndex) -> {
            if (tintIndex == 0)
            {
                return world != null && pos != null ? BiomeColors.getAverageFoliageColor(world, pos) : FoliageColor.getDefaultColor();
            }
            return 0;
        }, PrimalWinterBlocks.SNOWY_OAK_LEAVES.get(), PrimalWinterBlocks.SNOWY_DARK_OAK_LEAVES.get(), PrimalWinterBlocks.SNOWY_JUNGLE_LEAVES.get(), PrimalWinterBlocks.SNOWY_ACACIA_LEAVES.get(), PrimalWinterBlocks.SNOWY_VINE.get());
    }

    public static void setupItemColors(ItemColorCallback colors)
    {
        colors.accept((stack, tintIndex) -> {
            if (tintIndex == 0)
            {
                BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
                return Minecraft.getInstance().getBlockColors().getColor(state, null, null, tintIndex);
            }
            return 0;
        }, PrimalWinterBlocks.SNOWY_VINE.get(), PrimalWinterBlocks.SNOWY_OAK_LEAVES.get(), PrimalWinterBlocks.SNOWY_SPRUCE_LEAVES.get(), PrimalWinterBlocks.SNOWY_BIRCH_LEAVES.get(), PrimalWinterBlocks.SNOWY_JUNGLE_LEAVES.get(), PrimalWinterBlocks.SNOWY_ACACIA_LEAVES.get(), PrimalWinterBlocks.SNOWY_DARK_OAK_LEAVES.get());
    }

    public static void setupParticleFactories(ParticleProviderCallback particles)
    {
        particles.accept(PrimalWinterAmbience.SNOW.get(), SnowParticle.Provider::new);
    }

    public static void renderFogColors(Camera camera, float partialTick, FogColorCallback callback)
    {
        if (camera.getEntity() instanceof Player player && camera.getFluidInCamera() == FogType.NONE && prevFogDensity > 0f)
        {
            // Calculate color based on time of day
            final float angle = player.level.getSunAngle(partialTick);
            final float height = Mth.cos(angle);
            final float delta = Mth.clamp((height + 0.4f) / 0.8f, 0, 1);

            final int colorDay = Config.INSTANCE.fogColorDay.get();
            final int colorNight = Config.INSTANCE.fogColorNight.get();
            final float red = ((colorDay >> 16) & 0xFF) * delta + ((colorNight >> 16) & 0xFF) * (1 - delta);
            final float green = ((colorDay >> 8) & 0xFF) * delta + ((colorNight >> 8) & 0xFF) * (1 - delta);
            final float blue = (colorDay & 0xFF) * delta + (colorNight & 0xFF) * (1 - delta);

            callback.accept(red / 255f, green / 255f, blue / 255f);
        }
    }

    public static void renderFogDensity(Camera camera, float partialTick, FogDensityCallback callback)
    {
        if (camera.getEntity() instanceof Player player)
        {
            float expectedFogDensity = 0f;

            final Level level = player.level;
            final Biome biome = level.getBiome(camera.getBlockPosition()).value();
            if (level.isRaining() && biome.getPrecipitation() == Biome.Precipitation.SNOW && biome.coldEnoughToSnow(camera.getBlockPosition()))
            {
                final int light = level.getBrightness(LightLayer.SKY, new BlockPos(player.getEyePosition(partialTick)));
                expectedFogDensity = Mth.clampedMap(light, 3f, 15f, 0f, 1f);
            }

            // Smoothly interpolate fog towards the expected value - increasing faster than it decreases
            if (expectedFogDensity > prevFogDensity)
            {
                prevFogDensity = Math.min(0.1f, prevFogDensity + expectedFogDensity);
            }
            else if (expectedFogDensity < prevFogDensity)
            {
                prevFogDensity = Math.max(prevFogDensity - 0.05f, expectedFogDensity);
            }

            if (camera.getFluidInCamera() != FogType.NONE)
            {
                prevFogDensity = 0; // Immediately cancel fog if there's another fog effect going on
            }

            if (prevFogDensity > 0)
            {
                callback.accept(prevFogDensity * Config.INSTANCE.fogDensity.get().floatValue());
            }
        }
    }
}
