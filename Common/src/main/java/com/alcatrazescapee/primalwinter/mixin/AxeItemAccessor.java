package com.alcatrazescapee.primalwinter.mixin;

import java.util.Map;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AxeItem.class)
public interface AxeItemAccessor
{
    @Accessor("STRIPPABLES")
    static Map<Block, Block> accessor$getStrippables()
    {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("STRIPPABLES")
    static void accessor$setStrippables(Map<Block, Block> strippables) {}
}
