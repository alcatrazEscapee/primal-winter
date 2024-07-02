package com.alcatrazescapee.primalwinter.data;

import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static com.alcatrazescapee.primalwinter.PrimalWinter.*;
import static com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks.*;

public class BuiltinI18n extends LanguageProvider
{
    public BuiltinI18n(GatherDataEvent event)
    {
        super(event.getGenerator().getPackOutput(), MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations()
    {
        add(MOD_ID + ".items", "Primal Winter");
        add(MOD_ID + ".subtitle.wind", "Wind Howls");

        addBlock(SNOWY_DIRT, "Snowy Dirt");
        addBlock(SNOWY_COARSE_DIRT, "Snowy Coarse Dirt");
        addBlock(SNOWY_SAND, "Snowy Sand");
        addBlock(SNOWY_RED_SAND, "Snowy Red Sand");
        addBlock(SNOWY_GRAVEL, "Snowy Gravel");
        addBlock(SNOWY_MUD, "Snowy Mud");
        addBlock(SNOWY_STONE, "Snowy Stone");
        addBlock(SNOWY_GRANITE, "Snowy Granite");
        addBlock(SNOWY_ANDESITE, "Snowy Andesite");
        addBlock(SNOWY_DIORITE, "Snowy Diorite");
        addBlock(SNOWY_WHITE_TERRACOTTA, "Snowy White Terracotta");
        addBlock(SNOWY_ORANGE_TERRACOTTA, "Snowy Orange Terracotta");
        addBlock(SNOWY_TERRACOTTA, "Snowy Terracotta");
        addBlock(SNOWY_YELLOW_TERRACOTTA, "Snowy Yellow Terracotta");
        addBlock(SNOWY_BROWN_TERRACOTTA, "Snowy Brown Terracotta");
        addBlock(SNOWY_RED_TERRACOTTA, "Snowy Red Terracotta");
        addBlock(SNOWY_LIGHT_GRAY_TERRACOTTA, "Snowy Light Gray Terracotta");
        addBlock(SNOWY_DIRT_PATH, "Snowy Dirt Path");
        addBlock(SNOWY_OAK_LOG, "Snowy Oak Log");
        addBlock(SNOWY_BIRCH_LOG, "Snowy Birch Log");
        addBlock(SNOWY_SPRUCE_LOG, "Snowy Spruce Log");
        addBlock(SNOWY_JUNGLE_LOG, "Snowy Jungle Log");
        addBlock(SNOWY_DARK_OAK_LOG, "Snowy Dark Oak Log");
        addBlock(SNOWY_ACACIA_LOG, "Snowy Acacia Log");
        addBlock(SNOWY_CHERRY_LOG, "Snowy Cherry Log");
        addBlock(SNOWY_MANGROVE_LOG, "Snowy Mangrove Log");
        addBlock(SNOWY_OAK_LEAVES, "Snowy Oak Leaves");
        addBlock(SNOWY_BIRCH_LEAVES, "Snowy Birch Leaves");
        addBlock(SNOWY_SPRUCE_LEAVES, "Snowy Spruce Leaves");
        addBlock(SNOWY_JUNGLE_LEAVES, "Snowy Jungle Leaves");
        addBlock(SNOWY_DARK_OAK_LEAVES, "Snowy Dark Oak Leaves");
        addBlock(SNOWY_ACACIA_LEAVES, "Snowy Acacia Leaves");
        addBlock(SNOWY_CHERRY_LEAVES, "Snowy Cherry Leaves");
        addBlock(SNOWY_MANGROVE_LEAVES, "Snowy Mangrove Leaves");
        addBlock(SNOWY_MANGROVE_ROOTS, "Snowy Mangrove Roots");
        addBlock(SNOWY_MUDDY_MANGROVE_ROOTS, "Snowy Muddy Mangrove Roots");
        addBlock(SNOWY_VINE, "Snowy Vine");
    }
}
