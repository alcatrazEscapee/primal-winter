package com.alcatrazescapee.primalwinter.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;

import com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks;
import com.alcatrazescapee.primalwinter.client.PrimalWinterAmbience;
import com.alcatrazescapee.primalwinter.client.SnowParticle;
import com.alcatrazescapee.primalwinter.platform.client.BlockColorCallback;
import com.alcatrazescapee.primalwinter.platform.client.ItemColorCallback;
import com.alcatrazescapee.primalwinter.platform.client.ParticleProviderCallback;
import com.alcatrazescapee.primalwinter.platform.client.XPlatformClient;

public final class ClientEventHandler
{
    public static void setupClient()
    {
        XPlatformClient.INSTANCE.setRenderType(PrimalWinterBlocks.SNOWY_VINE.get(), RenderType.cutout());
    }

    public static void setupBlockColors(BlockColorCallback colors)
    {
        colors.accept((state, world, pos, tintIndex) -> tintIndex == 0 ? FoliageColor.getEvergreenColor() : 0, PrimalWinterBlocks.SNOWY_SPRUCE_LEAVES.get());
        colors.accept((state, world, pos, tintIndex) -> tintIndex == 0 ? FoliageColor.getBirchColor() : 0, PrimalWinterBlocks.SNOWY_BIRCH_LEAVES.get());
        colors.accept((state, world, pos, tintIndex) -> {
            if (tintIndex == 0)
            {
                return world != null && pos != null ? BiomeColors.getAverageFoliageColor(world, pos) : FoliageColor.getDefaultColor();
            }
            return 0;
        }, PrimalWinterBlocks.SNOWY_OAK_LEAVES.get(), PrimalWinterBlocks.SNOWY_DARK_OAK_LEAVES.get(), PrimalWinterBlocks.SNOWY_JUNGLE_LEAVES.get(), PrimalWinterBlocks.SNOWY_ACACIA_LEAVES.get(), PrimalWinterBlocks.SNOWY_VINE.get());
    }

    public static void setupItemColors(ItemColorCallback colors)
    {
        colors.accept((stack, tintIndex) -> {
            if (tintIndex == 0)
            {
                BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
                return Minecraft.getInstance().getBlockColors().getColor(state, null, null, tintIndex);
            }
            return 0;
        }, PrimalWinterBlocks.SNOWY_VINE.get(), PrimalWinterBlocks.SNOWY_OAK_LEAVES.get(), PrimalWinterBlocks.SNOWY_SPRUCE_LEAVES.get(), PrimalWinterBlocks.SNOWY_BIRCH_LEAVES.get(), PrimalWinterBlocks.SNOWY_JUNGLE_LEAVES.get(), PrimalWinterBlocks.SNOWY_ACACIA_LEAVES.get(), PrimalWinterBlocks.SNOWY_DARK_OAK_LEAVES.get());
    }

    public static void setupParticleFactories(ParticleProviderCallback particles)
    {
        particles.accept(PrimalWinterAmbience.SNOW.get(), SnowParticle.Provider::new);
    }
}
