package com.alcatrazescapee.primalwinter.data;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.alcatrazescapee.primalwinter.util.Helpers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks.*;

public final class BuiltinTags
{
    public static class Block extends BlockTagsProvider
    {
        public static final TagKey<net.minecraft.world.level.block.Block> REPLACEABLE_WITH_SNOWY_STUFF = TagKey.create(Registries.BLOCK, Helpers.identifier("replaceable_with_snowy_stuff"));

        public Block(GatherDataEvent event)
        {
            super(event.getGenerator().getPackOutput(), event.getLookupProvider(), PrimalWinter.MOD_ID, event.getExistingFileHelper());
        }

        @Override
        protected void addTags(HolderLookup.Provider provider)
        {
            tag(REPLACEABLE_WITH_SNOWY_STUFF).add(
                Blocks.DIRT,
                Blocks.GRASS_BLOCK,
                Blocks.PODZOL,
                Blocks.COARSE_DIRT,
                Blocks.MYCELIUM,
                Blocks.SNOW_BLOCK,
                Blocks.ICE,
                Blocks.SAND,
                Blocks.RED_SAND,
                Blocks.GRAVEL,
                SNOWY_DIRT.get(),
                SNOWY_COARSE_DIRT.get(),
                SNOWY_SAND.get(),
                SNOWY_RED_SAND.get(),
                SNOWY_GRAVEL.get());

            tag(BlockTags.DIRT).add(
                SNOWY_DIRT.get(),
                SNOWY_COARSE_DIRT.get(),
                SNOWY_MUD.get(),
                SNOWY_MUDDY_MANGROVE_ROOTS.get());
            tag(BlockTags.SAND).add(SNOWY_SAND.get(), SNOWY_RED_SAND.get());
            tag(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH).add(
                SNOWY_MUD.get(),
                SNOWY_MANGROVE_ROOTS.get(),
                SNOWY_MUDDY_MANGROVE_ROOTS.get(),
                SNOWY_VINE.get());
            tag(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH).add(
                SNOWY_MUD.get(),
                SNOWY_MANGROVE_ROOTS.get(),
                SNOWY_MUDDY_MANGROVE_ROOTS.get(),
                SNOWY_VINE.get());
            tag(BlockTags.BASE_STONE_OVERWORLD).add(
                SNOWY_STONE.get(),
                SNOWY_GRANITE.get(),
                SNOWY_ANDESITE.get(),
                SNOWY_DIORITE.get());
            tag(BlockTags.TERRACOTTA).add(
                SNOWY_WHITE_TERRACOTTA.get(),
                SNOWY_ORANGE_TERRACOTTA.get(),
                SNOWY_TERRACOTTA.get(),
                SNOWY_YELLOW_TERRACOTTA.get(),
                SNOWY_BROWN_TERRACOTTA.get(),
                SNOWY_RED_TERRACOTTA.get(),
                SNOWY_LIGHT_GRAY_TERRACOTTA.get());
            tag(BlockTags.BADLANDS_TERRACOTTA).add(
                SNOWY_WHITE_TERRACOTTA.get(),
                SNOWY_ORANGE_TERRACOTTA.get(),
                SNOWY_TERRACOTTA.get(),
                SNOWY_YELLOW_TERRACOTTA.get(),
                SNOWY_BROWN_TERRACOTTA.get(),
                SNOWY_RED_TERRACOTTA.get(),
                SNOWY_LIGHT_GRAY_TERRACOTTA.get());
            tag(BlockTags.OAK_LOGS).add(SNOWY_OAK_LOG.get());
            tag(BlockTags.BIRCH_LOGS).add(SNOWY_BIRCH_LOG.get());
            tag(BlockTags.SPRUCE_LOGS).add(SNOWY_SPRUCE_LOG.get());
            tag(BlockTags.JUNGLE_LOGS).add(SNOWY_JUNGLE_LOG.get());
            tag(BlockTags.DARK_OAK_LOGS).add(SNOWY_DARK_OAK_LOG.get());
            tag(BlockTags.ACACIA_LOGS).add(SNOWY_ACACIA_LOG.get());
            tag(BlockTags.CHERRY_LOGS).add(SNOWY_CHERRY_LOG.get());
            tag(BlockTags.MANGROVE_LOGS).add(SNOWY_MANGROVE_LOG.get());
            tag(BlockTags.OVERWORLD_NATURAL_LOGS).add(
                SNOWY_OAK_LOG.get(),
                SNOWY_BIRCH_LOG.get(),
                SNOWY_SPRUCE_LOG.get(),
                SNOWY_JUNGLE_LOG.get(),
                SNOWY_DARK_OAK_LOG.get(),
                SNOWY_ACACIA_LOG.get(),
                SNOWY_CHERRY_LOG.get(),
                SNOWY_MANGROVE_LOG.get());
            tag(BlockTags.LEAVES).add(
                SNOWY_OAK_LEAVES.get(),
                SNOWY_BIRCH_LEAVES.get(),
                SNOWY_SPRUCE_LEAVES.get(),
                SNOWY_JUNGLE_LEAVES.get(),
                SNOWY_DARK_OAK_LEAVES.get(),
                SNOWY_ACACIA_LEAVES.get(),
                SNOWY_CHERRY_LEAVES.get(),
                SNOWY_MANGROVE_LEAVES.get());
            tag(BlockTags.CLIMBABLE).add(SNOWY_VINE.get());
            tag(BlockTags.REPLACEABLE_BY_TREES).add(SNOWY_VINE.get());

            tag(BlockTags.MINEABLE_WITH_AXE).add(
                SNOWY_MANGROVE_ROOTS.get(),
                SNOWY_VINE.get());
            tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                SNOWY_DIRT.get(),
                SNOWY_COARSE_DIRT.get(),
                SNOWY_SAND.get(),
                SNOWY_RED_SAND.get(),
                SNOWY_GRAVEL.get(),
                SNOWY_MUD.get(),
                SNOWY_DIRT_PATH.get(),
                SNOWY_MUDDY_MANGROVE_ROOTS.get());
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                SNOWY_STONE.get(),
                SNOWY_GRANITE.get(),
                SNOWY_ANDESITE.get(),
                SNOWY_DIORITE.get(),
                SNOWY_WHITE_TERRACOTTA.get(),
                SNOWY_ORANGE_TERRACOTTA.get(),
                SNOWY_TERRACOTTA.get(),
                SNOWY_YELLOW_TERRACOTTA.get(),
                SNOWY_BROWN_TERRACOTTA.get(),
                SNOWY_RED_TERRACOTTA.get(),
                SNOWY_LIGHT_GRAY_TERRACOTTA.get());
            tag(BlockTags.MINEABLE_WITH_HOE).add(
                SNOWY_OAK_LEAVES.get(),
                SNOWY_BIRCH_LEAVES.get(),
                SNOWY_SPRUCE_LEAVES.get(),
                SNOWY_JUNGLE_LEAVES.get(),
                SNOWY_DARK_OAK_LEAVES.get(),
                SNOWY_ACACIA_LEAVES.get(),
                SNOWY_CHERRY_LEAVES.get(),
                SNOWY_MANGROVE_LEAVES.get());
            tag(BlockTags.SWORD_EFFICIENT).add(SNOWY_VINE.get());
        }
    }
}
