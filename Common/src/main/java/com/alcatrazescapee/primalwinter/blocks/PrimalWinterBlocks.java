package com.alcatrazescapee.primalwinter.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CherryLeavesBlock;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.MangroveRootsBlock;
import net.minecraft.world.level.block.MudBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import com.alcatrazescapee.primalwinter.mixin.AxeItemAccessor;
import com.alcatrazescapee.primalwinter.platform.RegistryHolder;
import com.alcatrazescapee.primalwinter.platform.RegistryInterface;
import com.alcatrazescapee.primalwinter.platform.XPlatform;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.*;

public final class PrimalWinterBlocks
{
    public static final RegistryInterface<Block> BLOCKS = XPlatform.INSTANCE.registryInterface(BuiltInRegistries.BLOCK);
    public static final RegistryInterface<Item> ITEMS = XPlatform.INSTANCE.registryInterface(BuiltInRegistries.ITEM);

    public static final RegistryHolder<Block> SNOWY_DIRT = register("snowy_dirt", () -> new Block(ofFullCopy(Blocks.DIRT)));
    public static final RegistryHolder<Block> SNOWY_COARSE_DIRT = register("snowy_coarse_dirt", () -> new Block(ofFullCopy(Blocks.COARSE_DIRT)));
    public static final RegistryHolder<Block> SNOWY_SAND = register("snowy_sand", () -> new ColoredFallingBlock(new ColorRGBA(0xdbd3a0), ofFullCopy(Blocks.SAND)));
    public static final RegistryHolder<Block> SNOWY_RED_SAND = register("snowy_red_sand", () -> new ColoredFallingBlock(new ColorRGBA(0xa95821), ofFullCopy(Blocks.RED_SAND)));
    public static final RegistryHolder<Block> SNOWY_GRAVEL = register("snowy_gravel", () -> new ColoredFallingBlock(new ColorRGBA(0xff807c7b), ofFullCopy(Blocks.GRAVEL)));
    public static final RegistryHolder<Block> SNOWY_MUD = register("snowy_mud", () -> new MudBlock(ofFullCopy(Blocks.MUD)));

    public static final RegistryHolder<Block> SNOWY_STONE = register("snowy_stone", () -> new Block(ofFullCopy(Blocks.STONE)));
    public static final RegistryHolder<Block> SNOWY_GRANITE = register("snowy_granite", () -> new Block(ofFullCopy(Blocks.GRANITE)));
    public static final RegistryHolder<Block> SNOWY_ANDESITE = register("snowy_andesite", () -> new Block(ofFullCopy(Blocks.ANDESITE)));
    public static final RegistryHolder<Block> SNOWY_DIORITE = register("snowy_diorite", () -> new Block(ofFullCopy(Blocks.DIORITE)));

    public static final RegistryHolder<Block> SNOWY_WHITE_TERRACOTTA = register("snowy_white_terracotta", () -> new Block(ofFullCopy(Blocks.WHITE_TERRACOTTA)));
    public static final RegistryHolder<Block> SNOWY_ORANGE_TERRACOTTA = register("snowy_orange_terracotta", () -> new Block(ofFullCopy(Blocks.ORANGE_TERRACOTTA)));
    public static final RegistryHolder<Block> SNOWY_TERRACOTTA = register("snowy_terracotta", () -> new Block(ofFullCopy(Blocks.TERRACOTTA)));
    public static final RegistryHolder<Block> SNOWY_YELLOW_TERRACOTTA = register("snowy_yellow_terracotta", () -> new Block(ofFullCopy(Blocks.YELLOW_TERRACOTTA)));
    public static final RegistryHolder<Block> SNOWY_BROWN_TERRACOTTA = register("snowy_brown_terracotta", () -> new Block(ofFullCopy(Blocks.BROWN_TERRACOTTA)));
    public static final RegistryHolder<Block> SNOWY_RED_TERRACOTTA = register("snowy_red_terracotta", () -> new Block(ofFullCopy(Blocks.RED_TERRACOTTA)));
    public static final RegistryHolder<Block> SNOWY_LIGHT_GRAY_TERRACOTTA = register("snowy_light_gray_terracotta", () -> new Block(ofFullCopy(Blocks.LIGHT_GRAY_TERRACOTTA)));

    public static final RegistryHolder<Block> SNOWY_DIRT_PATH = register("snowy_dirt_path", () -> new DirtPathBlock(ofFullCopy(Blocks.DIRT_PATH)) {});

    public static final RegistryHolder<RotatedPillarBlock> SNOWY_OAK_LOG = register("snowy_oak_log", () -> new RotatedPillarBlock(ofFullCopy(Blocks.OAK_LOG)));
    public static final RegistryHolder<RotatedPillarBlock> SNOWY_BIRCH_LOG = register("snowy_birch_log", () -> new RotatedPillarBlock(ofFullCopy(Blocks.BIRCH_LOG)));
    public static final RegistryHolder<RotatedPillarBlock> SNOWY_SPRUCE_LOG = register("snowy_spruce_log", () -> new RotatedPillarBlock(ofFullCopy(Blocks.SPRUCE_LOG)));
    public static final RegistryHolder<RotatedPillarBlock> SNOWY_JUNGLE_LOG = register("snowy_jungle_log", () -> new RotatedPillarBlock(ofFullCopy(Blocks.JUNGLE_LOG)));
    public static final RegistryHolder<RotatedPillarBlock> SNOWY_DARK_OAK_LOG = register("snowy_dark_oak_log", () -> new RotatedPillarBlock(ofFullCopy(Blocks.DARK_OAK_LOG)));
    public static final RegistryHolder<RotatedPillarBlock> SNOWY_ACACIA_LOG = register("snowy_acacia_log", () -> new RotatedPillarBlock(ofFullCopy(Blocks.ACACIA_LOG)));
    public static final RegistryHolder<RotatedPillarBlock> SNOWY_CHERRY_LOG = register("snowy_cherry_log", () -> new RotatedPillarBlock(ofFullCopy(Blocks.CHERRY_LOG)));
    public static final RegistryHolder<RotatedPillarBlock> SNOWY_MANGROVE_LOG = register("snowy_mangrove_log", () -> new RotatedPillarBlock(ofFullCopy(Blocks.MANGROVE_LOG)));

    public static final RegistryHolder<Block> SNOWY_OAK_LEAVES = register("snowy_oak_leaves", () -> new LeavesBlock(ofFullCopy(Blocks.OAK_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_BIRCH_LEAVES = register("snowy_birch_leaves", () -> new LeavesBlock(ofFullCopy(Blocks.BIRCH_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_SPRUCE_LEAVES = register("snowy_spruce_leaves", () -> new LeavesBlock(ofFullCopy(Blocks.SPRUCE_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_JUNGLE_LEAVES = register("snowy_jungle_leaves", () -> new LeavesBlock(ofFullCopy(Blocks.JUNGLE_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_DARK_OAK_LEAVES = register("snowy_dark_oak_leaves", () -> new LeavesBlock(ofFullCopy(Blocks.DARK_OAK_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_ACACIA_LEAVES = register("snowy_acacia_leaves", () -> new LeavesBlock(ofFullCopy(Blocks.ACACIA_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_CHERRY_LEAVES = register("snowy_cherry_leaves", () -> new CherryLeavesBlock(ofFullCopy(Blocks.CHERRY_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_MANGROVE_LEAVES = register("snowy_mangrove_leaves", () -> new LeavesBlock(ofFullCopy(Blocks.MANGROVE_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_MANGROVE_ROOTS = register("snowy_mangrove_roots", () -> new MangroveRootsBlock(ofFullCopy(Blocks.MANGROVE_ROOTS)) {});
    public static final RegistryHolder<Block> SNOWY_MUDDY_MANGROVE_ROOTS = register("snowy_muddy_mangrove_roots", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).strength(0.7F).sound(SoundType.MUDDY_MANGROVE_ROOTS)));

    public static final RegistryHolder<Block> SNOWY_VINE = register("snowy_vine", () -> new SnowyVineBlock(ofFullCopy(Blocks.VINE)));
    public static final RegistryHolder<Block> SNOWY_SUGAR_CANE = register("snowy_sugar_cane", () -> new SnowySugarCaneBlock(ofFullCopy(Blocks.SUGAR_CANE)));
    public static final RegistryHolder<Block> SNOWY_CACTUS = register("snowy_cactus", () -> new SnowyCactusBlock(ofFullCopy(Blocks.CACTUS)));
    public static final RegistryHolder<Block> SNOWY_BAMBOO = register("snowy_bamboo", () -> new SnowyBambooBlock(ofFullCopy(Blocks.BAMBOO)));

    public static final RegistryHolder<Block> SNOWY_BROWN_MUSHROOM_BLOCK = register("snowy_brown_mushroom_block", () -> new HugeMushroomBlock(ofFullCopy(Blocks.BROWN_MUSHROOM_BLOCK)));
    public static final RegistryHolder<Block> SNOWY_RED_MUSHROOM_BLOCK = register("snowy_red_mushroom_block", () -> new HugeMushroomBlock(ofFullCopy(Blocks.RED_MUSHROOM_BLOCK)));
    public static final RegistryHolder<Block> SNOWY_MUSHROOM_STEM = register("snowy_mushroom_stem", () -> new HugeMushroomBlock(ofFullCopy(Blocks.MUSHROOM_STEM)));

    public static final Map<Block, Supplier<Block>> SNOWY_TERRAIN_BLOCKS = new HashMap<>(new ImmutableMap.Builder<Block, Supplier<Block>>()
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

    public static final Map<Block, Supplier<Block>> SNOWY_SPECIAL_TERRAIN_BLOCKS = new ImmutableMap.Builder<Block, Supplier<Block>>()
        .put(Blocks.DIRT_PATH, SNOWY_DIRT_PATH)
        .put(Blocks.MUD, SNOWY_MUD)
        .put(Blocks.BLUE_ICE, () -> Blocks.SNOW_BLOCK)
        .put(Blocks.PACKED_ICE, () -> Blocks.SNOW_BLOCK)
        .build();

    public static final Map<Block, Supplier<? extends Block>> SNOWY_DIRECT_REPLACEMENT_BLOCKS = new ImmutableMap.Builder<Block, Supplier<? extends Block>>()
        .put(Blocks.OAK_LOG, SNOWY_OAK_LOG)
        .put(Blocks.BIRCH_LOG, SNOWY_BIRCH_LOG)
        .put(Blocks.SPRUCE_LOG, SNOWY_SPRUCE_LOG)
        .put(Blocks.JUNGLE_LOG, SNOWY_JUNGLE_LOG)
        .put(Blocks.DARK_OAK_LOG, SNOWY_DARK_OAK_LOG)
        .put(Blocks.ACACIA_LOG, SNOWY_ACACIA_LOG)
        .put(Blocks.CHERRY_LOG, SNOWY_CHERRY_LOG)
        .put(Blocks.MANGROVE_LOG, SNOWY_MANGROVE_LOG)
        .put(Blocks.OAK_LEAVES, SNOWY_OAK_LEAVES)
        .put(Blocks.BIRCH_LEAVES, SNOWY_BIRCH_LEAVES)
        .put(Blocks.SPRUCE_LEAVES, SNOWY_SPRUCE_LEAVES)
        .put(Blocks.JUNGLE_LEAVES, SNOWY_JUNGLE_LEAVES)
        .put(Blocks.DARK_OAK_LEAVES, SNOWY_DARK_OAK_LEAVES)
        .put(Blocks.ACACIA_LEAVES, SNOWY_ACACIA_LEAVES)
        .put(Blocks.CHERRY_LEAVES, SNOWY_CHERRY_LEAVES)
        .put(Blocks.MANGROVE_LEAVES, SNOWY_MANGROVE_LEAVES)
        .put(Blocks.MANGROVE_ROOTS, SNOWY_MANGROVE_ROOTS)
        .put(Blocks.MUDDY_MANGROVE_ROOTS, SNOWY_MUDDY_MANGROVE_ROOTS)
        .put(Blocks.VINE, SNOWY_VINE)
        .put(Blocks.SUGAR_CANE, SNOWY_SUGAR_CANE)
        .put(Blocks.CACTUS, SNOWY_CACTUS)
        .put(Blocks.BAMBOO, SNOWY_BAMBOO)
        .put(Blocks.BROWN_MUSHROOM_BLOCK, SNOWY_BROWN_MUSHROOM_BLOCK)
        .put(Blocks.RED_MUSHROOM_BLOCK, SNOWY_RED_MUSHROOM_BLOCK)
        .put(Blocks.MUSHROOM_STEM, SNOWY_MUSHROOM_STEM)
        .build();

    public static final Map<Supplier<? extends Block>, Supplier<? extends Block>> SNOWY_LOG_STRIPPING_BLOCKS = new ImmutableMap.Builder<Supplier<? extends Block>, Supplier<? extends Block>>()
        .put(SNOWY_OAK_LOG, () -> Blocks.OAK_LOG)
        .put(SNOWY_BIRCH_LOG, () -> Blocks.BIRCH_LOG)
        .put(SNOWY_SPRUCE_LOG, () -> Blocks.SPRUCE_LOG)
        .put(SNOWY_JUNGLE_LOG, () -> Blocks.JUNGLE_LOG)
        .put(SNOWY_DARK_OAK_LOG, () -> Blocks.DARK_OAK_LOG)
        .put(SNOWY_ACACIA_LOG, () -> Blocks.ACACIA_LOG)
        .put(SNOWY_CHERRY_LOG, () -> Blocks.CHERRY_LOG)
        .put(SNOWY_MANGROVE_LOG, () -> Blocks.MANGROVE_LOG)
        .build();

    public static void registerAxeStrippables()
    {
        final Map<Block, Block> strippables = new HashMap<>(AxeItemAccessor.accessor$getStrippables());
        SNOWY_LOG_STRIPPING_BLOCKS.forEach((from, to) -> strippables.put(from.get(), to.get()));
        AxeItemAccessor.accessor$setStrippables(strippables);
    }

    private static <T extends Block> RegistryHolder<T> register(String name, Supplier<T> blockFactory)
    {
        final RegistryHolder<T> block = BLOCKS.register(name, blockFactory);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        PrimalWinterItemGroups.ENTRIES.add(block);
        return block;
    }
}
