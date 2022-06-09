package com.alcatrazescapee.primalwinter.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.Nullable;

import com.alcatrazescapee.primalwinter.platform.AbstractConfig;
import com.alcatrazescapee.primalwinter.platform.XPlatform;

public abstract class Config extends AbstractConfig
{
    public static final Config INSTANCE = XPlatform.INSTANCE.createConfig();

    // Common
    public final BooleanValue enableWeatherCommand;

    public final BooleanValue enableSnowAccumulationDuringWorldgen;
    public final BooleanValue enableSnowAccumulationDuringWeather;

    public final ListValue<String> nonWinterBiomes;
    public final ListValue<String> nonWinterDimensions;

    public final BooleanValue invertNonWinterBiomes;
    public final BooleanValue invertNonWinterDimensions;

    // Client
    public final DoubleValue fogDensity;
    public final DoubleValue snowDensity;

    public final BooleanValue snowSounds;
    public final BooleanValue windSounds;

    public final IntValue fogColorDay;
    public final IntValue fogColorNight;

    public final BooleanValue weatherRenderChanges;
    public final BooleanValue skyRenderChanges;

    protected Config()
    {
        // Common
        enableWeatherCommand = build(Type.COMMON, "enableWeatherCommand", false, "Should the vanilla /weather be disabled? Any changes require a world restart to take effect.");

        enableSnowAccumulationDuringWorldgen = build(Type.COMMON, "enableSnowAccumulationDuringWorldgen", false, " If true, snow will be layered higher than one layer during world generation. Note: due to snow layers being > 1 block tall, this tends to prevent most passive (and hostile) mob spawning on the surface, since there are no places to spawn.");
        enableSnowAccumulationDuringWeather = build(Type.COMMON, "enableSnowAccumulationDuringWeather", true, " If true, snow will be layered higher than one layer during weather (snow).");

        nonWinterBiomes = build(Type.COMMON, "nonWinterBiomes", getDefaultNonWinterBiomes(), "A list of biome IDs that will not be forcibly converted to frozen wastelands. Any changes requires a MC restart to take effect.");
        nonWinterDimensions = build(Type.COMMON, "nonWinterDimensions", getDefaultNonWinterDimensions(), "A list of dimension IDs that will not have winter weather effects set.");

        invertNonWinterBiomes = build(Type.COMMON, "invertNonWinterBiomes", false, "If true, the 'nonWinterBiomes' config option will be interpreted as a list of winter biomes, and all others will be ignored.");
        invertNonWinterDimensions = build(Type.COMMON, "invertNonWinterDimensions", false, "If true, the 'nonWinterDimensions' config option will be interpreted as a list of winter dimensions, and all others will be ignored.");

        // Client
        fogDensity = build(Type.CLIENT, "fogDensity", 0.1, 0, 1, "How dense the fog effect during a snowstorm is.");
        snowDensity = build(Type.CLIENT, "snowDensity", 15d, 1d, 15d, "How visually dense the snow weather effect is. Normally, vanilla sets this to 5 with fast graphics, and 10 with fancy graphics.");
        snowSounds = build(Type.CLIENT, "snowSounds", true, "Enable snow (actually rain) weather sounds.");
        windSounds = build(Type.CLIENT, "windSounds", true, "Enable wind / snow storm weather sounds.");

        fogColorDay = build(Type.CLIENT, "fogColorDay", 0xbfbfd8, 0, 0xFFFFFF, "This is the fog color during the day. Default = #BFBFD8");
        fogColorNight = build(Type.CLIENT, "fogColorNight", 0x0c0c19, 0, 0xFFFFFF, "This is the fog color during the night. Default = #0C0C19");

        weatherRenderChanges = build(Type.CLIENT, "weatherRenderChanges", true, "Changes the weather renderer to one which renders faster, denser snow. Note: this requires a world reload to take effect.");
        skyRenderChanges = build(Type.CLIENT, "skyRenderChanges", true, "Changes the sky renderer to one which does not render sunrise or sunset effects during a snowstorm. Note: this requires a world reload to take effect.");
    }

    public boolean isWinterDimension(ResourceLocation id)
    {
        final String name = id.toString();
        final Stream<? extends String> stream = INSTANCE.nonWinterDimensions.get().stream();
        return INSTANCE.invertNonWinterDimensions.get() ? stream.anyMatch(name::equals) : stream.noneMatch(name::equals);
    }

    public boolean isWinterBiome(@Nullable ResourceLocation id)
    {
        if (id != null)
        {
            final String name = id.toString();
            final Stream<? extends String> stream = INSTANCE.nonWinterBiomes.get().stream();
            return INSTANCE.invertNonWinterBiomes.get() ? stream.anyMatch(name::equals) : stream.noneMatch(name::equals);
        }
        return false;
    }

    private List<String> getDefaultNonWinterBiomes()
    {
        return Stream.of(
            Biomes.NETHER_WASTES,
            Biomes.CRIMSON_FOREST,
            Biomes.WARPED_FOREST,
            Biomes.BASALT_DELTAS,
            Biomes.SOUL_SAND_VALLEY,
            Biomes.END_BARRENS,
            Biomes.END_HIGHLANDS,
            Biomes.END_MIDLANDS,
            Biomes.THE_END,
            Biomes.THE_VOID
        ).map(key -> key.location().toString()).collect(Collectors.toList());
    }

    private List<String> getDefaultNonWinterDimensions()
    {
        return Stream.of("minecraft:the_nether", "minecraft:the_end").collect(Collectors.toList());
    }
}
