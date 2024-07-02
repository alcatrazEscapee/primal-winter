package com.alcatrazescapee.primalwinter.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class PrimalWinterBlockTags
{
    public static final TagKey<Block> REPLACEABLE_WITH_SNOW = tag("replaceable_with_snow");
    public static final TagKey<Block> REPLACEABLE_WITH_SNOWY_STUFF = tag("replaceable_with_snowy_stuff");

    private static TagKey<Block> tag(String name)
    {
        return TagKey.create(Registries.BLOCK, Helpers.identifier(name));
    }
}
