package com.alcatrazescapee.primalwinter;

import java.util.List;
import java.util.Set;
import com.alcatrazescapee.primalwinter.data.BuiltinBlockLoot;
import com.alcatrazescapee.primalwinter.data.BuiltinBlockTags;
import com.alcatrazescapee.primalwinter.data.BuiltinFeatures;
import com.alcatrazescapee.primalwinter.data.BuiltinI18n;
import com.alcatrazescapee.primalwinter.data.BuiltinModels;
import com.alcatrazescapee.primalwinter.data.BuiltinPlacedFeatures;
import com.alcatrazescapee.primalwinter.data.BuiltinTemplatePools;
import com.alcatrazescapee.primalwinter.data.NeoForgeBiomeModifiers;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@EventBusSubscriber(modid = PrimalWinter.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class EntryPoint
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        event.getGenerator().addProvider(true, new BuiltinI18n(event));
        event.getGenerator().addProvider(true, new BuiltinModels(event));
        event.getGenerator().addProvider(true, new BuiltinBlockTags(event));
        event.getGenerator().addProvider(true,
            (DataProvider.Factory<? extends DataProvider>) output -> new LootTableProvider(
                output, Set.of(), List.of(
                    new LootTableProvider.SubProviderEntry(BuiltinBlockLoot::new, LootContextParamSets.BLOCK)
                ), event.getLookupProvider()));
        event.getGenerator().addProvider(true,
            (DataProvider.Factory<? extends DataProvider>) output -> new DatapackBuiltinEntriesProvider(
                output, event.getLookupProvider(), new RegistrySetBuilder()
                    .add(Registries.CONFIGURED_FEATURE, BuiltinFeatures::new)
                    .add(Registries.PLACED_FEATURE, BuiltinPlacedFeatures::new)
                    .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NeoForgeBiomeModifiers::new)
                    .add(Registries.TEMPLATE_POOL, BuiltinTemplatePools::new),
                Set.of(PrimalWinter.MOD_ID, "minecraft")));
    }
}
