package com.alcatrazescapee.primalwinter.mixin.client;

import java.util.Random;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
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
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
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

    @Redirect(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"))
    private Biome.Precipitation alwaysUseRainRendering(Biome biome, BlockPos pos)
    {
        if (Config.INSTANCE.weatherRenderChanges.getAsBoolean())
        {
            return Biome.Precipitation.RAIN;
        }
        return biome.getPrecipitationAt(pos);
    }

    @Redirect(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I"))
    private int getAdjustedLightColorForSnow(BlockAndTintGetter level, BlockPos pos)
    {
        final int packedLight = LevelRenderer.getLightColor(level, pos);
        if (Config.INSTANCE.weatherRenderChanges.getAsBoolean())
        {
            // Adjusts the light color via a heuristic that mojang uses to make snow appear more white
            // This targets both paths, but since we always use the rain rendering, it's fine.
            final int lightU = packedLight & 0xffff;
            final int lightV = (packedLight >> 16) & 0xffff;
            final int brightLightU = (lightU * 3 + 240) / 4;
            final int brightLightV = (lightV * 3 + 240) / 4;
            return brightLightU | (brightLightV << 16);
        }
        return packedLight;
    }

    @Inject(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;begin(Lcom/mojang/blaze3d/vertex/VertexFormat$Mode;Lcom/mojang/blaze3d/vertex/VertexFormat;)V"))
    private void overrideWithSnowTextures(LightTexture lightTexture, float partialTick, double x, double y, double z, CallbackInfo ci)
    {
        if (Config.INSTANCE.weatherRenderChanges.getAsBoolean())
        {
            RenderSystem.setShaderTexture(0, SNOW_LOCATION);
        }
    }

    // Use slice here to only change constants between "RenderSystem.enableDepthTest();" and "RenderSystem.depthMask(Z);".
    // This is to prevent a crash with OptiFine installed as the mixin was changing another value which caused an ArrayOutOfBounds error.
    @ModifyConstant(method = "renderSnowAndRain", constant = {@Constant(intValue = 5), @Constant(intValue = 10)}, slice = @Slice(
            from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableDepthTest()V"),
            to = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;depthMask(Z)V"))
    )
    private int modifySnowAmount(int constant)
    {
        // This constant is used to control how much snow is rendered - 5 with default, 10 with fancy graphics. By default, we bump this all the way to 15.
        return Config.INSTANCE.snowDensity.getAsInt();
    }

    @Inject(method = "tickRain", at = @At("HEAD"))
    private void addExtraSnowParticlesAndSounds(Camera camera, CallbackInfo ci)
    {
        if (!Config.INSTANCE.snowSounds.getAsBoolean())
        {
            // Prevent default rain/snow sounds by setting rainSoundTime to -1, which means the if() checking it will never pass
            rainSoundTime = -1;
        }

        final float rain = level.getRainLevel(1f) / (Minecraft.useFancyGraphics() ? 1f : 2f);
        if (rain > 0f)
        {
            final Random random = new Random((long) ticks * 312987231L);
            final BlockPos cameraPos = BlockPos.containing(camera.getPosition());
            BlockPos pos = null;

            final int particleCount = (int) (100.0F * rain * rain) / (minecraft.options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);
            for (int i = 0; i < particleCount; ++i)
            {
                final BlockPos randomPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, cameraPos.offset(random.nextInt(21) - 10, 0, random.nextInt(21) - 10));
                final Biome biome = level.getBiome(randomPos).value();
                if (randomPos.getY() > level.getMinBuildHeight() && randomPos.getY() <= cameraPos.getY() + 10 && randomPos.getY() >= cameraPos.getY() - 10 && biome.coldEnoughToSnow(randomPos)) // Change: use SNOW and coldEnoughToSnow() instead
                {
                    pos = randomPos.below();
                    if (minecraft.options.particles().get() == ParticleStatus.MINIMAL)
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
            if (windSoundTime-- < 0 && Config.INSTANCE.windSounds.getAsBoolean())
            {
                final BlockPos playerPos = camera.getBlockPosition();
                final Entity entity = camera.getEntity();
                int light = camera.getEntity().level().getBrightness(LightLayer.SKY, playerPos);
                if (light > 3 && entity.level().isRaining() && entity.level().getBiome(playerPos).value().coldEnoughToSnow(playerPos))
                {
                    // In a windy location, play wind sounds
                    float volumeModifier = 0.2f + (light - 3) * 0.01f;
                    float pitchModifier = 0.7f;
                    if (camera.getFluidInCamera() != FogType.NONE)
                    {
                        pitchModifier = 0.3f;
                    }
                    windSoundTime = 20 * 3 + random.nextInt(30);
                    level.playLocalSound(playerPos, PrimalWinterAmbience.WIND.get(), SoundSource.WEATHER, volumeModifier, pitchModifier, false);
                }
                else
                {
                    windSoundTime += 5; // check a short time later
                }
            }
        }
    }
}
