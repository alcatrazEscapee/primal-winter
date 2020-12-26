/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */


package com.alcatrazescapee.primalwinter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class Config
{
    public static final Common COMMON = register(ModConfig.Type.COMMON, Common::new);
    public static final Client CLIENT = register(ModConfig.Type.CLIENT, Client::new);
    public static final Server SERVER = register(ModConfig.Type.SERVER, Server::new);

    static void init() {}

    private static <T> T register(ModConfig.Type type, Function<ForgeConfigSpec.Builder, T> factory)
    {
        Pair<T, ForgeConfigSpec> configPair = new ForgeConfigSpec.Builder().configure(factory);
        ModLoadingContext.get().registerConfig(type, configPair.getRight());
        return configPair.getLeft();
    }

    public static final class Client
    {
        public final ForgeConfigSpec.DoubleValue fogDensity;
        public final ForgeConfigSpec.IntValue snowDensity;
        public final ForgeConfigSpec.BooleanValue snowSounds;
        public final ForgeConfigSpec.BooleanValue windSounds;
        public final ForgeConfigSpec.BooleanValue weatherRenderChanges;
        public final ForgeConfigSpec.BooleanValue skyRenderChanges;
        public final ForgeConfigSpec.IntValue fogColorDay;
        public final ForgeConfigSpec.IntValue fogColorNight;

        Client(ForgeConfigSpec.Builder builder)
        {
            fogDensity = builder.comment("How dense the fog effect during a snowstorm is.").defineInRange("fogDensity", 0.1, 0, 1);
            snowDensity = builder.comment("How visually dense the snow weather effect is. Normally, vanilla sets this to 5 with fast graphics, and 10 with fancy graphics.").defineInRange("snowDensity", 15, 1, 15);
            snowSounds = builder.comment("Enable snow (actually rain) weather sounds.").define("snowSounds", true);
            windSounds = builder.comment("Enable wind / snow storm weather sounds.").define("windSounds", true);

            fogColorDay = builder.comment("This is the fog color during the day. This is a hex color code, with 8 bits each for red, green, blue.").defineInRange("fogColorDay", 0xbfbfd8, 0, 0xFFFFFF);
            fogColorNight = builder.comment("This is the fog color during the night. This is a hex color code, with 8 bits each for red, green, blue.").defineInRange("fogColorNight", 0x0c0c19, 0, 0xFFFFFF);

            weatherRenderChanges = builder.comment("Changes the weather renderer to one which renders faster, denser snow. Note: this requires a world reload to take effect.").define("weatherRenderChanges", true);
            skyRenderChanges = builder.comment("Changes the sky renderer to one which does not render sunrise or sunset effects during a snowstorm. Note: this requires a world reload to take effect.").define("skyRenderChanges", true);
        }
    }

    public static final class Common
    {
        public final ForgeConfigSpec.BooleanValue disableWeatherCommand;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> nonWinterBiomes;

        Common(ForgeConfigSpec.Builder builder)
        {
            disableWeatherCommand = builder.comment("Should the vanilla /weather be disabled? Any changes require a world restart to take effect.").worldRestart().define("disableWeatherCommand", true);

            nonWinterBiomes = builder.comment("A list of biome IDs that will not be forcibly converted to frozen wastelands. Any changes requires a MC restart to take effect.").worldRestart().defineList("nonWinterBiomes", this::getDefaultNonWinterBiomes, e -> e instanceof String);
        }

        private List<? extends String> getDefaultNonWinterBiomes()
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
    }

    public static final class Server
    {
        public final ForgeConfigSpec.BooleanValue enableSnowAccumulation;

        Server(ForgeConfigSpec.Builder builder)
        {
            enableSnowAccumulation = builder.comment("Should snow accumulate during snow storms?").define("enableSnowAccumulation", true);
        }
    }
}