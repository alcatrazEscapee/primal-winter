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
        addBlock(SNOWY_OAK_LOG, "Frosted Oak Log");
        addBlock(SNOWY_BIRCH_LOG, "Frosted Birch Log");
        addBlock(SNOWY_SPRUCE_LOG, "Frosted Spruce Log");
        addBlock(SNOWY_JUNGLE_LOG, "Frosted Jungle Log");
        addBlock(SNOWY_DARK_OAK_LOG, "Frosted Dark Oak Log");
        addBlock(SNOWY_ACACIA_LOG, "Frosted Acacia Log");
        addBlock(SNOWY_CHERRY_LOG, "Frosted Cherry Log");
        addBlock(SNOWY_MANGROVE_LOG, "Frosted Mangrove Log");
        addBlock(SNOWY_OAK_LEAVES, "Frosted Oak Leaves");
        addBlock(SNOWY_BIRCH_LEAVES, "Frosted Birch Leaves");
        addBlock(SNOWY_SPRUCE_LEAVES, "Frosted Spruce Leaves");
        addBlock(SNOWY_JUNGLE_LEAVES, "Frosted Jungle Leaves");
        addBlock(SNOWY_DARK_OAK_LEAVES, "Frosted Dark Oak Leaves");
        addBlock(SNOWY_ACACIA_LEAVES, "Frosted Acacia Leaves");
        addBlock(SNOWY_CHERRY_LEAVES, "Frosted Cherry Leaves");
        addBlock(SNOWY_MANGROVE_LEAVES, "Frosted Mangrove Leaves");
        addBlock(SNOWY_MANGROVE_ROOTS, "Frosted Mangrove Roots");
        addBlock(SNOWY_MUDDY_MANGROVE_ROOTS, "Frosted Muddy Mangrove Roots");
        addBlock(SNOWY_VINE, "Frozen Vine");
        addBlock(SNOWY_SUGAR_CANE, "Frozen Sugar Cane");
        addBlock(SNOWY_CACTUS, "Frozen Cactus");
    }
}
