package com.alcatrazescapee.primalwinter.data;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class BuiltinEntityTags extends EntityTypeTagsProvider
{
    public BuiltinEntityTags(GatherDataEvent event)
    {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), PrimalWinter.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
            .addTag(EntityTypeTags.UNDEAD)
            .addTag(EntityTypeTags.RAIDERS)
            .add(
                EntityType.SPIDER,
                EntityType.CREEPER);
        tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)
            .addTag(EntityTypeTags.UNDEAD)
            .addTag(EntityTypeTags.RAIDERS)
            .add(
                EntityType.WOLF,
                EntityType.WANDERING_TRADER,
                EntityType.TRADER_LLAMA,
                EntityType.SPIDER,
                EntityType.CREEPER);
    }
}
