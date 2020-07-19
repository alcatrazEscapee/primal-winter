package com.alcatrazescapee.primalwinter.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.item.BlockItem;

import com.alcatrazescapee.primalwinter.common.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

@SuppressWarnings("unused")
public class PrimalWinterClient implements ClientModInitializer
{
    private static final int NOPE = 0xFFFFFF;

    @Override
    public void onInitializeClient()
    {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> tintIndex == 0 ? FoliageColors.getSpruceColor() : NOPE, ModBlocks.SNOWY_SPRUCE_LEAVES);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> tintIndex == 0 ? FoliageColors.getBirchColor() : NOPE, ModBlocks.SNOWY_BIRCH_LEAVES);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (tintIndex == 0)
            {
                return world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();
            }
            return NOPE;
        }, ModBlocks.SNOWY_OAK_LEAVES, ModBlocks.SNOWY_DARK_OAK_LEAVES, ModBlocks.SNOWY_JUNGLE_LEAVES, ModBlocks.SNOWY_ACACIA_LEAVES, ModBlocks.SNOWY_VINE);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 0)
            {
                Block block = ((BlockItem) stack.getItem()).getBlock();
                BlockState state = block.getDefaultState();
                return ColorProviderRegistry.BLOCK.get(block).getColor(state, null, null, tintIndex);
            }
            return NOPE;
        }, ModBlocks.SNOWY_VINE, ModBlocks.SNOWY_OAK_LEAVES, ModBlocks.SNOWY_SPRUCE_LEAVES, ModBlocks.SNOWY_BIRCH_LEAVES, ModBlocks.SNOWY_JUNGLE_LEAVES, ModBlocks.SNOWY_ACACIA_LEAVES, ModBlocks.SNOWY_DARK_OAK_LEAVES);

        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.SNOW, SnowParticle.Factory::new);
    }
}
