/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.alcatrazescapee.primalwinter.Config;
import com.alcatrazescapee.primalwinter.common.ModBlocks;

public final class ClientEventHandler
{
    private static final int NOPE = 0xFFFFFF;

    public static void init()
    {
        final IEventBus forge = MinecraftForge.EVENT_BUS;
        final IEventBus mod = FMLJavaModLoadingContext.get().getModEventBus();

        mod.addListener(ClientEventHandler::onClientSetup);
        mod.addListener(ClientEventHandler::onRegisterBlockColors);
        mod.addListener(ClientEventHandler::onRegisterItemColors);
        mod.addListener(ClientEventHandler::onRegisterParticleFactories);

        forge.addListener(ClientEventHandler::onRenderFogDensity);
        forge.addListener(ClientEventHandler::onRenderFogColors);
    }

    public static void onClientSetup(FMLClientSetupEvent event)
    {
        RenderTypeLookup.setRenderLayer(ModBlocks.SNOWY_VINE.get(), RenderType.cutout());
    }

    public static void onRegisterBlockColors(ColorHandlerEvent.Block event)
    {
        BlockColors colors = event.getBlockColors();

        colors.register((state, world, pos, tintIndex) -> tintIndex == 0 ? FoliageColors.getEvergreenColor() : NOPE, ModBlocks.SNOWY_SPRUCE_LEAVES.get());
        colors.register((state, world, pos, tintIndex) -> tintIndex == 0 ? FoliageColors.getBirchColor() : NOPE, ModBlocks.SNOWY_BIRCH_LEAVES.get());
        colors.register((state, world, pos, tintIndex) -> {
            if (tintIndex == 0)
            {
                return world != null && pos != null ? BiomeColors.getAverageFoliageColor(world, pos) : FoliageColors.getDefaultColor();
            }
            return NOPE;
        }, ModBlocks.SNOWY_OAK_LEAVES.get(), ModBlocks.SNOWY_DARK_OAK_LEAVES.get(), ModBlocks.SNOWY_JUNGLE_LEAVES.get(), ModBlocks.SNOWY_ACACIA_LEAVES.get(), ModBlocks.SNOWY_VINE.get());
    }

    public static void onRegisterItemColors(ColorHandlerEvent.Item event)
    {
        ItemColors colors = event.getItemColors();

        colors.register((stack, tintIndex) -> {
            if (tintIndex == 0)
            {
                BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
                return event.getBlockColors().getColor(state, null, null, tintIndex);
            }
            return NOPE;
        }, ModBlocks.SNOWY_VINE.get(), ModBlocks.SNOWY_OAK_LEAVES.get(), ModBlocks.SNOWY_SPRUCE_LEAVES.get(), ModBlocks.SNOWY_BIRCH_LEAVES.get(), ModBlocks.SNOWY_JUNGLE_LEAVES.get(), ModBlocks.SNOWY_ACACIA_LEAVES.get(), ModBlocks.SNOWY_DARK_OAK_LEAVES.get());
    }

    public static void onRegisterParticleFactories(ParticleFactoryRegisterEvent event)
    {
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.SNOW.get(), SnowParticle.Factory::new);
    }

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