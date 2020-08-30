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
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.LightType;
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

    public void renderSky(int ticks, float partialTicks, MatrixStack matrixStackIn, ClientWorld world, @Nullable Minecraft mc)
    {
        this.ticks = ticks;
        if (mc != null && mc.gameRenderer != null && mc.player != null)
        {
            RenderSystem.disableTexture();
            BlockPos pos = mc.gameRenderer.getActiveRenderInfo().getBlockPos();
            Vec3d vec3d = world.getSkyColor(mc.gameRenderer.getActiveRenderInfo().getBlockPos(), partialTicks);
            float posX = (float) vec3d.x;
            float posY = (float) vec3d.y;
            float posZ = (float) vec3d.z;
            FogRenderer.applyFog();
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
            RenderSystem.depthMask(false);
            RenderSystem.enableFog();
            RenderSystem.color3f(posX, posY, posZ);
            this.skyVBO.bindBuffer();
            this.skyVertexFormat.setupBufferState(0L);
            this.skyVBO.draw(matrixStackIn.getLast().getMatrix(), 7);
            VertexBuffer.unbindBuffer();
            this.skyVertexFormat.clearBufferState();
            RenderSystem.disableFog();
            RenderSystem.disableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float[] sunriseSunsetColors = world.dimension.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
            if (sunriseSunsetColors != null && !(world.getRainStrength(partialTicks) > 0 && world.getBiome(pos).getTemperature(pos) < 0.15)) // Skip sunrise and sunset if snowing as the snow fog will be in place
            {
                RenderSystem.disableTexture();
                RenderSystem.shadeModel(7425);
                matrixStackIn.push();
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
                float f3 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F;
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f3));
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
                float f4 = sunriseSunsetColors[0];
                float f5 = sunriseSunsetColors[1];
                float f6 = sunriseSunsetColors[2];
                Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
                bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
                bufferbuilder.pos(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, sunriseSunsetColors[3]).endVertex();

                for (int j = 0; j <= 16; ++j)
                {
                    float f7 = (float) j * ((float) Math.PI * 2F) / 16.0F;
                    float f8 = MathHelper.sin(f7);
                    float f9 = MathHelper.cos(f7);
                    bufferbuilder.pos(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * sunriseSunsetColors[3]).color(sunriseSunsetColors[0], sunriseSunsetColors[1], sunriseSunsetColors[2], 0.0F).endVertex();
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
            mc.getTextureManager().bindTexture(SUN_TEXTURES);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(matrix4f1, -f12, 100.0F, -f12).tex(0.0F, 0.0F).endVertex();
            bufferbuilder.pos(matrix4f1, f12, 100.0F, -f12).tex(1.0F, 0.0F).endVertex();
            bufferbuilder.pos(matrix4f1, f12, 100.0F, f12).tex(1.0F, 1.0F).endVertex();
            bufferbuilder.pos(matrix4f1, -f12, 100.0F, f12).tex(0.0F, 1.0F).endVertex();
            bufferbuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferbuilder);
            f12 = 20.0F;
            mc.getTextureManager().bindTexture(MOON_PHASES_TEXTURES);
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
            double d0 = mc.player.getEyePosition(partialTicks).y - world.getHorizonHeight();
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

            if (world.dimension.isSkyColored())
            {
                RenderSystem.color3f(posX * 0.2F + 0.04F, posY * 0.2F + 0.04F, posZ * 0.6F + 0.1F);
            }
            else
            {
                RenderSystem.color3f(posX, posY, posZ);
            }

            RenderSystem.enableTexture();
            RenderSystem.depthMask(true);
            RenderSystem.disableFog();
        }
    }

    /**
     * This is essentially a hardcoded rain renderer from {@link net.minecraft.client.renderer.WorldRenderer#renderRainSnow(LightTexture, float, double, double, double)}, except using the snow textures
     */
    @SuppressWarnings("deprecation")
    public void renderRainSnow(int ticks, float partialTicks, ClientWorld world, @Nullable Minecraft mc)
    {
        this.ticks = ticks;
        if (mc != null && mc.gameRenderer != null)
        {
            float rainStrength = world.getRainStrength(partialTicks);
            Vec3d vec3d = mc.gameRenderer.getActiveRenderInfo().getProjectedView();
            double xIn = vec3d.getX();
            double yIn = vec3d.getY();
            double zIn = vec3d.getZ();

            if (rainStrength > 0)
            {
                mc.gameRenderer.getLightTexture().enableLightmap();
                int xPos = MathHelper.floor(xIn);
                int yPos = MathHelper.floor(yIn);
                int zPos = MathHelper.floor(zIn);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                RenderSystem.disableCull();
                RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.defaultAlphaFunc();
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

                int weatherAmount = Config.CLIENT.snowDensity.get();
                int flag = -1;
                BlockPos.Mutable mutablePos = new BlockPos.Mutable();

                for (int z = zPos - weatherAmount; z <= zPos + weatherAmount; ++z)
                {
                    for (int x = xPos - weatherAmount; x <= xPos + weatherAmount; ++x)
                    {
                        int rainSizeIndex = (z - zPos + 16) * 32 + (x - xPos + 16);
                        double rainSizeXValue = (double) this.rainSizeX[rainSizeIndex] * 0.5D;
                        double rainSizeZValue = (double) this.rainSizeZ[rainSizeIndex] * 0.5D;
                        mutablePos.setPos(x, 0, z);
                        int topYPos = world.getHeight(Heightmap.Type.MOTION_BLOCKING, mutablePos).getY();
                        int lowestYPos = yPos - weatherAmount;
                        int highestYPos = yPos + weatherAmount;
                        if (lowestYPos < topYPos)
                        {
                            lowestYPos = topYPos;
                        }

                        if (highestYPos < topYPos)
                        {
                            highestYPos = topYPos;
                        }

                        int y = topYPos;
                        if (topYPos < yPos)
                        {
                            y = yPos;
                        }

                        if (lowestYPos != highestYPos)
                        {
                            Random random = new Random(x * x * 3121 + x * 45238971 ^ z * z * 418711 + z * 13761);
                            mutablePos.setPos(x, lowestYPos, z);
                            if (flag != 1)
                            {
                                flag = 1;
                                mc.getTextureManager().bindTexture(SNOW_TEXTURES);
                                bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                            }

                            int i3 = ticks + x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 31;
                            float f3 = -((float) i3 + partialTicks) / 32.0F * (3.0F + random.nextFloat());
                            double d2 = (double) ((float) x + 0.5F) - xIn;
                            double d4 = (double) ((float) z + 0.5F) - zIn;
                            float f4 = MathHelper.sqrt(d2 * d2 + d4 * d4) / (float) weatherAmount;
                            float f5 = ((1.0F - f4 * f4) * 0.5F + 0.5F) * rainStrength;
                            mutablePos.setPos(x, y, z);
                            int light = WorldRenderer.getCombinedLight(world, mutablePos);
                            bufferbuilder.pos((double) x - xIn - rainSizeXValue + 0.5D, (double) highestYPos - yIn, (double) z - zIn - rainSizeZValue + 0.5D).tex(0.0F, (float) lowestYPos * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(light).endVertex();
                            bufferbuilder.pos((double) x - xIn + rainSizeXValue + 0.5D, (double) highestYPos - yIn, (double) z - zIn + rainSizeZValue + 0.5D).tex(1.0F, (float) lowestYPos * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(light).endVertex();
                            bufferbuilder.pos((double) x - xIn + rainSizeXValue + 0.5D, (double) lowestYPos - yIn, (double) z - zIn + rainSizeZValue + 0.5D).tex(1.0F, (float) highestYPos * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(light).endVertex();
                            bufferbuilder.pos((double) x - xIn - rainSizeXValue + 0.5D, (double) lowestYPos - yIn, (double) z - zIn - rainSizeZValue + 0.5D).tex(0.0F, (float) highestYPos * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(light).endVertex();
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
                mc.gameRenderer.getLightTexture().disableLightmap();
            }
        }
    }

    /**
     * Similar to {@link WorldRenderer#addRainParticles(ActiveRenderInfo)}
     */
    @SuppressWarnings("ConstantConditions")
    public void addSnowParticlesAndSound(Minecraft mc, World world, ActiveRenderInfo activeRenderInfo)
    {
        float rainStrength = world.getRainStrength(1);
        if (!mc.gameSettings.fancyGraphics)
        {
            rainStrength /= 2;
        }
        if (rainStrength != 0)
        {
            Random random = new Random((long) ticks * 312987231L);
            BlockPos blockpos = new BlockPos(activeRenderInfo.getProjectedView());
            double posX = 0.0D;
            double posY = 0.0D;
            double posZ = 0.0D;
            int j = 0;
            int k = (int) (100.0F * rainStrength * rainStrength);
            if (mc.gameSettings.particles == ParticleStatus.DECREASED)
            {
                k >>= 1;
            }
            else if (mc.gameSettings.particles == ParticleStatus.MINIMAL)
            {
                k = 0;
            }

            for (int l = 0; l < k; ++l)
            {
                BlockPos topPos = world.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos.add(random.nextInt(10) - random.nextInt(10), 0, random.nextInt(10) - random.nextInt(10)));
                Biome biome = world.getBiome(topPos);
                BlockPos underPos = topPos.down();
                if (topPos.getY() <= blockpos.getY() + 10 && topPos.getY() >= blockpos.getY() - 10 && biome.getPrecipitation() == Biome.RainType.SNOW && biome.getTemperature(topPos) < 0.15F)
                {
                    double d3 = random.nextDouble();
                    double d4 = random.nextDouble();
                    BlockState blockstate = world.getBlockState(underPos);
                    IFluidState ifluidstate = world.getFluidState(topPos);
                    VoxelShape voxelshape = blockstate.getCollisionShape(world, underPos);
                    double d7 = voxelshape.max(Direction.Axis.Y, d3, d4);
                    double d8 = ifluidstate.getActualHeight(world, topPos);
                    double d5;
                    double d6;
                    if (d7 >= d8)
                    {
                        d5 = d7;
                        d6 = voxelshape.min(Direction.Axis.Y, d3, d4);
                    }
                    else
                    {
                        d5 = 0.0D;
                        d6 = 0.0D;
                    }

                    if (d5 > -Double.MAX_VALUE)
                    {
                        if (!ifluidstate.isTagged(FluidTags.LAVA) && blockstate.getBlock() != Blocks.MAGMA_BLOCK && (blockstate.getBlock() != Blocks.CAMPFIRE || !blockstate.get(CampfireBlock.LIT)))
                        {
                            ++j;
                            if (random.nextInt(j) == 0)
                            {
                                posX = (double) underPos.getX() + d3;
                                posY = (double) ((float) underPos.getY() + 0.1F) + d5 - 1.0D;
                                posZ = (double) underPos.getZ() + d4;
                            }

                            world.addParticle(ModParticleTypes.SNOW.get(), (double) underPos.getX() + d3, (double) ((float) underPos.getY() + 0.1F) + d5, (double) underPos.getZ() + d4, 0.0D, 0.0D, 0.0D);
                        }
                        else
                        {
                            world.addParticle(ParticleTypes.SMOKE, (double) topPos.getX() + d3, (double) ((float) topPos.getY() + 0.1F) - d6, (double) topPos.getZ() + d4, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            }

            if (j > 0 && random.nextInt(3) < rainSoundTime++ && Config.CLIENT.snowSounds.get())
            {
                rainSoundTime = 0;
                if (posY > (double) (blockpos.getY() + 1) && world.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos).getY() > MathHelper.floor((float) blockpos.getY()))
                {
                    world.playSound(posX, posY, posZ, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.03f, 0.05f, false);
                }
                else
                {
                    world.playSound(posX, posY, posZ, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.06f, 0.1f, false);
                }
            }

            // Added
            if (windSoundTime-- < 0 && Config.CLIENT.windSounds.get())
            {
                BlockPos playerPos = activeRenderInfo.getBlockPos();
                Entity entity = activeRenderInfo.getRenderViewEntity();
                int light = activeRenderInfo.getRenderViewEntity().world.getLightFor(LightType.SKY, playerPos);
                if (light > 3 && entity.world.isRaining() && entity.world.getBiome(playerPos).getTemperature(playerPos) < 0.15f)
                {
                    // In a windy location, play wind sounds
                    float volumeModifier = 0.2f + (light - 3) * 0.01f;
                    float pitchModifier = 0.7f;
                    if (activeRenderInfo.getFluidState().getFluid() != Fluids.EMPTY)
                    {
                        pitchModifier = 0.3f;
                    }
                    windSoundTime = 20 * 3 + random.nextInt(30);
                    mc.world.playSound(playerPos, ModSoundEvents.WIND.get(), SoundCategory.WEATHER, volumeModifier, pitchModifier, true);
                }
                else
                {
                    windSoundTime += 5; // check a short time later
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
