/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.common;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import com.alcatrazescapee.primalwinter.mixin.item.IAxeItem;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

public final class ModBlocks
{
    public static final Block SNOWY_DIRT = register("snowy_dirt", new Block(AbstractBlock.Settings.copy(Blocks.DIRT)));
    public static final Block SNOWY_COARSE_DIRT = register("snowy_coarse_dirt", new Block(Block.Settings.copy(Blocks.COARSE_DIRT)));
    public static final Block SNOWY_SAND = register("snowy_sand", new SandBlock(0xdbd3a0, Block.Settings.copy(Blocks.SAND)));
    public static final Block SNOWY_RED_SAND = register("snowy_red_sand", new SandBlock(0xa95821, Block.Settings.copy(Blocks.RED_SAND)));
    public static final Block SNOWY_GRAVEL = register("snowy_gravel", new GravelBlock(Block.Settings.copy(Blocks.GRAVEL)));
    public static final Block SNOWY_STONE = register("snowy_stone", new Block(Block.Settings.copy(Blocks.STONE)));
    public static final Block SNOWY_GRANITE = register("snowy_granite", new Block(Block.Settings.copy(Blocks.GRANITE)));
    public static final Block SNOWY_ANDESITE = register("snowy_andesite", new Block(Block.Settings.copy(Blocks.ANDESITE)));
    public static final Block SNOWY_DIORITE = register("snowy_diorite", new Block(Block.Settings.copy(Blocks.DIORITE)));

    // Just the variants that are used in BadlandsSurfaceBuilder
    public static final Block SNOWY_WHITE_TERRACOTTA = register("snowy_white_terracotta", new Block(Block.Settings.copy(Blocks.WHITE_TERRACOTTA)));
    public static final Block SNOWY_ORANGE_TERRACOTTA = register("snowy_orange_terracotta", new Block(Block.Settings.copy(Blocks.ORANGE_TERRACOTTA)));
    public static final Block SNOWY_TERRACOTTA = register("snowy_terracotta", new Block(Block.Settings.copy(Blocks.TERRACOTTA)));
    public static final Block SNOWY_YELLOW_TERRACOTTA = register("snowy_yellow_terracotta", new Block(Block.Settings.copy(Blocks.YELLOW_TERRACOTTA)));
    public static final Block SNOWY_BROWN_TERRACOTTA = register("snowy_brown_terracotta", new Block(Block.Settings.copy(Blocks.BROWN_TERRACOTTA)));
    public static final Block SNOWY_RED_TERRACOTTA = register("snowy_red_terracotta", new Block(Block.Settings.copy(Blocks.RED_TERRACOTTA)));
    public static final Block SNOWY_LIGHT_GRAY_TERRACOTTA = register("snowy_light_gray_terracotta", new Block(Block.Settings.copy(Blocks.LIGHT_GRAY_TERRACOTTA)));

    public static final Block SNOWY_GRASS_PATH = register("snowy_grass_path", new GrassPathBlock(Block.Settings.copy(Blocks.GRASS_PATH)) {});

    public static final Block SNOWY_OAK_LOG = register("snowy_oak_log", createLogBlock(MaterialColor.WOOD, MaterialColor.SPRUCE));
    public static final Block SNOWY_BIRCH_LOG = register("snowy_birch_log", createLogBlock(MaterialColor.SAND, MaterialColor.QUARTZ));
    public static final Block SNOWY_SPRUCE_LOG = register("snowy_spruce_log", createLogBlock(MaterialColor.SPRUCE, MaterialColor.BROWN));
    public static final Block SNOWY_JUNGLE_LOG = register("snowy_jungle_log", createLogBlock(MaterialColor.DIRT, MaterialColor.SPRUCE));
    public static final Block SNOWY_DARK_OAK_LOG = register("snowy_dark_oak_log", createLogBlock(MaterialColor.BROWN, MaterialColor.BROWN));
    public static final Block SNOWY_ACACIA_LOG = register("snowy_acacia_log", createLogBlock(MaterialColor.ORANGE, MaterialColor.STONE));

    public static final Block SNOWY_OAK_LEAVES = register("snowy_oak_leaves", new LeavesBlock(Block.Settings.copy(Blocks.OAK_LEAVES)));
    public static final Block SNOWY_BIRCH_LEAVES = register("snowy_birch_leaves", new LeavesBlock(Block.Settings.copy(Blocks.BIRCH_LEAVES)));
    public static final Block SNOWY_SPRUCE_LEAVES = register("snowy_spruce_leaves", new LeavesBlock(Block.Settings.copy(Blocks.SPRUCE_LEAVES)));
    public static final Block SNOWY_JUNGLE_LEAVES = register("snowy_jungle_leaves", new LeavesBlock(Block.Settings.copy(Blocks.JUNGLE_LEAVES)));
    public static final Block SNOWY_DARK_OAK_LEAVES = register("snowy_dark_oak_leaves", new LeavesBlock(Block.Settings.copy(Blocks.DARK_OAK_LEAVES)));
    public static final Block SNOWY_ACACIA_LEAVES = register("snowy_acacia_leaves", new LeavesBlock(Block.Settings.copy(Blocks.ACACIA_LEAVES)));

    public static final Block SNOWY_VINE = register("snowy_vine", new VineBlock(Block.Settings.copy(Blocks.VINE)));

    public static final Map<Block, Block> SNOWY_TERRAIN_BLOCKS = new HashMap<>(new ImmutableMap.Builder<Block, Block>()
        .put(Blocks.DIRT, SNOWY_DIRT)
        .put(Blocks.GRASS_BLOCK, SNOWY_DIRT)
        .put(Blocks.PODZOL, SNOWY_DIRT)
        .put(Blocks.COARSE_DIRT, SNOWY_COARSE_DIRT)
        .put(Blocks.SAND, SNOWY_SAND)
        .put(Blocks.RED_SAND, SNOWY_RED_SAND)
        .put(Blocks.GRAVEL, SNOWY_GRAVEL)
        .put(Blocks.STONE, SNOWY_STONE)
        .put(Blocks.GRANITE, SNOWY_GRANITE)
        .put(Blocks.ANDESITE, SNOWY_ANDESITE)
        .put(Blocks.DIORITE, SNOWY_DIORITE)
        .put(Blocks.WHITE_TERRACOTTA, SNOWY_WHITE_TERRACOTTA)
        .put(Blocks.ORANGE_TERRACOTTA, SNOWY_ORANGE_TERRACOTTA)
        .put(Blocks.TERRACOTTA, SNOWY_TERRACOTTA)
        .put(Blocks.YELLOW_TERRACOTTA, SNOWY_YELLOW_TERRACOTTA)
        .put(Blocks.BROWN_TERRACOTTA, SNOWY_BROWN_TERRACOTTA)
        .put(Blocks.RED_TERRACOTTA, SNOWY_RED_TERRACOTTA)
        .put(Blocks.LIGHT_GRAY_TERRACOTTA, SNOWY_LIGHT_GRAY_TERRACOTTA)
        .build()
    );
    public static final Map<Block, Block> SNOWY_SPECIAL_TERRAIN_BLOCKS = new HashMap<>(new ImmutableMap.Builder<Block, Block>()
        .put(Blocks.GRASS_PATH, SNOWY_GRASS_PATH)
        .put(Blocks.BLUE_ICE, Blocks.SNOW_BLOCK)
        .put(Blocks.PACKED_ICE, Blocks.SNOW_BLOCK)
        .build()
    );
    public static final Map<Block, Block> SNOWY_TREE_BLOCKS = new HashMap<>(new ImmutableMap.Builder<Block, Block>()
        .put(Blocks.OAK_LOG, SNOWY_OAK_LOG)
        .put(Blocks.BIRCH_LOG, SNOWY_BIRCH_LOG)
        .put(Blocks.SPRUCE_LOG, SNOWY_SPRUCE_LOG)
        .put(Blocks.JUNGLE_LOG, SNOWY_JUNGLE_LOG)
        .put(Blocks.DARK_OAK_LOG, SNOWY_DARK_OAK_LOG)
        .put(Blocks.ACACIA_LOG, SNOWY_ACACIA_LOG)
        .put(Blocks.OAK_LEAVES, SNOWY_OAK_LEAVES)
        .put(Blocks.BIRCH_LEAVES, SNOWY_BIRCH_LEAVES)
        .put(Blocks.SPRUCE_LEAVES, SNOWY_SPRUCE_LEAVES)
        .put(Blocks.JUNGLE_LEAVES, SNOWY_JUNGLE_LEAVES)
        .put(Blocks.DARK_OAK_LEAVES, SNOWY_DARK_OAK_LEAVES)
        .put(Blocks.ACACIA_LEAVES, SNOWY_ACACIA_LEAVES)
        .put(Blocks.VINE, SNOWY_VINE)
        .build()
    );

    public static void init()
    {
        // Fire properties - leaves and vines are lowered from vanilla
        FlammableBlockRegistry fireBlocks = FlammableBlockRegistry.getDefaultInstance();
        fireBlocks.add(SNOWY_OAK_LOG, 5, 5);
        fireBlocks.add(SNOWY_SPRUCE_LOG, 5, 5);
        fireBlocks.add(SNOWY_BIRCH_LOG, 5, 5);
        fireBlocks.add(SNOWY_JUNGLE_LOG, 5, 5);
        fireBlocks.add(SNOWY_ACACIA_LOG, 5, 5);
        fireBlocks.add(SNOWY_DARK_OAK_LOG, 5, 5);
        fireBlocks.add(SNOWY_OAK_LEAVES, 10, 20);
        fireBlocks.add(SNOWY_SPRUCE_LEAVES, 10, 20);
        fireBlocks.add(SNOWY_BIRCH_LEAVES, 10, 20);
        fireBlocks.add(SNOWY_JUNGLE_LEAVES, 10, 20);
        fireBlocks.add(SNOWY_ACACIA_LEAVES, 10, 20);
        fireBlocks.add(SNOWY_DARK_OAK_LEAVES, 10, 20);
        fireBlocks.add(SNOWY_VINE, 5, 30);

        // Block stripping - first we need to hack in mutability, then we can add special stripping "recipes"
        //noinspection ConstantConditions
        Map<Block, Block> blockStrippingMap = new HashMap<>(IAxeItem.primalwinter_getStrippedBlocks());
        blockStrippingMap.put(SNOWY_ACACIA_LOG, Blocks.ACACIA_LOG);
        blockStrippingMap.put(SNOWY_OAK_LOG, Blocks.OAK_LOG);
        blockStrippingMap.put(SNOWY_DARK_OAK_LOG, Blocks.DARK_OAK_LOG);
        blockStrippingMap.put(SNOWY_JUNGLE_LOG, Blocks.JUNGLE_LOG);
        blockStrippingMap.put(SNOWY_BIRCH_LOG, Blocks.BIRCH_LOG);
        blockStrippingMap.put(SNOWY_SPRUCE_LOG, Blocks.SPRUCE_LOG);
        IAxeItem.primalwinter_setStrippedBlocks(blockStrippingMap);
    }

    private static <T extends Block> T register(String id, T block)
    {
        return register(id, block, new BlockItem(block, new Item.Settings().group(ModItemGroups.BLOCKS)));
    }

    private static <T extends Block> T register(String id, T block, BlockItem blockItem)
    {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, id), block);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, id), blockItem);
        return block;
    }

    private static PillarBlock createLogBlock(MaterialColor topMaterialColor, MaterialColor sideMaterialColor)
    {
        return new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, (blockState) -> blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMaterialColor : sideMaterialColor).strength(2.0F).sounds(BlockSoundGroup.WOOD));
    }
}
