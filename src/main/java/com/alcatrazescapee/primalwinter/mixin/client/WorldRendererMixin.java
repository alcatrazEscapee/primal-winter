/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.client;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.alcatrazescapee.primalwinter.Config;
import com.alcatrazescapee.primalwinter.client.ModParticleTypes;
import com.alcatrazescapee.primalwinter.client.ModSoundEvents;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin
{
    // Don't bother shadowing these as they're constants
    private static final ResourceLocation RAIN_TEXTURES = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation SNOW_TEXTURES = new ResourceLocation("textures/environment/snow.png");

    //@formatter:off
    @Shadow @Final private Minecraft mc;
    @Shadow @Final private float[] field_20794;
    @Shadow @Final private float[] field_20795;
    @Shadow private ClientWorld world;
    @Shadow private int ticks;
    //@formatter:on

    private int windSoundTime = 0, rainSoundTime = 0;

    /**
     * This is a modified version of {@link WorldRenderer#renderWeather(LightmapTextureManager, float, double, double, double)}
     * Changes:
     * - If this is the overworld, then the original method is cancelled and this is ran instead
     * - The rain / snow branch is condensed into one branch using the rain logic, except with rain / snow textures
     */
    @SuppressWarnings({"deprecation"})
    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    public void inject_renderWeather(LightTexture manager, float partialTicks, double xIn, double yIn, double zIn, CallbackInfo ci)
    {
        if (Config.CLIENT.weatherRenderChanges.get())
        {
            if (mc != null && mc.gameRenderer != null)
            {
                float rainStrength = world.getRainStrength(partialTicks);
                if (rainStrength > 0)
                {
                    mc.gameRenderer.getLightmapTextureManager().enableLightmap();
                    Vector3d vec3d = mc.gameRenderer.getActiveRenderInfo().getProjectedView();
                    int xPos = MathHelper.floor(vec3d.getX());
                    int yPos = MathHelper.floor(vec3d.getY());
                    int zPos = MathHelper.floor(vec3d.getZ());
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferbuilder = tessellator.getBuffer();
                    RenderSystem.enableAlphaTest();
                    RenderSystem.disableCull();
                    RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.defaultAlphaFunc();
                    RenderSystem.enableDepthTest();
                    int weatherAmount = Config.CLIENT.snowDensity.get();
                    RenderSystem.depthMask(Minecraft.isFabulousGraphicsOrBetter());
                    int flag = -1;
                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                    BlockPos.Mutable mutablePos = new BlockPos.Mutable();

                    for (int z = zPos - weatherAmount; z <= zPos + weatherAmount; ++z)
                    {
                        for (int x = xPos - weatherAmount; x <= xPos + weatherAmount; ++x)
                        {
                            int l1 = (z - zPos + 16) * 32 + x - xPos + 16;
                            double d0 = (double) this.field_20794[l1] * 0.5D;
                            double d1 = (double) this.field_20795[l1] * 0.5D;
                            mutablePos.setPos(x, 0, z);
                            Biome biome = world.getBiome(mutablePos);
                            if (biome.getPrecipitation() != Biome.RainType.NONE)
                            {
                                int i2 = world.getHeight(Heightmap.Type.MOTION_BLOCKING, mutablePos).getY();
                                int y = yPos - weatherAmount;
                                int k2 = yPos + weatherAmount;
                                if (y < i2)
                                {
                                    y = i2;
                                }

                                if (k2 < i2)
                                {
                                    k2 = i2;
                                }

                                int l2 = i2;
                                if (i2 < yPos)
                                {
                                    l2 = yPos;
                                }

                                if (y != k2)
                                {
                                    Random random = new Random(x * x * 3121 + x * 45238971 ^ z * z * 418711 + z * 13761);
                                    mutablePos.setPos(x, y, z);
                                    float temperature = biome.getTemperatureCached(mutablePos);
                                    if (flag != 0)
                                    {
                                        flag = 0;
                                        if (temperature > 0.15)
                                        {
                                            mc.getTextureManager().bindTexture(RAIN_TEXTURES);
                                        }
                                        else
                                        {
                                            mc.getTextureManager().bindTexture(SNOW_TEXTURES);
                                        }
                                        bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                    }

                                    int i3 = this.ticks + x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 31;
                                    float f3 = -((float) i3 + partialTicks) / 32.0F * (3.0F + random.nextFloat());
                                    double d2 = (double) ((float) x + 0.5F) - xIn;
                                    double d4 = (double) ((float) z + 0.5F) - zIn;
                                    float f4 = MathHelper.sqrt(d2 * d2 + d4 * d4) / (float) weatherAmount;
                                    float f5 = ((1.0F - f4 * f4) * 0.5F + 0.5F) * rainStrength;
                                    mutablePos.setPos(x, l2, z);
                                    int j3 = WorldRenderer.getLightmapCoordinates(world, mutablePos);
                                    bufferbuilder.vertex((double) x - xIn - d0 + 0.5D, (double) k2 - yIn, (double) z - zIn - d1 + 0.5D).texture(0.0F, (float) y * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).light(j3).endVertex();
                                    bufferbuilder.vertex((double) x - xIn + d0 + 0.5D, (double) k2 - yIn, (double) z - zIn + d1 + 0.5D).texture(1.0F, (float) y * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).light(j3).endVertex();
                                    bufferbuilder.vertex((double) x - xIn + d0 + 0.5D, (double) y - yIn, (double) z - zIn + d1 + 0.5D).texture(1.0F, (float) k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).light(j3).endVertex();
                                    bufferbuilder.vertex((double) x - xIn - d0 + 0.5D, (double) y - yIn, (double) z - zIn - d1 + 0.5D).texture(0.0F, (float) k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).light(j3).endVertex();
                                }
                            }
                        }
                    }

                    if (flag >= 0)
                    {
                        tessellator.draw();
                    }

                    RenderSystem.enableCull();
                    RenderSystem.disableBlend();
                    RenderSystem.defaultAlphaFunc();
                    RenderSystem.disableAlphaTest();
                    mc.gameRenderer.getLightmapTextureManager().disableLightmap();
                }
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
    @Inject(method = "tickRainSplashing", at = @At("RETURN"))
    public void inject_tickRainSplashing(ActiveRenderInfo renderInfo, CallbackInfo ci)
    {
        float f = world.getRainStrength(1.0F) / (Minecraft.isFancyGraphicsEnabled() ? 1.0F : 2.0F);
        if (f > 0.0F)
        {
            Random random = new Random((long) this.ticks * 312987231L);
            IWorldReader worldView = world;
            BlockPos blockPos = new BlockPos(renderInfo.getBlockPos());
            BlockPos blockPos2 = null;
            int i = (int) (100.0F * f * f) / (mc.gameSettings.particles == ParticleStatus.DECREASED ? 2 : 1);

            for (int j = 0; j < i; ++j)
            {
                int k = random.nextInt(21) - 10;
                int l = random.nextInt(21) - 10;
                BlockPos blockPos3 = worldView.getHeight(Heightmap.Type.MOTION_BLOCKING, blockPos.add(k, 0, l)).down();
                Biome biome = worldView.getBiome(blockPos3);
                if (blockPos3.getY() > 0 && blockPos3.getY() <= blockPos.getY() + 10 && blockPos3.getY() >= blockPos.getY() - 10 && biome.getPrecipitation() == Biome.RainType.SNOW && biome.getTemperatureCached(blockPos3) < 0.15F)
                {
                    blockPos2 = blockPos3;
                    if (mc.gameSettings.particles == ParticleStatus.MINIMAL)
                    {
                        break;
                    }

                    double d = random.nextDouble();
                    double e = random.nextDouble();
                    BlockState blockState = worldView.getBlockState(blockPos3);
                    FluidState fluidState = worldView.getFluidState(blockPos3);
                    VoxelShape voxelShape = blockState.getCollisionShape(worldView, blockPos3);
                    double g = voxelShape.max(Direction.Axis.Y, d, e);
                    double h = fluidState.getActualHeight(worldView, blockPos3);
                    double m = Math.max(g, h);
                    IParticleData particleEffect = !fluidState.isTagged(FluidTags.LAVA) && !blockState.isIn(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(blockState) ? ModParticleTypes.SNOW.get() : ParticleTypes.SMOKE;
                    world.addParticle(particleEffect, (double) blockPos3.getX() + d, (double) blockPos3.getY() + m, (double) blockPos3.getZ() + e, 0.0D, 0.0D, 0.0D);
                }
            }

            // Rain / snow splashing sounds. Happens on the surface where particles are generated
            if (blockPos2 != null && random.nextInt(3) < this.rainSoundTime++ && Config.CLIENT.snowSounds.get())
            {
                this.rainSoundTime = 0;
                if (blockPos2.getY() > blockPos.getY() + 1 && worldView.getHeight(Heightmap.Type.MOTION_BLOCKING, blockPos).getY() > MathHelper.floor((float) blockPos.getY()))
                {
                    world.playSound(blockPos2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.03f, 0.05f, false);
                }
                else
                {
                    world.playSound(blockPos2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.06f, 0.1f, false);
                }
            }

            // Added
            if (windSoundTime-- < 0 && Config.CLIENT.windSounds.get())
            {
                BlockPos playerPos = renderInfo.getBlockPos();
                Entity entity = renderInfo.getRenderViewEntity();
                int light = renderInfo.getRenderViewEntity().world.getLightLevel(LightType.SKY, playerPos);
                if (light > 3 && entity.world.isRaining() && entity.world.getBiome(playerPos).getTemperatureCached(playerPos) < 0.15f)
                {
                    // In a windy location, play wind sounds
                    float volumeModifier = 0.2f + (light - 3) * 0.01f;
                    float pitchModifier = 0.7f;
                    if (renderInfo.getFluidState().getFluid() != Fluids.EMPTY)
                    {
                        pitchModifier = 0.3f;
                    }
                    windSoundTime = 20 * 3 + random.nextInt(30);
                    world.playSound(playerPos, ModSoundEvents.WIND.get(), SoundCategory.WEATHER, volumeModifier, pitchModifier, true);
                }
                else
                {
                    windSoundTime += 5; // check a short time later
                }
            }
        }
    }

    /**
     * This is the simplest way to ignore sunset and sunrise colors. We do this since it looks very bad during winter sky rendering. This has the effect of removing it everywhere in the overworld, but since there's no additional context here, and the alternative is to override the entire thing, this is fine.
     */
    @Redirect(method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/DimensionRenderInfo;getSkyColor(FF)[F"))
    private float[] redirect_getSkyAngle(DimensionRenderInfo renderInfo, float skyAngle, float tickDelta)
    {
        return renderInfo instanceof DimensionRenderInfo.Overworld ? null : renderInfo.getSkyColor(skyAngle, tickDelta);
    }
}
