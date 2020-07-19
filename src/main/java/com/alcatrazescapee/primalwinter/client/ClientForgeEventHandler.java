/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.LightType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.alcatrazescapee.primalwinter.Config;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientForgeEventHandler
{
    @SubscribeEvent
    public static void onClientWorldLoad(WorldEvent.Load event)
    {
        if (event.getWorld() instanceof ClientWorld && ((ClientWorld) event.getWorld()).func_234922_V_() == DimensionType.field_235999_c_)
        {
            DimensionRenderInfo renderInfo = ((ClientWorld) event.getWorld()).func_239132_a_();
            WinterWorldRenderer winterRenderer = WinterWorldRenderer.get();
            if (Config.CLIENT.weatherRenderChanges.get())
            {
                // todo: needs MinecraftForge#6994
                //renderInfo.setWeatherRenderer(winterRenderer.getWeatherHandler());
            }
            if (Config.CLIENT.skyRenderChanges.get())
            {
                // todo: needs MinecraftForge#6994
                //renderInfo.setSkyRenderer(winterRenderer.getSkyHandler());
            }
        }
    }

    @SubscribeEvent
    public static void onClientWorldTick(TickEvent.WorldTickEvent.ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientWorld world = mc.world;
        if (world != null && mc.gameRenderer != null && !mc.isGamePaused() && world.func_234922_V_() == DimensionType.field_235999_c_) // getDimensionTypeKey() == DimensionType.OVERWORLD
        {
            WinterWorldRenderer.get().addSnowParticlesAndSound(mc, world, mc.gameRenderer.getActiveRenderInfo());
        }
    }

    @SubscribeEvent
    public static void onRenderFogDensity(EntityViewRenderEvent.FogDensity event)
    {
        if (event.getInfo().getRenderViewEntity() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) event.getInfo().getRenderViewEntity();
            int light = player.world.getLightFor(LightType.SKY, player.func_233580_cy_()); // getPosition
            if (light > 3 && event.getInfo().getFluidState().getFluid() == Fluids.EMPTY && player.world.func_234922_V_() == DimensionType.field_235999_c_) // getDimensionKey() == DimensionType.OVERWORLD
            {
                event.setCanceled(true);
                event.setDensity((light - 3) * Config.CLIENT.fogDensity.get().floatValue() / 13f);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderFogColors(EntityViewRenderEvent.FogColors event)
    {
        if (event.getInfo().getRenderViewEntity() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) event.getInfo().getRenderViewEntity();
            int light = player.world.getLightFor(LightType.SKY, player.func_233580_cy_());
            if (light > 3 && event.getInfo().getFluidState().getFluid() == Fluids.EMPTY && player.world.func_234922_V_() == DimensionType.field_235999_c_) // getDimensionKey() == DimensionType.OVERWORLD
            {
                // Calculate color based on time of day
                float partialTicks = (float) event.getRenderPartialTicks();
                float angle = player.world.getCelestialAngle(partialTicks);
                float height = MathHelper.cos(angle * ((float) Math.PI * 2F));
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
