/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.common;

import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.tag.TagRegistry;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

public final class ModTags
{
    public static final class Blocks
    {
        public static final Tag<Block> ANIMAL_SPAWNS_ON = TagRegistry.block(new Identifier(MOD_ID, "animal_spawns_on"));
    }
}
