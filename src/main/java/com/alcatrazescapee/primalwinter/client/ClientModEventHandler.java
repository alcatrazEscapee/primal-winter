/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import com.alcatrazescapee.primalwinter.common.ModBlocks;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEventHandler
{
    private static final int NOPE = 0xFFFFFF;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        RenderTypeLookup.setRenderLayer(ModBlocks.SNOWY_VINE.get(), RenderType.cutout());
    }

    @SubscribeEvent
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

    @SubscribeEvent
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

    @SubscribeEvent
    public static void onRegisterParticleFactories(ParticleFactoryRegisterEvent event)
    {
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.SNOW.get(), SnowParticle.Factory::new);
    }
}