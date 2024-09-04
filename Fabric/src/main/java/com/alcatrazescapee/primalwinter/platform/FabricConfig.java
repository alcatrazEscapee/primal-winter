package com.alcatrazescapee.primalwinter.platform;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import com.alcatrazescapee.epsilon.EpsilonUtil;
import com.alcatrazescapee.epsilon.ParseError;
import com.alcatrazescapee.epsilon.Spec;
import com.alcatrazescapee.epsilon.SpecBuilder;
import com.alcatrazescapee.epsilon.Type;
import com.alcatrazescapee.epsilon.value.IntValue;
import com.alcatrazescapee.epsilon.value.TypeValue;
import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.alcatrazescapee.primalwinter.util.ConfigPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class FabricConfig extends Config
{
    private final TypeValue<Set<ResourceKey<Level>>> winterDimensions;
    private final Spec spec;

    FabricConfig()
    {
        final SpecBuilder builder = Spec.builder();

        builder
            .comment("This is the config file for the Primal Winter mod")
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
            .define("fogDensity", 0.1f, 0f, 1f)::get;
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
        EpsilonUtil.parse(spec, Path.of(FabricLoader.getInstance().getConfigDir().toString(), PrimalWinter.MOD_ID + ".toml"), LOGGER::warn);
    }

    @Override
    protected Collection<ResourceKey<Level>> winterDimensions()
    {
        return winterDimensions.get();
    }

    @Override
    protected void syncTo(ServerPlayer player, ConfigPacket packet)
    {
        ServerPlayNetworking.send(player, packet);
    }
}
