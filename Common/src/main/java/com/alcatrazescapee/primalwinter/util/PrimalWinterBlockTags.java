package com.alcatrazescapee.primalwinter.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class PrimalWinterBlockTags
{
    public static final TagKey<Block> REPLACEABLE_WITH_SNOW = tag("replaceable_with_snow");
    public static final TagKey<Block> REPLACEABLE_WITH_SNOWY_STUFF = tag("replaceable_with_snowy_stuff");
    public static final TagKey<Block> REPLACEABLE_WITH_SNOWY_BLOCK_UNDERNEATH = tag("replaceable_with_snowy_block_underneath");
    public static final TagKey<Block> SNOWY_SUGAR_CANE_SURVIVES_ON = tag("snowy_sugar_cane_survives_on");
    public static final TagKey<Block> SNOWY_CACTUS_SURVIVES_ON = tag("snowy_cactus_survives_on");
    public static final TagKey<Block> SNOWY_BAMBOO_SURVIVES_ON = tag("snowy_bamboo_survives_on");

    private static TagKey<Block> tag(String name)
    {
        return TagKey.create(Registries.BLOCK, Helpers.identifier(name));
    }
}
