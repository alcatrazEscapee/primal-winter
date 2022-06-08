package com.alcatrazescapee.primalwinter.util;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.world.level.biome.Biomes;

import com.alcatrazescapee.primalwinter.platform.XPlatform;

public abstract class Config
{
    public static final Config INSTANCE = XPlatform.INSTANCE.config();

    // Common
    public final BooleanValue enableWeatherCommand;

    public final BooleanValue enableSnowAccumulationDuringWorldgen;

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

        enableSnowAccumulationDuringWorldgen = build(Type.COMMON, "enableSnowAccumulationDuringWorldgen", true, " If true, snow will be layered higher than one layer during world generation.");

        nonWinterBiomes = build(Type.COMMON, "nonWinterBiomes", getDefaultNonWinterBiomes(), "A list of biome IDs that will not be forcibly converted to frozen wastelands. Any changes requires a MC restart to take effect.");
        nonWinterDimensions = build(Type.COMMON, "nonWinterDimensions", getDefaultNonWinterDimensions(), "A list of dimension IDs that will not have winter weather effects set.");

        invertNonWinterBiomes = build(Type.COMMON, "invertNonWinterBiomes", false, "If true, the 'nonWinterBiomes' config option will be interpreted as a list of winter biomes, and all others will be ignored.");
        invertNonWinterDimensions = build(Type.COMMON, "invertNonWinterDimensions", false, "If true, the 'nonWinterDimensions' config option will be interpreted as a list of winter dimensions, and all others will be ignored.");

        // Client
        fogDensity = build(Type.CLIENT, "fogDensity", 0.1, 0, 1, "How dense the fog effect during a snowstorm is.");
        snowDensity = build(Type.CLIENT, "snowDensity", 15d, 1d, 15d, "How visually dense the snow weather effect is. Normally, vanilla sets this to 5 with fast graphics, and 10 with fancy graphics.");
        snowSounds = build(Type.CLIENT, "snowSounds", true, "Enable snow (actually rain) weather sounds.");
        windSounds = build(Type.CLIENT, "windSounds", true, "Enable wind / snow storm weather sounds.");

        fogColorDay = build(Type.CLIENT, "fogColorDay", 0xbfbfd8, 0, 0xFFFFFF, "This is the fog color during the day. This is a hex color code, with 8 bits each for red, green, blue.");
        fogColorNight = build(Type.CLIENT, "fogColorNight", 0x0c0c19, 0, 0xFFFFFF, "This is the fog color during the night. This is a hex color code, with 8 bits each for red, green, blue.");

        weatherRenderChanges = build(Type.CLIENT, "weatherRenderChanges", true, "Changes the weather renderer to one which renders faster, denser snow. Note: this requires a world reload to take effect.");
        skyRenderChanges = build(Type.CLIENT, "skyRenderChanges", true, "Changes the sky renderer to one which does not render sunrise or sunset effects during a snowstorm. Note: this requires a world reload to take effect.");
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

    protected abstract BooleanValue build(Type configType, String name, boolean defaultValue, String comment);
    protected abstract DoubleValue build(Type configType, String name, double defaultValue, double minValue, double maxValue, String comment);
    protected abstract IntValue build(Type configType, String name, int defaultValue, int minValue, int maxValue, String comment);
    protected abstract ListValue<String> build(Type configType, String name, List<String> defaultValue, String comment);

    public interface BooleanValue extends Supplier<Boolean> {}
    public interface DoubleValue extends Supplier<Double> {}
    public interface IntValue extends Supplier<Integer> {}
    public interface ListValue<T> extends Supplier<List<T>> {}

    public enum Type
    {
        CLIENT,
        COMMON
    }
}
