package com.alcatrazescapee.primalwinter.data;

import java.util.List;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.DesertVillagePools;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.data.worldgen.SavannaVillagePools;
import net.minecraft.data.worldgen.SnowyVillagePools;
import net.minecraft.data.worldgen.TaigaVillagePools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

/**
 * Modified from {@link net.minecraft.data.worldgen.VillagePools}, but modified to only include zombie villages
 */
public class BuiltinTemplatePools
{
    final BootstrapContext<StructureTemplatePool> context;
    final HolderGetter<StructureTemplatePool> structurePools;
    final HolderGetter<StructureProcessorList> processors;

    public BuiltinTemplatePools(BootstrapContext<StructureTemplatePool> context)
    {
        this.context = context;
        this.structurePools = context.lookup(Registries.TEMPLATE_POOL);
        this.processors = context.lookup(Registries.PROCESSOR_LIST);

        final Holder<StructureProcessorList> zombiePlains = processors.getOrThrow(ProcessorLists.ZOMBIE_PLAINS);
        final Holder<StructureProcessorList> zombieSavanna = processors.getOrThrow(ProcessorLists.ZOMBIE_SAVANNA);
        final Holder<StructureProcessorList> zombieDesert = processors.getOrThrow(ProcessorLists.ZOMBIE_DESERT);
        final Holder<StructureProcessorList> zombieTaiga = processors.getOrThrow(ProcessorLists.ZOMBIE_TAIGA);

        register(PlainVillagePools.START, List.of(
            Pair.of(StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_fountain_01", zombiePlains), 1),
            Pair.of(StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_meeting_point_1", zombiePlains), 1),
            Pair.of(StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_meeting_point_2", zombiePlains), 1),
            Pair.of(StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_meeting_point_3", zombiePlains), 1)
        ));
        register(SnowyVillagePools.START, List.of(
            Pair.of(StructurePoolElement.legacy("village/snowy/zombie/town_centers/snowy_meeting_point_1"), 2),
            Pair.of(StructurePoolElement.legacy("village/snowy/zombie/town_centers/snowy_meeting_point_2"), 1),
            Pair.of(StructurePoolElement.legacy("village/snowy/zombie/town_centers/snowy_meeting_point_3"), 3)
        ));
        register(SavannaVillagePools.START, List.of(
            Pair.of(StructurePoolElement.legacy("village/savanna/zombie/town_centers/savanna_meeting_point_1", zombieSavanna), 2),
            Pair.of(StructurePoolElement.legacy("village/savanna/zombie/town_centers/savanna_meeting_point_2", zombieSavanna), 1),
            Pair.of(StructurePoolElement.legacy("village/savanna/zombie/town_centers/savanna_meeting_point_3", zombieSavanna), 3),
            Pair.of(StructurePoolElement.legacy("village/savanna/zombie/town_centers/savanna_meeting_point_4", zombieSavanna), 3)
        ));
        register(DesertVillagePools.START, List.of(
            Pair.of(StructurePoolElement.legacy("village/desert/zombie/town_centers/desert_meeting_point_1", zombieDesert), 2),
            Pair.of(StructurePoolElement.legacy("village/desert/zombie/town_centers/desert_meeting_point_2", zombieDesert), 2),
            Pair.of(StructurePoolElement.legacy("village/desert/zombie/town_centers/desert_meeting_point_3", zombieDesert), 1)
        ));
        register(TaigaVillagePools.START, List.of(
            Pair.of(StructurePoolElement.legacy("village/taiga/zombie/town_centers/taiga_meeting_point_1", zombieTaiga), 1),
            Pair.of(StructurePoolElement.legacy("village/taiga/zombie/town_centers/taiga_meeting_point_2", zombieTaiga), 1)
        ));
    }

    void register(ResourceKey<StructureTemplatePool> key, List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> factories)
    {
        context.register(key, new StructureTemplatePool(structurePools.getOrThrow(Pools.EMPTY), factories, StructureTemplatePool.Projection.RIGID));
    }
}
