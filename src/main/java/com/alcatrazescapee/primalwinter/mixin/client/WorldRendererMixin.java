/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.client;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.ParticlesMode;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;

import com.alcatrazescapee.primalwinter.ModConfig;
import com.alcatrazescapee.primalwinter.client.ModParticleTypes;
import com.alcatrazescapee.primalwinter.client.ModSoundEvents;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin
{
    // Don't bother shadowing these as they're constants
    private static final Identifier RAIN = new Identifier("textures/environment/rain.png");
    private static final Identifier SNOW = new Identifier("textures/environment/snow.png");

    //@formatter:off
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private float[] field_20794;
    @Shadow @Final private float[] field_20795;
    @Shadow private int ticks;
    //@formatter:on

    private int windSoundTime = 0, rainSoundTime = 0;

    /**
     * This is a modified version of {@link WorldRenderer#renderWeather(LightmapTextureManager, float, double, double, double)}
     * Changes:
     * - If this is the overworld, then the original method is cancelled and this is ran instead
     * - The rain / snow branch is condensed into one branch using the rain logic, except with rain / snow textures
     */
    @SuppressWarnings({"deprecation", "ConstantConditions"})
    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    public void inject_renderWeather(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo ci)
    {
        if (ModConfig.INSTANCE.enableWeatherRenderChanges)
        {
            float h = this.client.world.getRainGradient(f);
            if (h > 0.0F)
            {
                manager.enable();
                World world = this.client.world;
                int i = MathHelper.floor(d);
                int j = MathHelper.floor(e);
                int k = MathHelper.floor(g);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                RenderSystem.enableAlphaTest();
                RenderSystem.disableCull();
                RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.defaultAlphaFunc();
                RenderSystem.enableDepthTest();
                int particleDensity = MathHelper.clamp(ModConfig.INSTANCE.snowParticleDensity, 1, 15);
                RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
                int m = -1;
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                BlockPos.Mutable mutable = new BlockPos.Mutable();

                for (int o = k - particleDensity; o <= k + particleDensity; ++o)
                {
                    for (int p = i - particleDensity; p <= i + particleDensity; ++p)
                    {
                        int q = (o - k + 16) * 32 + p - i + 16;
                        double r = (double) this.field_20794[q] * 0.5D;
                        double s = (double) this.field_20795[q] * 0.5D;
                        mutable.set(p, 0, o);
                        Biome biome = world.getBiome(mutable);
                        if (biome.getPrecipitation() != Biome.Precipitation.NONE)
                        {
                            int t = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, mutable).getY();
                            int u = j - particleDensity;
                            int v = j + particleDensity;
                            if (u < t)
                            {
                                u = t;
                            }

                            if (v < t)
                            {
                                v = t;
                            }

                            int w = t;
                            if (t < j)
                            {
                                w = j;
                            }

                            if (u != v)
                            {
                                Random random = new Random(p * p * 3121 + p * 45238971 ^ o * o * 418711 + o * 13761);
                                mutable.set(p, u, o);
                                float temperature = biome.getTemperature(mutable);
                                float z;
                                float ad;
                                if (m != 0)
                                {
                                    m = 0;
                                    if (temperature > 0.15f)
                                    {
                                        this.client.getTextureManager().bindTexture(RAIN);
                                    }
                                    else
                                    {
                                        this.client.getTextureManager().bindTexture(SNOW);
                                    }
                                    bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
                                }

                                int y = this.ticks + p * p * 3121 + p * 45238971 + o * o * 418711 + o * 13761 & 31;
                                z = -((float) y + f) / 32.0F * (3.0F + random.nextFloat());
                                double aa = (double) ((float) p + 0.5F) - d;
                                double ab = (double) ((float) o + 0.5F) - g;
                                float ac = MathHelper.sqrt(aa * aa + ab * ab) / (float) particleDensity;
                                ad = ((1.0F - ac * ac) * 0.5F + 0.5F) * h;
                                mutable.set(p, w, o);
                                int ae = WorldRenderer.getLightmapCoordinates(world, mutable);
                                bufferBuilder.vertex((double) p - d - r + 0.5D, (double) v - e, (double) o - g - s + 0.5D).texture(0.0F, (float) u * 0.25F + z).color(1.0F, 1.0F, 1.0F, ad).light(ae).next();
                                bufferBuilder.vertex((double) p - d + r + 0.5D, (double) v - e, (double) o - g + s + 0.5D).texture(1.0F, (float) u * 0.25F + z).color(1.0F, 1.0F, 1.0F, ad).light(ae).next();
                                bufferBuilder.vertex((double) p - d + r + 0.5D, (double) u - e, (double) o - g + s + 0.5D).texture(1.0F, (float) v * 0.25F + z).color(1.0F, 1.0F, 1.0F, ad).light(ae).next();
                                bufferBuilder.vertex((double) p - d - r + 0.5D, (double) u - e, (double) o - g - s + 0.5D).texture(0.0F, (float) v * 0.25F + z).color(1.0F, 1.0F, 1.0F, ad).light(ae).next();
                            }
                        }
                    }
                }

                if (m >= 0)
                {
                    tessellator.draw();
                }

                RenderSystem.enableCull();
                RenderSystem.disableBlend();
                RenderSystem.defaultAlphaFunc();
                RenderSystem.disableAlphaTest();
                manager.disable();
            }
            ci.cancel();
        }
    }

    /**
     * This is a modified version of {@link WorldRenderer#tickRainSplashing(Camera)}
     * Changes:
     * - Applies sounds when temperature < 0.15 instead of above
     * - Rain particles are exchanged for snow particles
     * - Applies two sounds: the wind sound, using a custom timer, and a lower pitch rain sound
     *
     * Since vanilla does nothing here when temperature < 0.15, we don't cancel the original method
     */
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "tickRainSplashing", at = @At("RETURN"))
    public void inject_tickRainSplashing(Camera camera, CallbackInfo ci)
    {
        float f = this.client.world.getRainGradient(1.0F) / (MinecraftClient.isFancyGraphicsOrBetter() ? 1.0F : 2.0F);
        if (f > 0.0F)
        {
            Random random = new Random((long) this.ticks * 312987231L);
            WorldView worldView = this.client.world;
            BlockPos blockPos = new BlockPos(camera.getPos());
            BlockPos blockPos2 = null;
            int i = (int) (100.0F * f * f) / (this.client.options.particles == ParticlesMode.DECREASED ? 2 : 1);

            for (int j = 0; j < i; ++j)
            {
                int k = random.nextInt(21) - 10;
                int l = random.nextInt(21) - 10;
                BlockPos blockPos3 = worldView.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(k, 0, l)).down();
                Biome biome = worldView.getBiome(blockPos3);
                if (blockPos3.getY() > 0 && blockPos3.getY() <= blockPos.getY() + 10 && blockPos3.getY() >= blockPos.getY() - 10 && biome.getPrecipitation() == Biome.Precipitation.SNOW && biome.getTemperature(blockPos3) < 0.15F)
                {
                    blockPos2 = blockPos3;
                    if (this.client.options.particles == ParticlesMode.MINIMAL)
                    {
                        break;
                    }

                    double d = random.nextDouble();
                    double e = random.nextDouble();
                    BlockState blockState = worldView.getBlockState(blockPos3);
                    FluidState fluidState = worldView.getFluidState(blockPos3);
                    VoxelShape voxelShape = blockState.getCollisionShape(worldView, blockPos3);
                    double g = voxelShape.getEndingCoord(Direction.Axis.Y, d, e);
                    double h = fluidState.getHeight(worldView, blockPos3);
                    double m = Math.max(g, h);
                    ParticleEffect particleEffect = !fluidState.isIn(FluidTags.LAVA) && !blockState.isOf(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(blockState) ? ModParticleTypes.SNOW : ParticleTypes.SMOKE;
                    this.client.world.addParticle(particleEffect, (double) blockPos3.getX() + d, (double) blockPos3.getY() + m, (double) blockPos3.getZ() + e, 0.0D, 0.0D, 0.0D);
                }
            }

            // Rain / snow splashing sounds. Happens on the surface where particles are generated
            if (blockPos2 != null && random.nextInt(3) < this.rainSoundTime++ && ModConfig.INSTANCE.enableSnowSounds)
            {
                this.rainSoundTime = 0;
                if (blockPos2.getY() > blockPos.getY() + 1 && worldView.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos).getY() > MathHelper.floor((float) blockPos.getY()))
                {
                    this.client.world.playSound(blockPos2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.03f, 0.05f, false);
                }
                else
                {
                    this.client.world.playSound(blockPos2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.06f, 0.1f, false);
                }
            }

            // Added
            if (windSoundTime-- < 0 && ModConfig.INSTANCE.enableWindSounds)
            {
                BlockPos playerPos = camera.getBlockPos();
                Entity entity = camera.getFocusedEntity();
                int light = camera.getFocusedEntity().world.getLightLevel(LightType.SKY, playerPos);
                if (light > 3 && entity.world.isRaining() && entity.world.getBiome(playerPos).getTemperature(playerPos) < 0.15f)
                {
                    // In a windy location, play wind sounds
                    float volumeModifier = 0.2f + (light - 3) * 0.01f;
                    float pitchModifier = 0.7f;
                    if (camera.getSubmergedFluidState().getFluid() != Fluids.EMPTY)
                    {
                        pitchModifier = 0.3f;
                    }
                    windSoundTime = 20 * 3 + random.nextInt(30);
                    this.client.world.playSound(playerPos, ModSoundEvents.WIND, SoundCategory.WEATHER, volumeModifier, pitchModifier, true);
                }
                else
                {
                    windSoundTime += 5; // check a short time later
                }
            }
        }
    }

    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/SkyProperties;getSkyColor(FF)[F"))
    private float[] redirect_getSkyAngle(SkyProperties skyProperties, float skyAngle, float tickDelta)
    {
        return skyProperties instanceof SkyProperties.Overworld ? null : skyProperties.getSkyColor(skyAngle, tickDelta);
    }
}
