package com.alcatrazescapee.primalwinter.platform;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.alcatrazescapee.primalwinter.util.ConfigPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.network.PacketDistributor;

public class ForgeConfig extends Config
{
    public final ModConfigSpec common;
    public final ModConfigSpec client;

    private final ModConfigSpec.ConfigValue<List<? extends String>> winterDimensions;

    private final ModConfigSpec.ConfigValue<String> fogColorDayValue;
    private final ModConfigSpec.ConfigValue<String> fogColorNightValue;

    private int fogColorDayCache = -1;
    private int fogColorNightCache = -1;

    ForgeConfig()
    {
        final ModConfigSpec.Builder common = new ModConfigSpec.Builder();
        final ModConfigSpec.Builder client = new ModConfigSpec.Builder();

        // Server Only
        enableWeatherCommand = common
            .comment("", " Should the vanilla /weather be disabled?")
            .translation(key("enableWeatherCommand"))
            .gameRestart()
            .define("enableWeatherCommand", false);

        enableSnowAccumulationDuringWorldgen = common
            .comment(
                "",
                " If true, snow will be layered higher than one layer during world generation.",
                "",
                " Note: due to snow layers being > 1 block tall, this tends to prevent most passive (and hostile) mob spawning on the surface, since there are no places to spawn."
            )
            .translation(key("enableSnowAccumulationDuringWorldgen"))
            .define("enableSnowAccumulationDuringWorldgen", false);
        enableSnowAccumulationDuringWeather = common
            .comment("", "If true, snow will be layered higher than one layer during weather (snow).")
            .translation(key("enableSnowAccumulationDuringWeather"))
            .define("enableSnowAccumulationDuringWeather", true);

        // Server -> Client Sync
        winterDimensions = common
            .comment(
                "",
                " A list of dimensions that will be modified by Primal Winter.",
                " Dimensions on this list, and all biomes that they may generate, **will** be modified. Note that this means if a biome is generated in a winter dimension, and a non-winter dimension **that biome will be broken**."
            )
            .translation(key("winterDimensions"))
            .defineListAllowEmpty(
                "winterDimensions",
                () -> List.of(Level.OVERWORLD.location().toString()),
                () -> Level.OVERWORLD.location().toString(),
                o -> o instanceof String s && ResourceLocation.tryParse(s) != null);

        // Client
        fogDensity = client
            .comment("", " How dense the fog effect during a snowstorm is.")
            .translation(key("fogDensity"))
            .defineInRange("fogDensity", 0.1f, 0f, 1f);
        snowDensity = client
            .comment("", " How visually dense the snow weather effect is. Normally, vanilla sets this to 5 with fast graphics, and 10 with fancy graphics.")
            .translation(key("snowDensity"))
            .defineInRange("snowDensity", 15, 1, 15);
        snowSounds = client
            .comment("", " Enable snow (actually rain) weather sounds.")
            .translation(key("snowSounds"))
            .define("snowSounds", true);
        windSounds = client
            .comment("", "Enable wind / snow storm weather sounds.")
            .translation(key("windSounds"))
            .define("windSounds", true);

        fogColorDay = () -> fogColorDayCache;
        fogColorDayValue = client
            .comment("", " This is the fog color during the day. It must be an RGB hex string.")
            .translation(key("fogColorDay"))
            .define("fogColorDay", "0xbfbfd8", this::isColor);
        fogColorNight = () -> fogColorNightCache;
        fogColorNightValue = client
            .comment("", " This is the fog color during the night. It must be an RGB hex string.")
            .translation(key("fogColorNight"))
            .define("fogColorNight", "0x0c0c19", this::isColor);

        this.common = common.build();
        this.client = client.build();
    }

    public void updateCaches()
    {
        fogColorDayCache = extractColor(fogColorDayValue);
        fogColorNightCache = extractColor(fogColorNightValue);
    }

    @Override
    protected Collection<ResourceKey<Level>> winterDimensions()
    {
        return winterDimensions.get()
            .stream()
            .map(ResourceLocation::tryParse)
            .filter(Objects::nonNull)
            .map(e -> ResourceKey.create(Registries.DIMENSION, e))
            .toList();
    }

    @Override
    protected void syncTo(ServerPlayer player, ConfigPacket packet)
    {
        PacketDistributor.sendToPlayer(player, packet);
    }

    private String key(String path)
    {
        return PrimalWinter.MOD_ID + ".config." + path;
    }

    private boolean isColor(Object o)
    {
        if (!(o instanceof String s)) return false;
        try { Integer.parseInt(s, 16); }
        catch (NumberFormatException e) { return false; }
        return true;
    }

    private int extractColor(ModConfigSpec.ConfigValue<String> value)
    {
        try { return Integer.parseInt(value.get()); }
        catch (NumberFormatException e) { return -1; }
    }
}
