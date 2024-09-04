package com.alcatrazescapee.primalwinter.data;

import java.util.Collections;
import com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks;
import com.alcatrazescapee.primalwinter.platform.ForgeRegistryInterface;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import static com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks.*;

public final class BuiltinBlockLoot extends BlockLootSubProvider
{
    public BuiltinBlockLoot(HolderLookup.Provider lookup)
    {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), lookup);
    }

    @Override
    protected void generate()
    {
        dropOther(SNOWY_DIRT.get(), Blocks.DIRT);
        dropOther(SNOWY_COARSE_DIRT.get(), Blocks.COARSE_DIRT);
        dropOther(SNOWY_SAND.get(), Blocks.SAND);
        dropOther(SNOWY_RED_SAND.get(), Blocks.RED_SAND);
        dropOther(SNOWY_GRAVEL.get(), Blocks.GRAVEL);
        dropOther(SNOWY_MUD.get(), Blocks.MUD);
        dropOther(SNOWY_STONE.get(), Blocks.STONE);
        dropOther(SNOWY_GRANITE.get(), Blocks.GRANITE);
        dropOther(SNOWY_ANDESITE.get(), Blocks.ANDESITE);
        dropOther(SNOWY_DIORITE.get(), Blocks.DIORITE);
        dropOther(SNOWY_WHITE_TERRACOTTA.get(), Blocks.WHITE_TERRACOTTA);
        dropOther(SNOWY_ORANGE_TERRACOTTA.get(), Blocks.ORANGE_TERRACOTTA);
        dropOther(SNOWY_TERRACOTTA.get(), Blocks.TERRACOTTA);
        dropOther(SNOWY_YELLOW_TERRACOTTA.get(), Blocks.YELLOW_TERRACOTTA);
        dropOther(SNOWY_BROWN_TERRACOTTA.get(), Blocks.BROWN_TERRACOTTA);
        dropOther(SNOWY_RED_TERRACOTTA.get(), Blocks.RED_TERRACOTTA);
        dropOther(SNOWY_LIGHT_GRAY_TERRACOTTA.get(), Blocks.LIGHT_GRAY_TERRACOTTA);
        dropOther(SNOWY_DIRT_PATH.get(), Blocks.DIRT);
        dropOther(SNOWY_OAK_LOG.get(), Blocks.OAK_LOG);
        dropOther(SNOWY_BIRCH_LOG.get(), Blocks.BIRCH_LOG);
        dropOther(SNOWY_SPRUCE_LOG.get(), Blocks.SPRUCE_LOG);
        dropOther(SNOWY_JUNGLE_LOG.get(), Blocks.JUNGLE_LOG);
        dropOther(SNOWY_DARK_OAK_LOG.get(), Blocks.DARK_OAK_LOG);
        dropOther(SNOWY_ACACIA_LOG.get(), Blocks.ACACIA_LOG);
        dropOther(SNOWY_CHERRY_LOG.get(), Blocks.CHERRY_LOG);
        dropOther(SNOWY_MANGROVE_LOG.get(), Blocks.MANGROVE_LOG);
        add(SNOWY_OAK_LEAVES.get(), b -> createOakLeavesDrops(b, Blocks.OAK_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
        add(SNOWY_BIRCH_LEAVES.get(), b -> createLeavesDrops(b, Blocks.BIRCH_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
        add(SNOWY_SPRUCE_LEAVES.get(), b -> createLeavesDrops(b, Blocks.SPRUCE_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
        add(SNOWY_JUNGLE_LEAVES.get(), b -> createLeavesDrops(b, Blocks.JUNGLE_SAPLING, 0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F));
        add(SNOWY_DARK_OAK_LEAVES.get(), b -> createOakLeavesDrops(b, Blocks.DARK_OAK_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
        add(SNOWY_ACACIA_LEAVES.get(), b -> createLeavesDrops(b, Blocks.ACACIA_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
        add(SNOWY_CHERRY_LEAVES.get(), b -> createLeavesDrops(b, Blocks.CHERRY_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
        add(SNOWY_MANGROVE_LEAVES.get(), this::createMangroveLeavesDrops);
        dropOther(SNOWY_MANGROVE_ROOTS.get(), Blocks.MANGROVE_ROOTS);
        dropOther(SNOWY_MUDDY_MANGROVE_ROOTS.get(), Blocks.MUDDY_MANGROVE_ROOTS);
        add(SNOWY_VINE.get(), BlockLootSubProvider::createShearsOnlyDrop);
        dropOther(SNOWY_SUGAR_CANE.get(), Blocks.SUGAR_CANE);
        dropOther(SNOWY_CACTUS.get(), Blocks.CACTUS);
        add(SNOWY_BAMBOO.get(), createSingleItemTable(Items.BAMBOO, UniformGenerator.between(0f, 0.8f)));
        add(SNOWY_BROWN_MUSHROOM_BLOCK.get(), b -> createMushroomBlockDrop(b, Items.BROWN_MUSHROOM));
        add(SNOWY_RED_MUSHROOM_BLOCK.get(), b -> createMushroomBlockDrop(b, Items.RED_MUSHROOM));
        otherWhenSilkTouch(SNOWY_MUSHROOM_STEM.get(), Blocks.MUSHROOM_STEM);
    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        return ((ForgeRegistryInterface<Block>) PrimalWinterBlocks.BLOCKS).deferred.getEntries().stream().map(Holder::value).toList();
    }
}
