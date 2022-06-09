package com.alcatrazescapee.primalwinter.mixin.client;

import java.util.Random;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.alcatrazescapee.primalwinter.client.PrimalWinterAmbience;
import com.alcatrazescapee.primalwinter.util.Config;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin
{
    @Shadow @Final private static ResourceLocation SNOW_LOCATION;

    @Shadow private ClientLevel level;
    @Shadow @Final private Minecraft minecraft;
    @Shadow private int ticks;
    @Shadow private int rainSoundTime;

    private int windSoundTime;

    @Redirect(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;warmEnoughToRain(Lnet/minecraft/core/BlockPos;)Z"))
    private boolean alwaysUseRainRendering(Biome biome, BlockPos pos)
    {
        if (Config.INSTANCE.weatherRenderChanges.get())
        {
            return true;
        }
        return biome.warmEnoughToRain(pos);
    }

    @Inject(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;begin(Lcom/mojang/blaze3d/vertex/VertexFormat$Mode;Lcom/mojang/blaze3d/vertex/VertexFormat;)V"))
    private void overrideWithSnowTextures(LightTexture lightTexture, float partialTick, double x, double y, double z, CallbackInfo ci)
    {
        if (Config.INSTANCE.weatherRenderChanges.get())
        {
            RenderSystem.setShaderTexture(0, SNOW_LOCATION);
        }
    }

    @Inject(method = "tickRain", at = @At("HEAD"))
    private void addExtraSnowParticlesAndSounds(Camera camera, CallbackInfo ci)
    {
        final float rain = level.getRainLevel(1f) / (Minecraft.useFancyGraphics() ? 1f : 2f);
        if (rain > 0f)
        {
            final Random random = new Random((long) ticks * 312987231L);
            final BlockPos cameraPos = new BlockPos(camera.getPosition());
            BlockPos pos = null;

            final int particleCount = (int) (100.0F * rain * rain) / (minecraft.options.particles == ParticleStatus.DECREASED ? 2 : 1);
            for (int i = 0; i < particleCount; ++i)
            {
                final BlockPos randomPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, cameraPos.offset(random.nextInt(21) - 10, 0, random.nextInt(21) - 10));
                final Biome biome = level.getBiome(randomPos).value();
                if (randomPos.getY() > level.getMinBuildHeight() && randomPos.getY() <= cameraPos.getY() + 10 && randomPos.getY() >= cameraPos.getY() - 10 && biome.getPrecipitation() == Biome.Precipitation.SNOW && biome.coldEnoughToSnow(randomPos)) // Change: use SNOW and coldEnoughToSnow() instead
                {
                    pos = randomPos.below();
                    if (minecraft.options.particles == ParticleStatus.MINIMAL)
                    {
                        break;
                    }

                    final double dx = random.nextDouble(), dz = random.nextDouble();
                    final BlockState state = level.getBlockState(pos);
                    final FluidState fluid = level.getFluidState(pos);
                    final double blockY = state.getCollisionShape(level, pos).max(Direction.Axis.Y, dx, dz);
                    final double fluidY = fluid.getHeight(level, pos);
                    final ParticleOptions particle = !fluid.is(FluidTags.LAVA) && !state.is(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(state) ? PrimalWinterAmbience.SNOW.get() : ParticleTypes.SMOKE;

                    level.addParticle(particle, pos.getX() + dx, pos.getY() + Math.max(blockY, fluidY), pos.getZ() + dz, 0d, 0d, 0d);
                }
            }

            if (pos != null && random.nextInt(3) < rainSoundTime++)
            {
                rainSoundTime = 0;
                if (pos.getY() > cameraPos.getY() + 1 && level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, cameraPos).getY() > Mth.floor((float) cameraPos.getY()))
                {
                    level.playLocalSound(pos, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.05f, 0.2f, false);
                }
                else
                {
                    level.playLocalSound(pos, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.1f, 0.5f, false);
                }
            }

            // Added
            if (windSoundTime-- < 0 && Config.INSTANCE.windSounds.get())
            {
                final BlockPos playerPos = camera.getBlockPosition();
                final Entity entity = camera.getEntity();
                int light = camera.getEntity().level.getBrightness(LightLayer.SKY, playerPos);
                if (light > 3 && entity.level.isRaining() && entity.level.getBiome(playerPos).value().coldEnoughToSnow(playerPos))
                {
                    // In a windy location, play wind sounds
                    float volumeModifier = 0.2f + (light - 3) * 0.01f;
                    float pitchModifier = 0.7f;
                    if (camera.getFluidInCamera() == FogType.NONE)
                    {
                        pitchModifier = 0.3f;
                    }
                    windSoundTime = 20 * 3 + random.nextInt(30);
                    level.playLocalSound(playerPos, PrimalWinterAmbience.WIND.get(), SoundSource.WEATHER, volumeModifier, pitchModifier, true);
                }
                else
                {
                    windSoundTime += 5; // check a short time later
                }
            }
        }
    }

    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/DimensionSpecialEffects;getSunriseColor(FF)[F"))
    private float[] preventSunriseColors(DimensionSpecialEffects effects, float skyAngle, float tickDelta)
    {
        if (Config.INSTANCE.weatherRenderChanges.get())
        {
            final BlockPos pos = Minecraft.getInstance().gameRenderer.getMainCamera().getBlockPosition();
            final Holder<Biome> biome = level.getBiome(pos);
            if (biome.value().coldEnoughToSnow(pos) && effects instanceof DimensionSpecialEffects.OverworldEffects)
            {
                return null;
            }
        }
        return effects.getSunriseColor(skyAngle, tickDelta);
    }
}
