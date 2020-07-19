package com.alcatrazescapee.primalwinter.mixin.client;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.options.ParticlesOption;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import com.alcatrazescapee.primalwinter.client.ModParticleTypes;
import com.alcatrazescapee.primalwinter.client.ModSoundEvents;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin
{
    // Don't bother shadowing these as they're constants
    private static final Identifier MOON_PHASES = new Identifier("textures/environment/moon_phases.png");
    private static final Identifier SUN = new Identifier("textures/environment/sun.png");
    private static final Identifier RAIN = new Identifier("textures/environment/rain.png");
    private static final Identifier SNOW = new Identifier("textures/environment/snow.png");

    //@formatter:off
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private VertexFormat skyVertexFormat;
    @Shadow @Final private TextureManager textureManager;
    @Shadow @Final private float[] field_20794;
    @Shadow @Final private float[] field_20795;
    @Shadow private int ticks;
    @Shadow private ClientWorld world;
    @Shadow private VertexBuffer lightSkyBuffer;
    @Shadow private VertexBuffer starsBuffer;
    @Shadow private VertexBuffer darkSkyBuffer;
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
    public void primalwinter_renderWeather(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo ci)
    {
        if (this.client.world.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY)
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
                int l = 5;
                if (MinecraftClient.isFancyGraphicsOrBetter())
                {
                    l = 10;
                }

                RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
                int m = -1;
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                BlockPos.Mutable mutable = new BlockPos.Mutable();

                for (int o = k - l; o <= k + l; ++o)
                {
                    for (int p = i - l; p <= i + l; ++p)
                    {
                        int q = (o - k + 16) * 32 + p - i + 16;
                        double r = (double) this.field_20794[q] * 0.5D;
                        double s = (double) this.field_20795[q] * 0.5D;
                        mutable.set(p, 0, o);
                        Biome biome = world.getBiome(mutable);
                        if (biome.getPrecipitation() != Biome.Precipitation.NONE)
                        {
                            int t = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, mutable).getY();
                            int u = j - l;
                            int v = j + l;
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
                                float ac = MathHelper.sqrt(aa * aa + ab * ab) / (float) l;
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
     * This is a modified version of {@link WorldRenderer#renderSky(MatrixStack, float)}
     * Changes:
     * - If this is the overworld, and normal sky type, we cancel the original method
     * - The sunrise and sunset sky colors are not shown when the weather is stormy
     */
    @SuppressWarnings({"deprecation", "ConstantConditions"})
    @Inject(method = "renderSky", at = @At("INVOKE_ASSIGN"), cancellable = true)
    public void primalwinter_renderSky(MatrixStack matrices, float tickDelta, CallbackInfo ci)
    {
        if (this.client.world.getSkyProperties().getSkyType() == SkyProperties.SkyType.NORMAL && this.client.world.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY)
        {
            RenderSystem.disableTexture();
            Vec3d vec3d = this.world.method_23777(this.client.gameRenderer.getCamera().getBlockPos(), tickDelta);
            float f = (float) vec3d.x;
            float g = (float) vec3d.y;
            float h = (float) vec3d.z;
            BackgroundRenderer.setFogBlack();
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            RenderSystem.depthMask(false);
            RenderSystem.enableFog();
            RenderSystem.color3f(f, g, h);
            this.lightSkyBuffer.bind();
            this.skyVertexFormat.startDrawing(0L);
            this.lightSkyBuffer.draw(matrices.peek().getModel(), 7);
            VertexBuffer.unbind();
            this.skyVertexFormat.endDrawing();
            RenderSystem.disableFog();
            RenderSystem.disableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float[] fs = this.world.getSkyProperties().getSkyColor(this.world.getSkyAngle(tickDelta), tickDelta);
            float r;
            float s;
            float o;
            float p;
            float q;
            BlockPos pos = new BlockPos(vec3d);
            if (fs != null && !(world.isRaining() && world.getBiome(pos).getTemperature(pos) < 0.15))
            {
                RenderSystem.disableTexture();
                RenderSystem.shadeModel(7425);
                matrices.push();
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
                r = MathHelper.sin(this.world.getSkyAngleRadians(tickDelta)) < 0.0F ? 180.0F : 0.0F;
                matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(r));
                matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
                float j = fs[0];
                s = fs[1];
                float l = fs[2];
                Matrix4f matrix4f = matrices.peek().getModel();
                bufferBuilder.begin(6, VertexFormats.POSITION_COLOR);
                bufferBuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(j, s, l, fs[3]).next();
                for (int n = 0; n <= 16; ++n)
                {
                    o = (float) n * 6.2831855F / 16.0F;
                    p = MathHelper.sin(o);
                    q = MathHelper.cos(o);
                    bufferBuilder.vertex(matrix4f, p * 120.0F, q * 120.0F, -q * 40.0F * fs[3]).color(fs[0], fs[1], fs[2], 0.0F).next();
                }
                bufferBuilder.end();
                BufferRenderer.draw(bufferBuilder);
                matrices.pop();
                RenderSystem.shadeModel(7424);
            }
            RenderSystem.enableTexture();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
            matrices.push();
            r = 1.0F - this.world.getRainGradient(tickDelta);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, r);
            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
            matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(this.world.getSkyAngle(tickDelta) * 360.0F));
            Matrix4f matrix4f2 = matrices.peek().getModel();
            s = 30.0F;
            this.textureManager.bindTexture(SUN);
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f2, -s, 100.0F, -s).texture(0.0F, 0.0F).next();
            bufferBuilder.vertex(matrix4f2, s, 100.0F, -s).texture(1.0F, 0.0F).next();
            bufferBuilder.vertex(matrix4f2, s, 100.0F, s).texture(1.0F, 1.0F).next();
            bufferBuilder.vertex(matrix4f2, -s, 100.0F, s).texture(0.0F, 1.0F).next();
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            s = 20.0F;
            this.textureManager.bindTexture(MOON_PHASES);
            int t = this.world.getMoonPhase();
            int u = t % 4;
            int v = t / 4 % 2;
            float w = (float) (u) / 4.0F;
            o = (float) (v) / 2.0F;
            p = (float) (u + 1) / 4.0F;
            q = (float) (v + 1) / 2.0F;
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f2, -s, -100.0F, s).texture(p, q).next();
            bufferBuilder.vertex(matrix4f2, s, -100.0F, s).texture(w, q).next();
            bufferBuilder.vertex(matrix4f2, s, -100.0F, -s).texture(w, o).next();
            bufferBuilder.vertex(matrix4f2, -s, -100.0F, -s).texture(p, o).next();
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.disableTexture();
            float aa = this.world.method_23787(tickDelta) * r;
            if (aa > 0.0F)
            {
                RenderSystem.color4f(aa, aa, aa, aa);
                this.starsBuffer.bind();
                this.skyVertexFormat.startDrawing(0L);
                this.starsBuffer.draw(matrices.peek().getModel(), 7);
                VertexBuffer.unbind();
                this.skyVertexFormat.endDrawing();
            }
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableFog();
            matrices.pop();
            RenderSystem.disableTexture();
            RenderSystem.color3f(0.0F, 0.0F, 0.0F);
            double d = this.client.player.getCameraPosVec(tickDelta).y - this.world.getLevelProperties().getSkyDarknessHeight();
            if (d < 0.0D)
            {
                matrices.push();
                matrices.translate(0.0D, 12.0D, 0.0D);
                this.darkSkyBuffer.bind();
                this.skyVertexFormat.startDrawing(0L);
                this.darkSkyBuffer.draw(matrices.peek().getModel(), 7);
                VertexBuffer.unbind();
                this.skyVertexFormat.endDrawing();
                matrices.pop();
            }
            if (this.world.getSkyProperties().isAlternateSkyColor())
            {
                RenderSystem.color3f(f * 0.2F + 0.04F, g * 0.2F + 0.04F, h * 0.6F + 0.1F);
            }
            else
            {
                RenderSystem.color3f(f, g, h);
            }
            RenderSystem.enableTexture();
            RenderSystem.depthMask(true);
            RenderSystem.disableFog();

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
    public void primalwinter_tickRainSplashing(Camera camera, CallbackInfo ci)
    {
        float f = this.client.world.getRainGradient(1.0F) / (MinecraftClient.isFancyGraphicsOrBetter() ? 1.0F : 2.0F);
        if (f > 0.0F)
        {
            Random random = new Random((long) this.ticks * 312987231L);
            WorldView worldView = this.client.world;
            BlockPos blockPos = new BlockPos(camera.getPos());
            BlockPos blockPos2 = null;
            int i = (int) (100.0F * f * f) / (this.client.options.particles == ParticlesOption.DECREASED ? 2 : 1);

            for (int j = 0; j < i; ++j)
            {
                int k = random.nextInt(21) - 10;
                int l = random.nextInt(21) - 10;
                BlockPos blockPos3 = worldView.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(k, 0, l)).down();
                Biome biome = worldView.getBiome(blockPos3);
                if (blockPos3.getY() > 0 && blockPos3.getY() <= blockPos.getY() + 10 && blockPos3.getY() >= blockPos.getY() - 10 && biome.getPrecipitation() == Biome.Precipitation.SNOW && biome.getTemperature(blockPos3) < 0.15F)
                {
                    blockPos2 = blockPos3;
                    if (this.client.options.particles == ParticlesOption.MINIMAL)
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

            if (blockPos2 != null && windSoundTime-- < 0)
            {
                windSoundTime = 20 * 3 + random.nextInt(30);
                if (blockPos2.getY() > blockPos.getY() + 1 && worldView.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos).getY() > MathHelper.floor((float) blockPos.getY()))
                {
                    this.client.world.playSound(blockPos2, ModSoundEvents.WIND, SoundCategory.WEATHER, 0.2F, 0.5F, false);
                }
                else
                {
                    this.client.world.playSound(blockPos2, ModSoundEvents.WIND, SoundCategory.WEATHER, 0.3F, 0.7F, false);
                }
            }

            if (blockPos2 != null && random.nextInt(3) < this.rainSoundTime++)
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
        }
    }
}
