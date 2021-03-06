/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.alcatrazescapee.primalwinter.Config;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientForgeEventHandler
{
    @SubscribeEvent
    public static void onRenderFogDensity(EntityViewRenderEvent.FogDensity event)
    {
        if (event.getInfo().getEntity() instanceof PlayerEntity)
        {
            final PlayerEntity player = (PlayerEntity) event.getInfo().getEntity();
            final World world = player.level;
            final Biome biome = world.getBiome(player.blockPosition());
            if (world.getRainLevel(1.0F) > 0.2D && biome.getPrecipitation() == Biome.RainType.SNOW && biome.getTemperature(player.blockPosition()) < 0.15F)
            {
                final int light = world.getBrightness(LightType.SKY, new BlockPos(player.getEyePosition((float) event.getRenderPartialTicks())));
                if (light > 3 && event.getInfo().getFluidInCamera().getType() == Fluids.EMPTY)
                {
                    event.setCanceled(true);
                    event.setDensity((light - 3) * Config.CLIENT.fogDensity.get().floatValue() / 13f);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderFogColors(EntityViewRenderEvent.FogColors event)
    {
        if (event.getInfo().getEntity() instanceof PlayerEntity)
        {
            final PlayerEntity player = (PlayerEntity) event.getInfo().getEntity();
            final World world = player.level;
            final Biome biome = world.getBiome(player.blockPosition());
            if (world.getRainLevel(1.0F) > 0.2D && biome.getPrecipitation() == Biome.RainType.SNOW && biome.getTemperature(player.blockPosition()) < 0.15F)
            {
                final int light = player.level.getBrightness(LightType.SKY, new BlockPos(player.getEyePosition((float) event.getRenderPartialTicks())));
                if (light > 3 && event.getInfo().getFluidInCamera().getType() == Fluids.EMPTY)
                {
                    // Calculate color based on time of day
                    float partialTicks = (float) event.getRenderPartialTicks();
                    float angle = player.level.getSunAngle(partialTicks);
                    float height = MathHelper.cos(angle);
                    float delta = MathHelper.clamp((height + 0.4f) / 0.8f, 0, 1);

                    int colorDay = Config.CLIENT.fogColorDay.get();
                    int colorNight = Config.CLIENT.fogColorNight.get();
                    float red = ((colorDay >> 16) & 0xFF) * delta + ((colorNight >> 16) & 0xFF) * (1 - delta);
                    float green = ((colorDay >> 8) & 0xFF) * delta + ((colorNight >> 8) & 0xFF) * (1 - delta);
                    float blue = (colorDay & 0xFF) * delta + (colorNight & 0xFF) * (1 - delta);

                    event.setRed(red / 255f);
                    event.setGreen(green / 255f);
                    event.setBlue(blue / 255f);
                }
            }
        }
    }
}