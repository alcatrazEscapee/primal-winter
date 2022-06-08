package com.alcatrazescapee.primalwinter.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.alcatrazescapee.primalwinter.mixin.AxeItemAccessor;
import com.alcatrazescapee.primalwinter.platform.RegistryHolder;
import com.alcatrazescapee.primalwinter.platform.RegistryInterface;
import com.alcatrazescapee.primalwinter.platform.XPlatform;

public final class PrimalWinterBlocks
{
    public static final RegistryInterface<Block> BLOCKS = XPlatform.INSTANCE.registryInterface(Registry.BLOCK);
    public static final RegistryInterface<Item> ITEMS = XPlatform.INSTANCE.registryInterface(Registry.ITEM);

    public static final RegistryHolder<Block> SNOWY_DIRT = register("snowy_dirt", () -> new Block(Block.Properties.copy(Blocks.DIRT)));
    public static final RegistryHolder<Block> SNOWY_COARSE_DIRT = register("snowy_coarse_dirt", () -> new Block(Block.Properties.copy(Blocks.COARSE_DIRT)));
    public static final RegistryHolder<Block> SNOWY_SAND = register("snowy_sand", () -> new SandBlock(0xdbd3a0, Block.Properties.copy(Blocks.SAND)));
    public static final RegistryHolder<Block> SNOWY_RED_SAND = register("snowy_red_sand", () -> new SandBlock(0xa95821, Block.Properties.copy(Blocks.RED_SAND)));
    public static final RegistryHolder<Block> SNOWY_GRAVEL = register("snowy_gravel", () -> new GravelBlock(Block.Properties.copy(Blocks.GRAVEL)));

    public static final RegistryHolder<Block> SNOWY_STONE = register("snowy_stone", () -> new Block(Block.Properties.copy(Blocks.STONE)));
    public static final RegistryHolder<Block> SNOWY_GRANITE = register("snowy_granite", () -> new Block(Block.Properties.copy(Blocks.GRANITE)));
    public static final RegistryHolder<Block> SNOWY_ANDESITE = register("snowy_andesite", () -> new Block(Block.Properties.copy(Blocks.ANDESITE)));
    public static final RegistryHolder<Block> SNOWY_DIORITE = register("snowy_diorite", () -> new Block(Block.Properties.copy(Blocks.DIORITE)));

    // Just the variants that are used in BadlandsSurfaceBuilder
    public static final RegistryHolder<Block> SNOWY_WHITE_TERRACOTTA = register("snowy_white_terracotta", () -> new Block(Block.Properties.copy(Blocks.WHITE_TERRACOTTA)));
    public static final RegistryHolder<Block> SNOWY_ORANGE_TERRACOTTA = register("snowy_orange_terracotta", () -> new Block(Block.Properties.copy(Blocks.ORANGE_TERRACOTTA)));
    public static final RegistryHolder<Block> SNOWY_TERRACOTTA = register("snowy_terracotta", () -> new Block(Block.Properties.copy(Blocks.TERRACOTTA)));
    public static final RegistryHolder<Block> SNOWY_YELLOW_TERRACOTTA = register("snowy_yellow_terracotta", () -> new Block(Block.Properties.copy(Blocks.YELLOW_TERRACOTTA)));
    public static final RegistryHolder<Block> SNOWY_BROWN_TERRACOTTA = register("snowy_brown_terracotta", () -> new Block(Block.Properties.copy(Blocks.BROWN_TERRACOTTA)));
    public static final RegistryHolder<Block> SNOWY_RED_TERRACOTTA = register("snowy_red_terracotta", () -> new Block(Block.Properties.copy(Blocks.RED_TERRACOTTA)));
    public static final RegistryHolder<Block> SNOWY_LIGHT_GRAY_TERRACOTTA = register("snowy_light_gray_terracotta", () -> new Block(Block.Properties.copy(Blocks.LIGHT_GRAY_TERRACOTTA)));

    public static final RegistryHolder<Block> SNOWY_DIRT_PATH = register("snowy_dirt_path", () -> new DirtPathBlock(Block.Properties.copy(Blocks.DIRT_PATH)) {});

    public static final RegistryHolder<Block> SNOWY_OAK_LOG = register("snowy_oak_log", () -> log(MaterialColor.WOOD, MaterialColor.PODZOL));
    public static final RegistryHolder<Block> SNOWY_BIRCH_LOG = register("snowy_birch_log", () -> log(MaterialColor.SAND, MaterialColor.QUARTZ));
    public static final RegistryHolder<Block> SNOWY_SPRUCE_LOG = register("snowy_spruce_log", () -> log(MaterialColor.PODZOL, MaterialColor.COLOR_BROWN));
    public static final RegistryHolder<Block> SNOWY_JUNGLE_LOG = register("snowy_jungle_log", () -> log(MaterialColor.DIRT, MaterialColor.PODZOL));
    public static final RegistryHolder<Block> SNOWY_DARK_OAK_LOG = register("snowy_dark_oak_log", () -> log(MaterialColor.COLOR_BROWN, MaterialColor.COLOR_BROWN));
    public static final RegistryHolder<Block> SNOWY_ACACIA_LOG = register("snowy_acacia_log", () -> log(MaterialColor.COLOR_ORANGE, MaterialColor.STONE));

    public static final RegistryHolder<Block> SNOWY_OAK_LEAVES = register("snowy_oak_leaves", () -> new LeavesBlock(Block.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_BIRCH_LEAVES = register("snowy_birch_leaves", () -> new LeavesBlock(Block.Properties.copy(Blocks.BIRCH_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_SPRUCE_LEAVES = register("snowy_spruce_leaves", () -> new LeavesBlock(Block.Properties.copy(Blocks.SPRUCE_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_JUNGLE_LEAVES = register("snowy_jungle_leaves", () -> new LeavesBlock(Block.Properties.copy(Blocks.JUNGLE_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_DARK_OAK_LEAVES = register("snowy_dark_oak_leaves", () -> new LeavesBlock(Block.Properties.copy(Blocks.DARK_OAK_LEAVES)));
    public static final RegistryHolder<Block> SNOWY_ACACIA_LEAVES = register("snowy_acacia_leaves", () -> new LeavesBlock(Block.Properties.copy(Blocks.ACACIA_LEAVES)));

    public static final RegistryHolder<Block> SNOWY_VINE = register("snowy_vine", () -> new VineBlock(Block.Properties.copy(Blocks.VINE)));

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
        .put(Blocks.BLUE_ICE, () -> Blocks.SNOW_BLOCK)
        .put(Blocks.PACKED_ICE, () -> Blocks.SNOW_BLOCK)
        .build();

    public static final Map<Block, Supplier<Block>> SNOWY_TREE_BLOCKS = new ImmutableMap.Builder<Block, Supplier<Block>>()
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
        .build();

    public static final Map<Supplier<Block>, Supplier<Block>> SNOWY_LOG_STRIPPING_BLOCKS = new ImmutableMap.Builder<Supplier<Block>, Supplier<Block>>()
        .put(SNOWY_OAK_LOG, () -> Blocks.OAK_LOG)
        .put(SNOWY_BIRCH_LOG, () -> Blocks.BIRCH_LOG)
        .put(SNOWY_SPRUCE_LOG, () -> Blocks.SPRUCE_LOG)
        .put(SNOWY_JUNGLE_LOG, () -> Blocks.JUNGLE_LOG)
        .put(SNOWY_DARK_OAK_LOG, () -> Blocks.DARK_OAK_LOG)
        .put(SNOWY_ACACIA_LOG, () -> Blocks.ACACIA_LOG)
        .build();

    public static void registerAxeStrippables()
    {
        final Map<Block, Block> strippables = new HashMap<>(AxeItemAccessor.accessor$getStrippables());
        SNOWY_LOG_STRIPPING_BLOCKS.forEach((from, to) -> strippables.put(from.get(), to.get()));
        AxeItemAccessor.accessor$setStrippables(strippables);
    }

    private static Block log(MaterialColor topColor, MaterialColor barkColor)
    {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : barkColor).strength(2.0F).sound(SoundType.WOOD));
    }

    private static <T extends Block> RegistryHolder<T> register(String name, Supplier<T> blockFactory)
    {
        return register(name, blockFactory, block -> new BlockItem(block, new Item.Properties().tab(Tab.TAB)));
    }

    private static <T extends Block> RegistryHolder<T> register(String name, Supplier<T> blockFactory, Function<T, ? extends BlockItem> blockItemFactory)
    {
        final RegistryHolder<T> block = BLOCKS.register(name, blockFactory);
        ITEMS.register(name, () -> blockItemFactory.apply(block.get()));
        return block;
    }

    public static final class Tab
    {
        public static final CreativeModeTab TAB = XPlatform.INSTANCE.creativeTab(new ResourceLocation(PrimalWinter.MOD_ID), () -> new ItemStack(SNOWY_DIRT.get()));
    }
}
