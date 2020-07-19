/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.client;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
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
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.SkyRenderHandler;
import net.minecraftforge.client.WeatherRenderHandler;

import com.alcatrazescapee.primalwinter.Config;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

public class WinterWorldRenderer
{
    private static final ResourceLocation RAIN_TEXTURES = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation SNOW_TEXTURES = new ResourceLocation("textures/environment/snow.png");
    private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
    private static WinterWorldRenderer INSTANCE;

    public static WinterWorldRenderer get()
    {
        return INSTANCE == null ? INSTANCE = new WinterWorldRenderer() : INSTANCE;
    }

    private final float[] rainSizeX = new float[1024];
    private final float[] rainSizeZ = new float[1024];
    private final VertexFormat skyVertexFormat = DefaultVertexFormats.POSITION;
    private int rainSoundTime, windSoundTime;
    private int ticks;
    private VertexBuffer starVBO;
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;

    public WinterWorldRenderer()
    {
        INSTANCE = this;

        // Copied from WorldRenderer - I have no idea what it does but let's not touch it shall we?
        for (int i = 0; i < 32; ++i)
        {
            for (int j = 0; j < 32; ++j)
            {
                float f = (float) (j - 16);
                float f1 = (float) (i - 16);
                float f2 = MathHelper.sqrt(f * f + f1 * f1);
                this.rainSizeX[i << 5 | j] = -f1 / f2;
                this.rainSizeZ[i << 5 | j] = f / f2;
            }
        }

        rainSoundTime = 0;
        windSoundTime = 0;

        generateStars();
        generateSky();
        generateSky2();
    }

    public WeatherRenderHandler getWeatherHandler()
    {
        return this::renderRainSnow;
    }

    public SkyRenderHandler getSkyHandler()
    {
        return this::renderSky;
    }

    /**
     * This is a modified version of {@link WorldRenderer#renderSky(MatrixStack, float)}
     * Changes:
     * - This only contains the overworld render path
     * - The sunrise and sunset sky colors are not shown when the weather is stormy
     */
    @SuppressWarnings("deprecation")
    public void renderSky(int ticks, float partialTicks, MatrixStack matrixStackIn, ClientWorld world, @Nullable Minecraft mc)
    {
        this.ticks = ticks;
        if (mc != null && mc.gameRenderer != null && mc.player != null)
        {
            RenderSystem.disableTexture();
            Vector3d vector3d = world.getSkyColor(mc.gameRenderer.getActiveRenderInfo().getBlockPos(), partialTicks);
            float f = (float) vector3d.x;
            float f1 = (float) vector3d.y;
            float f2 = (float) vector3d.z;
            FogRenderer.applyFog();
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
            RenderSystem.depthMask(false);
            RenderSystem.enableFog();
            RenderSystem.color3f(f, f1, f2);
            this.skyVBO.bindBuffer();
            this.skyVertexFormat.setupBufferState(0L);
            this.skyVBO.draw(matrixStackIn.getLast().getMatrix(), 7);
            VertexBuffer.unbindBuffer();
            this.skyVertexFormat.clearBufferState();
            RenderSystem.disableFog();
            RenderSystem.disableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float[] skyColors = world.func_239132_a_().func_230492_a_(world.getCelestialAngle(partialTicks), partialTicks);
            BlockPos pos = new BlockPos(vector3d);
            if (skyColors != null && !(world.getRainStrength(partialTicks) > 0 && world.getBiome(pos).getTemperature(pos) < 0.15)) // Skip sunrise and sunset if snowing as the snow fog will be in place)
            {
                RenderSystem.disableTexture();
                RenderSystem.shadeModel(7425);
                matrixStackIn.push();
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
                float f3 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F;
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f3));
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
                float f4 = skyColors[0];
                float f5 = skyColors[1];
                float f6 = skyColors[2];
                Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
                bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
                bufferbuilder.pos(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, skyColors[3]).endVertex();
                int i = 16;

                for (int j = 0; j <= 16; ++j)
                {
                    float f7 = (float) j * ((float) Math.PI * 2F) / 16.0F;
                    float f8 = MathHelper.sin(f7);
                    float f9 = MathHelper.cos(f7);
                    bufferbuilder.pos(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * skyColors[3]).color(skyColors[0], skyColors[1], skyColors[2], 0.0F).endVertex();
                }

                bufferbuilder.finishDrawing();
                WorldVertexBufferUploader.draw(bufferbuilder);
                matrixStackIn.pop();
                RenderSystem.shadeModel(7424);
            }
            RenderSystem.enableTexture();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            matrixStackIn.push();
            float f11 = 1.0F - world.getRainStrength(partialTicks);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, f11);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90.0F));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(world.getCelestialAngle(partialTicks) * 360.0F));
            Matrix4f matrix4f1 = matrixStackIn.getLast().getMatrix();
            float f12 = 30.0F;
            mc.textureManager.bindTexture(SUN_TEXTURES);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(matrix4f1, -f12, 100.0F, -f12).tex(0.0F, 0.0F).endVertex();
            bufferbuilder.pos(matrix4f1, f12, 100.0F, -f12).tex(1.0F, 0.0F).endVertex();
            bufferbuilder.pos(matrix4f1, f12, 100.0F, f12).tex(1.0F, 1.0F).endVertex();
            bufferbuilder.pos(matrix4f1, -f12, 100.0F, f12).tex(0.0F, 1.0F).endVertex();
            bufferbuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferbuilder);
            f12 = 20.0F;
            mc.textureManager.bindTexture(MOON_PHASES_TEXTURES);
            int k = world.getMoonPhase();
            int l = k % 4;
            int i1 = k / 4 % 2;
            float f13 = (float) (l) / 4.0F;
            float f14 = (float) (i1) / 2.0F;
            float f15 = (float) (l + 1) / 4.0F;
            float f16 = (float) (i1 + 1) / 2.0F;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(matrix4f1, -f12, -100.0F, f12).tex(f15, f16).endVertex();
            bufferbuilder.pos(matrix4f1, f12, -100.0F, f12).tex(f13, f16).endVertex();
            bufferbuilder.pos(matrix4f1, f12, -100.0F, -f12).tex(f13, f14).endVertex();
            bufferbuilder.pos(matrix4f1, -f12, -100.0F, -f12).tex(f15, f14).endVertex();
            bufferbuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferbuilder);
            RenderSystem.disableTexture();
            float f10 = world.getStarBrightness(partialTicks) * f11;
            if (f10 > 0.0F)
            {
                RenderSystem.color4f(f10, f10, f10, f10);
                this.starVBO.bindBuffer();
                this.skyVertexFormat.setupBufferState(0L);
                this.starVBO.draw(matrixStackIn.getLast().getMatrix(), 7);
                VertexBuffer.unbindBuffer();
                this.skyVertexFormat.clearBufferState();
            }
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableFog();
            matrixStackIn.pop();
            RenderSystem.disableTexture();
            RenderSystem.color3f(0.0F, 0.0F, 0.0F);
            double d0 = mc.player.getEyePosition(partialTicks).y - world.getWorldInfo().func_239159_f_();
            if (d0 < 0.0D)
            {
                matrixStackIn.push();
                matrixStackIn.translate(0.0D, 12.0D, 0.0D);
                this.sky2VBO.bindBuffer();
                this.skyVertexFormat.setupBufferState(0L);
                this.sky2VBO.draw(matrixStackIn.getLast().getMatrix(), 7);
                VertexBuffer.unbindBuffer();
                this.skyVertexFormat.clearBufferState();
                matrixStackIn.pop();
            }
            if (world.func_239132_a_().func_239216_b_())
            {
                RenderSystem.color3f(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
            }
            else
            {
                RenderSystem.color3f(f, f1, f2);
            }
            RenderSystem.enableTexture();
            RenderSystem.depthMask(true);
            RenderSystem.disableFog();
        }
    }

    /**
     * This is a modified version of {@link WorldRenderer#renderRainSnow(LightTexture, float, double, double, double)}
     * Changes:
     * - The {@code LightTexture lightmapIn} is accessed via {@code mc.gameRenderer.getLightTexture()}
     * - The x, y, z positions in are accessed via {@code Vector3d vec3d = mc.gameRenderer.getActiveRenderInfo().getProjectedView();}
     * - The rain amount, in vanilla controlled by fast / fancy graphics, is set via config option
     * - The rain / snow branch is condensed into one branch using the rain logic, except with rain / snow textures
     */
    @SuppressWarnings("deprecation")
    public void renderRainSnow(int ticks, float partialTicks, ClientWorld world, @Nullable Minecraft mc)
    {
        this.ticks = ticks;
        if (mc != null && mc.gameRenderer != null)
        {
            float rainStrength = world.getRainStrength(partialTicks);
            if (rainStrength > 0)
            {
                mc.gameRenderer.getLightTexture().enableLightmap();
                Vector3d vec3d = mc.gameRenderer.getActiveRenderInfo().getProjectedView();
                double xIn = vec3d.getX();
                double yIn = vec3d.getY();
                double zIn = vec3d.getZ();
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
                RenderSystem.depthMask(Minecraft.func_238218_y_());
                int flag = -1;
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                BlockPos.Mutable mutablePos = new BlockPos.Mutable();

                for (int z = zPos - weatherAmount; z <= zPos + weatherAmount; ++z)
                {
                    for (int x = xPos - weatherAmount; x <= xPos + weatherAmount; ++x)
                    {
                        int l1 = (z - zPos + 16) * 32 + x - xPos + 16;
                        double d0 = (double) this.rainSizeX[l1] * 0.5D;
                        double d1 = (double) this.rainSizeZ[l1] * 0.5D;
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
                                float temperature = biome.getTemperature(mutablePos);
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
                                int j3 = WorldRenderer.getCombinedLight(world, mutablePos);
                                bufferbuilder.pos((double) x - xIn - d0 + 0.5D, (double) k2 - yIn, (double) z - zIn - d1 + 0.5D).tex(0.0F, (float) y * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
                                bufferbuilder.pos((double) x - xIn + d0 + 0.5D, (double) k2 - yIn, (double) z - zIn + d1 + 0.5D).tex(1.0F, (float) y * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
                                bufferbuilder.pos((double) x - xIn + d0 + 0.5D, (double) y - yIn, (double) z - zIn + d1 + 0.5D).tex(1.0F, (float) k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
                                bufferbuilder.pos((double) x - xIn - d0 + 0.5D, (double) y - yIn, (double) z - zIn - d1 + 0.5D).tex(0.0F, (float) k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();

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
                mc.gameRenderer.getLightTexture().disableLightmap();
            }
        }
    }

    /**
     * This is a modified version of {@link WorldRenderer#addRainParticles(ActiveRenderInfo)}
     * Changes:
     * - Applies sounds when temperature < 0.15 instead of above
     * - Rain particles are exchanged for snow particles
     * - Applies two sounds: the wind sound, using a custom timer, and a lower pitch rain sound
     */
    public void addSnowParticlesAndSound(Minecraft mc, World world, ActiveRenderInfo activeRenderInfo)
    {
        float rainStrength = world.getRainStrength(1.0F) / (Minecraft.isFancyGraphicsEnabled() ? 1.0F : 2.0F);
        if (rainStrength > 0)
        {
            Random random = new Random((long) this.ticks * 312987231L);
            BlockPos blockpos = new BlockPos(activeRenderInfo.getProjectedView());
            BlockPos blockpos1 = null;
            int i = (int) (100.0F * rainStrength * rainStrength) / (mc.gameSettings.particles == ParticleStatus.DECREASED ? 2 : 1);
            for (int j = 0; j < i; ++j)
            {
                int k = random.nextInt(21) - 10;
                int l = random.nextInt(21) - 10;
                BlockPos blockpos2 = world.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos.add(k, 0, l)).down();
                Biome biome = world.getBiome(blockpos2);
                if (blockpos2.getY() > 0 && blockpos2.getY() <= blockpos.getY() + 10 && blockpos2.getY() >= blockpos.getY() - 10 && biome.getPrecipitation() == Biome.RainType.RAIN && biome.getTemperature(blockpos2) < 0.15F)
                {
                    blockpos1 = blockpos2;
                    if (mc.gameSettings.particles == ParticleStatus.MINIMAL)
                    {
                        break;
                    }
                    double d0 = random.nextDouble();
                    double d1 = random.nextDouble();
                    BlockState blockstate = world.getBlockState(blockpos2);
                    FluidState fluidstate = world.getFluidState(blockpos2);
                    VoxelShape voxelshape = blockstate.getCollisionShape(world, blockpos2);
                    double d2 = voxelshape.max(Direction.Axis.Y, d0, d1);
                    double d3 = fluidstate.getActualHeight(world, blockpos2);
                    double d4 = Math.max(d2, d3);
                    IParticleData iparticledata = !fluidstate.isTagged(FluidTags.LAVA) && !blockstate.isIn(Blocks.MAGMA_BLOCK) && !CampfireBlock.func_226915_i_(blockstate) ? ModParticleTypes.SNOW.get() : ParticleTypes.SMOKE;
                    world.addParticle(iparticledata, (double) blockpos2.getX() + d0, (double) blockpos2.getY() + d4, (double) blockpos2.getZ() + d1, 0.0D, 0.0D, 0.0D);
                }
            }

            if (blockpos1 != null && windSoundTime-- < 0 && Config.CLIENT.windSounds.get())
            {
                if (blockpos1.getY() > blockpos.getY() + 1 && world.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos).getY() > MathHelper.floor((float) blockpos.getY()))
                {
                    ((ClientWorld) world).playSound(blockpos1, ModSoundEvents.WIND.get(), SoundCategory.WEATHER, 0.2F, 0.5F, false);
                }
                else
                {
                    ((ClientWorld) world).playSound(blockpos1, ModSoundEvents.WIND.get(), SoundCategory.WEATHER, 0.3F, 0.7F, false);
                }
                windSoundTime = 20 * 3 + random.nextInt(30);
            }
            if (blockpos1 != null && random.nextInt(3) < this.rainSoundTime++)
            {
                this.rainSoundTime = 0;
                if (blockpos1.getY() > blockpos.getY() + 1 && world.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos).getY() > MathHelper.floor((float) blockpos.getY()))
                {
                    ((ClientWorld) world).playSound(blockpos1, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.03f, 0.05f, false);
                }
                else
                {
                    ((ClientWorld) world).playSound(blockpos1, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.06f, 0.1f, false);
                }
            }
        }
    }

    /**
     * {@link WorldRenderer#generateStars()}
     */
    private void generateStars()
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        if (this.starVBO != null)
        {
            this.starVBO.close();
        }

        this.starVBO = new VertexBuffer(this.skyVertexFormat);
        this.renderStars(bufferbuilder);
        bufferbuilder.finishDrawing();
        this.starVBO.upload(bufferbuilder);
    }

    /**
     * {@link WorldRenderer#renderStars(BufferBuilder)}
     */
    private void renderStars(BufferBuilder bufferBuilderIn)
    {
        Random random = new Random(10842L);
        bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);

        for (int i = 0; i < 1500; ++i)
        {
            double d0 = random.nextFloat() * 2.0F - 1.0F;
            double d1 = random.nextFloat() * 2.0F - 1.0F;
            double d2 = random.nextFloat() * 2.0F - 1.0F;
            double d3 = 0.15F + random.nextFloat() * 0.1F;
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d4 < 1.0D && d4 > 0.01D)
            {
                d4 = 1.0D / Math.sqrt(d4);
                d0 = d0 * d4;
                d1 = d1 * d4;
                d2 = d2 * d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; ++j)
                {
                    double d18 = (double) ((j & 2) - 1) * d3;
                    double d19 = (double) ((j + 1 & 2) - 1) * d3;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0D * d13;
                    double d24 = 0.0D * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    bufferBuilderIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
                }
            }
        }

    }

    /**
     * {@link WorldRenderer#generateSky()}
     */
    private void generateSky()
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        if (this.skyVBO != null)
        {
            this.skyVBO.close();
        }
        this.skyVBO = new VertexBuffer(this.skyVertexFormat);
        this.renderSky(bufferbuilder, 16.0F, false);
        bufferbuilder.finishDrawing();
        this.skyVBO.upload(bufferbuilder);
    }

    /**
     * {@link WorldRenderer#generateSky2()}
     */
    private void generateSky2()
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        if (this.sky2VBO != null)
        {
            this.sky2VBO.close();
        }
        this.sky2VBO = new VertexBuffer(this.skyVertexFormat);
        this.renderSky(bufferbuilder, -16.0F, true);
        bufferbuilder.finishDrawing();
        this.sky2VBO.upload(bufferbuilder);
    }

    /**
     * {@link WorldRenderer#renderSky(BufferBuilder, float, boolean)}
     */
    private void renderSky(BufferBuilder bufferBuilderIn, float posY, boolean reverseX)
    {
        bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);
        for (int k = -384; k <= 384; k += 64)
        {
            for (int l = -384; l <= 384; l += 64)
            {
                float f = (float) k;
                float f1 = (float) (k + 64);
                if (reverseX)
                {
                    f1 = (float) k;
                    f = (float) (k + 64);
                }

                bufferBuilderIn.pos(f, posY, l).endVertex();
                bufferBuilderIn.pos(f1, posY, l).endVertex();
                bufferBuilderIn.pos(f1, posY, l + 64).endVertex();
                bufferBuilderIn.pos(f, posY, l + 64).endVertex();
            }
        }
    }
}
