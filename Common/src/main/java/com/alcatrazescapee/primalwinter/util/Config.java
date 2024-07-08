package com.alcatrazescapee.primalwinter.util;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import com.alcatrazescapee.primalwinter.client.ReloadableLevelRenderer;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.CheckReturnValue;
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

    // Server Only
    public final BoolValue enableWeatherCommand;

    public final BoolValue enableSnowAccumulationDuringWorldgen;
    public final BoolValue enableSnowAccumulationDuringWeather;

    // Server Only - Synced
    private final TypeValue<Set<ResourceKey<Level>>> winterDimensions;

    // Server Synced
    private Set<ResourceKey<Biome>> winterBiomesView = Set.of();
    private Set<ResourceKey<Level>> winterDimensionsView = Set.of();

    // Client
    public final FloatValue fogDensity;
    public final IntValue snowDensity;
    public final BoolValue windSounds;
    public final BoolValue snowSounds;

    public final IntValue fogColorDay;
    public final IntValue fogColorNight;

    private final Spec spec;

    Config()
    {
        final SpecBuilder builder = Spec.builder();

        builder
            .comment(
                "This is the config file for the Primal Winter mod",
                "In order to reload these settings in-game, you must run /primalwinterReloadConfig"
            )
            .push("general");

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

        winterDimensions = builder
            .comment(
                "A list of dimensions that will be modified by Primal Winter.",
                "Dimensions on this list, and all biomes that they may generate, **will** be modified. Note that this means if",
                "a biome is generated in a winter dimension, and a non-winter dimension **that biome will be broken**."
            )
            .define("winterDimensions", Set.of(
                Level.OVERWORLD
            ), Type.STRING_LIST.map(
                list -> list.stream()
                    .map(name -> ResourceKey.create(Registries.DIMENSION, ParseError.require(() -> ResourceLocation.parse(name))))
                    .collect(Collectors.toSet()),
                set -> set.stream()
                    .map(rl -> rl.location().toString())
                    .toList(),
                TypeValue::new
            ));

        builder.swap("client");

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

        spec = builder
            .pop()
            .build();
    }

    public void load()
    {
        LOGGER.info("Loading Config");
        EpsilonUtil.parse(spec, Path.of(XPlatform.INSTANCE.configDir().toString(), PrimalWinter.MOD_ID + ".toml"), LOGGER::warn);
    }

    public void loadWinterBiomes(MinecraftServer server)
    {
        winterDimensionsView = winterDimensions.get();
        winterBiomesView = server.registryAccess()
            .registryOrThrow(Registries.LEVEL_STEM)
            .entrySet()
            .stream()
            .filter(e -> isWinterDimension(ResourceKey.create(Registries.DIMENSION, e.getKey().location())))
            .flatMap(e -> e.getValue()
                .generator()
                .getBiomeSource()
                .possibleBiomes()
                .stream())
            .flatMap(holder -> holder.unwrapKey().stream())
            .collect(Collectors.toSet());
        LOGGER.info("Loaded winter dimensions={}, biomes={}", winterDimensionsView.size(), winterBiomesView.size());
    }

    @CheckReturnValue
    public ConfigPacket createSyncPacket()
    {
        return new ConfigPacket(winterDimensions.get());
    }

    public void onSync(ConfigPacket packet)
    {
        winterDimensionsView = packet.winterDimensions();
        ((ReloadableLevelRenderer) Minecraft.getInstance().levelRenderer).primalWinter$reload();
    }

    /**
     * @return {@code true} if this dimensions is a winter dimension.
     */
    public boolean isWinterDimension(ResourceKey<Level> dimension)
    {
        return winterDimensionsView.contains(dimension);
    }

    /**
     * @return {@code true} if this is a winter biome, when queried from server. <strong>Not valid on client!</strong>
     */
    public boolean isWinterBiome(ResourceKey<Biome> biome)
    {
        assert winterDimensionsView.isEmpty() || !winterBiomesView.isEmpty(); // Config loaded check, allowing for no enabled winter dimensions
        return winterBiomesView.contains(biome);
    }
}
