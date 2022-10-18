package com.alcatrazescapee.primalwinter.util;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.alcatrazescapee.epsilon.EpsilonUtil;
import com.alcatrazescapee.epsilon.ParseError;
import com.alcatrazescapee.epsilon.Spec;
import com.alcatrazescapee.epsilon.SpecBuilder;
import com.alcatrazescapee.epsilon.Type;
import com.alcatrazescapee.epsilon.value.BoolValue;
import com.alcatrazescapee.epsilon.value.FloatValue;
import com.alcatrazescapee.epsilon.value.IntValue;
import com.alcatrazescapee.epsilon.value.TypeValue;
import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.alcatrazescapee.primalwinter.platform.XPlatform;

public enum Config
{
    INSTANCE;

    private static final Logger LOGGER = LogUtils.getLogger();

    // Common
    public final BoolValue enableWeatherCommand;

    public final BoolValue enableSnowAccumulationDuringWorldgen;
    public final BoolValue enableSnowAccumulationDuringWeather;

    public final TypeValue<List<ResourceLocation>> nonWinterBiomes;
    public final TypeValue<List<ResourceKey<Level>>> nonWinterDimensions;

    public final BoolValue invertNonWinterBiomes;
    public final BoolValue invertNonWinterDimensions;

    // Client
    public final FloatValue fogDensity;
    public final IntValue snowDensity;
    public final BoolValue windSounds;
    public final BoolValue snowSounds;

    public final IntValue fogColorDay;
    public final IntValue fogColorNight;

    public final BoolValue weatherRenderChanges;
    public final BoolValue skyRenderChanges;

    private final Spec spec;

    Config()
    {
        final SpecBuilder builder = Spec.builder();

        builder.push("general");

        // Common
        enableWeatherCommand = builder
            .comment("Should the vanilla /weather be disabled? Any changes require a world restart to take effect.")
            .define("enableWeatherCommand", false);

        enableSnowAccumulationDuringWorldgen = builder
            .comment(
                "If true, snow will be layered higher than one layer during world generation.",
                "Note: due to snow layers being > 1 block tall, this tends to prevent most passive (and hostile) mob spawning on the surface, since there are no places to spawn."
            )
            .define("enableSnowAccumulationDuringWorldgen", false);
        enableSnowAccumulationDuringWeather = builder
            .comment("If true, snow will be layered higher than one layer during weather (snow).")
            .define("enableSnowAccumulationDuringWeather", true);

        nonWinterBiomes = builder
            .comment("A list of biome IDs that will not be forcibly converted to frozen wastelands. Any changes requires a MC restart to take effect.")
            .define("nonWinterBiomes", Stream.of(
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
            ).map(ResourceKey::location).toList(), Type.STRING_LIST.map(
                list -> list.stream().map(name -> ParseError.require(() -> new ResourceLocation(name))).toList(),
                list -> list.stream().map(ResourceLocation::toString).toList(),
                TypeValue::new
            ));
        nonWinterDimensions = builder
            .comment("A list of dimension IDs that will not have winter weather effects set.")
            .define("nonWinterDimensions", List.of(
                Level.NETHER,
                Level.END
            ), Type.STRING_LIST.map(
                list -> list.stream().map(name -> ResourceKey.create(Registry.DIMENSION_REGISTRY, ParseError.require(() -> new ResourceLocation(name)))).toList(),
                list -> list.stream().map(rl -> rl.location().toString()).toList(),
                TypeValue::new
            ));

        invertNonWinterBiomes = builder
            .comment("If true, the 'nonWinterBiomes' config option will be interpreted as a list of winter biomes, and all others will be ignored.")
            .define("invertNonWinterBiomes", false);
        invertNonWinterDimensions = builder
            .comment("If true, the 'nonWinterDimensions' config option will be interpreted as a list of winter dimensions, and all others will be ignored.")
            .define("invertNonWinterDimensions", false);

        builder.swap("client");

        // Client
        fogDensity = builder
            .comment("How dense the fog effect during a snowstorm is.")
            .define("fogDensity", 0.1f, 0f, 1f);
        snowDensity = builder
            .comment("How visually dense the snow weather effect is. Normally, vanilla sets this to 5 with fast graphics, and 10 with fancy graphics.")
            .define("snowDensity", 15, 1, 15);
        snowSounds = builder
            .comment("Enable snow (actually rain) weather sounds.")
            .define("snowSounds", true);
        windSounds = builder
            .comment("Enable wind / snow storm weather sounds.")
            .define("windSounds", true);

        fogColorDay = builder
            .comment("This is the fog color during the day. It must be an RGB hex string.")
            .define("fogColorDay", 0xbfbfd8, Type.STRING.map(
                str -> ParseError.require(() -> Integer.parseInt(str, 16)),
                value -> String.format("%06x", value),
                IntValue::new
            ));
        fogColorNight = builder
            .comment("This is the fog color during the night. It must be an RGB hex string.")
            .define("fogColorNight", 0x0c0c19, Type.STRING.map(
                str -> ParseError.require(() -> Integer.parseInt(str, 16)),
                value -> String.format("%06x", value),
                IntValue::new
            ));

        weatherRenderChanges = builder
            .comment("Changes the weather renderer to one which renders faster, denser snow.")
            .define("weatherRenderChanges", true);
        skyRenderChanges = builder
            .comment("Changes the sky renderer to one which does not render sunrise or sunset effects during a snowstorm.")
            .define("skyRenderChanges", true);

        spec = builder
            .pop()
            .build();
    }

    public void load()
    {
        LOGGER.info("Loading Primal Winter Config");
        EpsilonUtil.parse(spec, Path.of(XPlatform.INSTANCE.configDir().toString(), PrimalWinter.MOD_ID + ".toml"), LOGGER::warn);
    }

    public boolean isWinterDimension(ResourceKey<Level> dimension)
    {
        final Stream<ResourceKey<Level>> stream = INSTANCE.nonWinterDimensions.get().stream();
        return INSTANCE.invertNonWinterDimensions.getAsBoolean() ? stream.anyMatch(dimension::equals) : stream.noneMatch(dimension::equals);
    }

    public boolean isWinterBiome(@Nullable ResourceLocation name)
    {
        if (name != null)
        {
            final Stream<ResourceLocation> stream = INSTANCE.nonWinterBiomes.get().stream();
            return INSTANCE.invertNonWinterBiomes.getAsBoolean() ? stream.anyMatch(name::equals) : stream.noneMatch(name::equals);
        }
        return false;
    }
}
