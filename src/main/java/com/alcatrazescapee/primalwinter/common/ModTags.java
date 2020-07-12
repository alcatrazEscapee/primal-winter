/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.common;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

public class ModTags
{
    public static class Blocks
    {
        public static final Tag<Block> TURTLE_SPAWNS_ON = new BlockTags.Wrapper(new ResourceLocation(MOD_ID, "turtle_spawns_on"));
        public static final Tag<Block> ANIMAL_SPAWNS_ON = new BlockTags.Wrapper(new ResourceLocation(MOD_ID, "animal_spawns_on"));
    }
}
