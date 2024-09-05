package com.alcatrazescapee.primalwinter.data;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import com.alcatrazescapee.primalwinter.platform.ForgeRegistryInterface;
import com.google.common.base.Preconditions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static com.alcatrazescapee.primalwinter.PrimalWinter.*;
import static com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks.*;

public class BuiltinI18n extends LanguageProvider
{
    private final Set<Block> untranslatedBlocks = ((ForgeRegistryInterface<Block>) BLOCKS).deferred.getEntries()
        .stream()
        .map(Supplier::get)
        .collect(Collectors.toSet());

    public BuiltinI18n(GatherDataEvent event)
    {
        super(event.getGenerator().getPackOutput(), MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations()
    {
        add(MOD_ID + ".items", "Primal Winter");
        add(MOD_ID + ".subtitle.wind", "Wind Howls");

        addConfig("enableWeatherCommand", "Enable /weather Command");
        addConfig("enableSnowAccumulationDuringWorldgen", "Enable Snow Layering in World Generation");
        addConfig("enableSnowAccumulationDuringWeather", "Enable Snow Layering in Weather");
        addConfig("winterDimensions", "Winter Dimensions");
        addConfig("fogDensity", "Fog Density");
        addConfig("snowDensity", "Snow Density");
        addConfig("snowSounds", "Snow Sounds");
        addConfig("windSounds", "Wind Sounds");
        addConfig("fogColorDay", "Daytime Fog Color");
        addConfig("fogColorNight", "Nighttime Fog Color");

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
        addBlock(SNOWY_AZALEA_LEAVES, "Frosted Azalea Leaves");
        addBlock(SNOWY_FLOWERING_AZALEA_LEAVES, "Frosted Flowering Azalea Leaves");
        addBlock(SNOWY_VINE, "Frozen Vine");
        addBlock(SNOWY_SUGAR_CANE, "Frozen Sugar Cane");
        addBlock(SNOWY_CACTUS, "Frozen Cactus");
        addBlock(SNOWY_BAMBOO, "Frozen Bamboo");
        addBlock(SNOWY_LILY_PAD, "Frozen Lily Pad");
        addBlock(SNOWY_BROWN_MUSHROOM_BLOCK, "Frosted Brown Mushroom Block");
        addBlock(SNOWY_RED_MUSHROOM_BLOCK, "Frosted Red Mushroom Block");
        addBlock(SNOWY_MUSHROOM_STEM, "Frosted Mushroom Stem");

        Preconditions.checkArgument(untranslatedBlocks.isEmpty(), "Missing translations for %s block(s): %s",
            untranslatedBlocks.size(),
            untranslatedBlocks.stream()
                .map(BuiltInRegistries.BLOCK::getKey)
                .map(ResourceLocation::toString)
                .collect(Collectors.joining(", ")));
    }

    @Override
    public void addBlock(Supplier<? extends Block> key, String name)
    {
        untranslatedBlocks.remove(key.get());
        super.addBlock(key, name);
    }

    private void addConfig(String key, String name)
    {
        add(MOD_ID + ".config." + key, name);
    }
}
