package com.alcatrazescapee.primalwinter.data;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks.*;
import static com.alcatrazescapee.primalwinter.util.PrimalWinterBlockTags.*;

public final class BuiltinBlockTags extends BlockTagsProvider
{
    public BuiltinBlockTags(GatherDataEvent event)
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
        tag(REPLACEABLE_WITH_SNOW)
            .addTag(BlockTags.FLOWERS)
            .add(
                Blocks.TALL_GRASS,
                Blocks.SHORT_GRASS,
                Blocks.FERN,
                Blocks.LARGE_FERN,
                Blocks.DEAD_BUSH,
                Blocks.BROWN_MUSHROOM,
                Blocks.RED_MUSHROOM,
                Blocks.SEAGRASS,
                Blocks.TALL_SEAGRASS,
                Blocks.KELP,
                Blocks.KELP_PLANT,
                Blocks.SWEET_BERRY_BUSH);
        tag(SNOWY_CACTUS_SURVIVES_ON).add(
            Blocks.CACTUS,
            Blocks.SAND,
            Blocks.RED_SAND,
            SNOWY_SAND.get(),
            SNOWY_CACTUS.get(),
            SNOWY_RED_SAND.get());
        tag(SNOWY_SUGAR_CANE_SURVIVES_ON)
            .addTag(BlockTags.DIRT)
            .addTag(BlockTags.SAND)
            .add(
                Blocks.SUGAR_CANE,
                SNOWY_SUGAR_CANE.get(),
                SNOWY_SAND.get(),
                SNOWY_RED_SAND.get(),
                SNOWY_DIRT.get(),
                SNOWY_COARSE_DIRT.get(),
                SNOWY_MUD.get(),
                SNOWY_MUDDY_MANGROVE_ROOTS.get());

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
            SNOWY_VINE.get(),
            SNOWY_SUGAR_CANE.get());
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
        tag(BlockTags.SWORD_EFFICIENT).add(
            SNOWY_VINE.get(),
            SNOWY_SUGAR_CANE.get());
    }
}
