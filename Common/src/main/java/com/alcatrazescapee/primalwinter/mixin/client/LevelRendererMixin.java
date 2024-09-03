package com.alcatrazescapee.primalwinter.mixin.client;

import com.alcatrazescapee.primalwinter.client.PrimalWinterAmbience;
import com.alcatrazescapee.primalwinter.client.ReloadableLevelRenderer;
import com.alcatrazescapee.primalwinter.platform.XPlatform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin
    implements ReloadableLevelRenderer
{
    @Shadow @Final private static ResourceLocation SNOW_LOCATION;

    @Shadow private ClientLevel level;
    @Shadow @Final private Minecraft minecraft;
    @Shadow private int ticks;
    @Shadow private int rainSoundTime;

    @Shadow @Final private float[] rainSizeX;
    @Shadow @Final private float[] rainSizeZ;

    @Unique private int primalWinter$windSoundTime;
    @Unique private boolean primalWinter$isWinterDimension;

    @Override
    public void primalWinter$reload()
    {
        primalWinter$isWinterDimension = level != null && XPlatform.INSTANCE.config().isWinterDimension(level.dimension());
    }

    /**
     * Record, when the level is set, if this dimension is set to have winter render effects.
     */
    @Inject(method = "setLevel", at = @At("RETURN"))
    private void checkIfLevelIsWinter(@Nullable ClientLevel level, CallbackInfo ci)
    {
        primalWinter$reload();
    }

    /**
     * Replace snow and rain rendering, if {@code primalWinter$isWinterDimension}, with a variant that is based on "rain, but heavier,
     * using the snow textures". We want to avoid any changes to the code path in non-winter dimensions, but persist all changes in winter
     * dimensions. Duplicating this path seems to be the easiest way to do that, without running into issues with other injections, or accidentally
     * modifying other weather rendering call paths.
     */
    @Inject(method = "renderSnowAndRain", at = @At("HEAD"), cancellable = true)
    private void renderFastSnow(LightTexture lightTexture, float partialTick, double srcCamX, double srcCamY, double srcCamZ, CallbackInfo ci)
    {
        if (!primalWinter$isWinterDimension)
        {
            return; // Take the normal path
        }

        final Tesselator tesselator = Tesselator.getInstance();
        final Level level = this.minecraft.level;
        final float camX = (float) srcCamX;
        final float camY = (float) srcCamY;
        final float camZ = (float) srcCamZ;
        final int blockX = Mth.floor(camX);
        final int blockY = Mth.floor(camY);
        final int blockZ = Mth.floor(camZ);
        final int particleAmount = XPlatform.INSTANCE.config().snowDensity.getAsInt();
        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        assert level != null;

        BufferBuilder buffer = null;
        boolean renderedAny = false;

        lightTexture.turnOnLightLayer();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        RenderSystem.setShader(GameRenderer::getParticleShader);

        for (int z = blockZ - particleAmount; z <= blockZ + particleAmount; z++)
        {
            for (int x = blockX - particleAmount; x <= blockX + particleAmount; x++)
            {
                final int rainSizeIndex = (z - blockZ + 16) * 32 + x - blockX + 16;
                final float rainSizeX = this.rainSizeX[rainSizeIndex] * 0.5F;
                final float rainSizeZ = this.rainSizeZ[rainSizeIndex] * 0.5F;
                final int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                int minY = blockY - particleAmount;
                int maxY = blockY + particleAmount;

                if (minY < y)
                {
                    minY = y;
                }

                if (maxY < y)
                {
                    maxY = y;
                }

                final int targetY = Math.max(y, blockY);

                if (minY != maxY)
                {
                    if (!renderedAny)
                    {
                        renderedAny = true;
                        RenderSystem.setShaderTexture(0, SNOW_LOCATION);
                        buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                    }

                    final RandomSource random = RandomSource.create(x * x * 3121L + x * 45238971L ^ z * z * 418711L + z * 13761L);
                    final int tickOffset = this.ticks & 131071;
                    final int posOffset = x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 0xFF;
                    final float textureV = -((float) (tickOffset + posOffset) + partialTick) / 32.0F * (3.0F + random.nextFloat()) % 32.0F;

                    // Calculate the alpha, based on the rain level (fade in/out), and the distance to the camera
                    // We don't bother calculating the rain level, since in practice it should always be 1.0f
                    final double offsetX = x + 0.5 - camX;
                    final double offsetZ = z + 0.5 - camZ;
                    final float offsetXZ = (float) Math.sqrt(offsetX * offsetX + offsetZ * offsetZ) / particleAmount;
                    final float alpha = (1.0F - offsetXZ * offsetXZ) * 0.5F + 0.5F;

                    cursor.set(x, targetY, z);
                    final int packedLight = LevelRenderer.getLightColor(level, cursor);

                    // Adjusts the light color via a heuristic that mojang uses to make snow appear more white
                    // This targets both paths, but since we always use the rain rendering, it's fine.
                    final int lightU = packedLight & 0xffff;
                    final int lightV = (packedLight >> 16) & 0xffff;
                    final int brightLightU = (lightU * 3 + 240) / 4;
                    final int brightLightV = (lightV * 3 + 240) / 4;

                    buffer.addVertex(x - camX - rainSizeX + 0.5F, maxY - camY, z - camZ - rainSizeZ + 0.5F)
                        .setUv(0.0F, minY * 0.25F + textureV)
                        .setColor(1.0F, 1.0F, 1.0F, alpha)
                        .setUv2(brightLightU, brightLightV);
                    buffer.addVertex(x - camX + rainSizeX + 0.5F, maxY - camY, z - camZ + rainSizeZ + 0.5F)
                        .setUv(1.0F, minY * 0.25F + textureV)
                        .setColor(1.0F, 1.0F, 1.0F, alpha)
                        .setUv2(brightLightU, brightLightV);
                    buffer.addVertex(x - camX + rainSizeX + 0.5F, minY - camY, z - camZ + rainSizeZ + 0.5F)
                        .setUv(1.0F, maxY * 0.25F + textureV)
                        .setColor(1.0F, 1.0F, 1.0F, alpha)
                        .setUv2(brightLightU, brightLightV);
                    buffer.addVertex(x - camX - rainSizeX + 0.5F, minY - camY, z - camZ - rainSizeZ + 0.5F)
                        .setUv(0.0F, maxY * 0.25F + textureV)
                        .setColor(1.0F, 1.0F, 1.0F, alpha)
                        .setUv2(brightLightU, brightLightV);
                }
            }
        }

        if (renderedAny)
        {
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        lightTexture.turnOffLightLayer();
        ci.cancel();
    }

    /**
     * Replace rain sounds and particles with modified snow particles, snow and wind sounds, if {@code primalWinter$isWinterDimension}. Like the above method,
     * replacing outright is safer and avoids modifying the default code paths, and allows us to remove essentially all biome specific checks, because we are
     * intending to be dimension-specific.
     */
    @Inject(method = "tickRain", at = @At("HEAD"))
    private void addExtraSnowParticlesAndSounds(Camera camera, CallbackInfo ci)
    {
        if (!primalWinter$isWinterDimension)
        {
            return; // If not a winter dimension, use the original code
        }

        final RandomSource random = RandomSource.create((long) this.ticks * 312987231L);
        final ClientLevel level = minecraft.level;
        final BlockPos origin = BlockPos.containing(camera.getPosition());
        final int particleAmount = (int) 100.0F / (minecraft.options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

        BlockPos pos = null;

        assert level != null;

        for (int iparticle = 0; iparticle < particleAmount; iparticle++)
        {
            final int particleX = random.nextInt(21) - 10;
            final int particleZ = random.nextInt(21) - 10;
            final BlockPos particlePos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, origin.offset(particleX, 0, particleZ));

            if (particlePos.getY() > level.getMinBuildHeight() && particlePos.getY() <= origin.getY() + 10 && particlePos.getY() >= origin.getY() - 10)
            {
                pos = particlePos.below();
                if (minecraft.options.particles().get() == ParticleStatus.MINIMAL)
                {
                    break;
                }

                final BlockState state = level.getBlockState(pos);
                final FluidState fluid = level.getFluidState(pos);

                final double particleY = Math.max(
                    state.getCollisionShape(level, pos).max(Direction.Axis.Y, random.nextDouble(), random.nextDouble()),
                    fluid.getHeight(level, pos)
                );
                final ParticleOptions options = !fluid.is(FluidTags.LAVA)
                    && !state.is(Blocks.MAGMA_BLOCK)
                    && !CampfireBlock.isLitCampfire(state)
                    ? PrimalWinterAmbience.SNOW.get() // Use snow particles over rain
                    : ParticleTypes.SMOKE;

                level.addParticle(options, pos.getX() + random.nextDouble(), pos.getY() + particleY, pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
            }
        }

        if (pos != null && random.nextInt(3) < this.rainSoundTime++ && XPlatform.INSTANCE.config().snowSounds.getAsBoolean())
        {
            rainSoundTime = 0;
            if (pos.getY() > origin.getY() + 1 && level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, origin).getY() > Mth.floor(origin.getY()))
            {
                // Modified: use a lower volume for these sounds, since they are too indicative of rain, not snow
                level.playLocalSound(pos, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.05f, 0.2f, false);
            }
            else
            {
                level.playLocalSound(pos, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.1f, 0.5f, false);
            }
        }

        // Added: wind sounds as well
        if (primalWinter$windSoundTime-- < 0 && pos != null && XPlatform.INSTANCE.config().windSounds.getAsBoolean())
        {
            final int light = level.getBrightness(LightLayer.SKY, pos);
            if (light > 3)
            {
                // In a windy location, play wind sounds
                float volumeModifier = 0.2f + (light - 3) * 0.01f;
                float pitchModifier = 0.7f;
                if (camera.getFluidInCamera() != FogType.NONE)
                {
                    pitchModifier = 0.3f;
                }
                primalWinter$windSoundTime = 20 * 3 + random.nextInt(30);
                this.level.playLocalSound(pos, PrimalWinterAmbience.WIND.get(), SoundSource.WEATHER, volumeModifier, pitchModifier, false);
            }
            else
            {
                primalWinter$windSoundTime += 5; // check a short time later
            }
        }
    }
}
