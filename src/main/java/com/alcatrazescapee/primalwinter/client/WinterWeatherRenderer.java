/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.client;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.client.world.ClientWorld;
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
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;

import net.minecraftforge.client.IRenderHandler;

import com.alcatrazescapee.primalwinter.Config;
import com.mojang.blaze3d.systems.RenderSystem;

public class WinterWeatherRenderer implements IRenderHandler
{
    private static final ResourceLocation SNOW_TEXTURES = new ResourceLocation("textures/environment/snow.png");

    private final float[] rainSizeX = new float[1024];
    private final float[] rainSizeZ = new float[1024];
    private int rainSoundTime, windSoundTime;
    private int ticks;

    public WinterWeatherRenderer()
    {
        // Copied from WorldRenderer - I have no idea what it does but let's not touch it shall we?
        for(int i = 0; i < 32; ++i)
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
    }

    /**
     * This is essentially a hardcoded rain renderer from {@link net.minecraft.client.renderer.WorldRenderer#renderRainSnow(LightTexture, float, double, double, double)}, except using the snow textures
     */
    @Override
    @SuppressWarnings("deprecation")
    public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc)
    {
        if (mc != null && mc.gameRenderer != null && !mc.isGamePaused())
        {
            // Update the ticks here since we don't tick anywhere else and this is the best reference we can get.

            this.ticks = ticks;
            float f = world.getRainStrength(partialTicks);
            Vec3d vec3d = mc.gameRenderer.getActiveRenderInfo().getProjectedView();
            double xIn = vec3d.getX();
            double yIn = vec3d.getY();
            double zIn = vec3d.getZ();

            if (f > 0)
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
                            float f5 = ((1.0F - f4 * f4) * 0.5F + 0.5F) * f;
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

                            world.addParticle(ParticleTypes.RAIN, (double) underPos.getX() + d3, (double) ((float) underPos.getY() + 0.1F) + d5, (double) underPos.getZ() + d4, 0.0D, 0.0D, 0.0D);
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
                    world.playSound(posX, posY, posZ, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.05f, 0.05f, false);
                }
                else
                {
                    world.playSound(posX, posY, posZ, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.1f, 0.1f, false);
                }
            }
            if (j > 0 && windSoundTime-- < 0 && Config.CLIENT.windSounds.get())
            {
                windSoundTime = 20 * 3 + random.nextInt(30);
                if (posY > (double) (blockpos.getY() + 1) && world.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos).getY() > MathHelper.floor((float) blockpos.getY()))
                {
                    world.playSound(posX, posY, posZ, ModSounds.WIND.get(), SoundCategory.WEATHER, 0.2f, 0.5f, false);
                }
                else
                {
                    world.playSound(posX, posY, posZ, ModSounds.WIND.get(), SoundCategory.WEATHER, 0.3f, 0.7f, false);
                }
            }

        }
    }
}
