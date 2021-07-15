/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.util.ResourceLocation;
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

        private final ForgeConfigSpec.ConfigValue<List<? extends String>> nonWinterBiomes;
        private final ForgeConfigSpec.ConfigValue<List<? extends String>> nonWinterDimensions;

        private final ForgeConfigSpec.BooleanValue invertNonWinterBiomes;
        private final ForgeConfigSpec.BooleanValue invertNonWinterDimensions;

        Common(ForgeConfigSpec.Builder builder)
        {
            disableWeatherCommand = builder.comment("Should the vanilla /weather be disabled? Any changes require a world restart to take effect.").worldRestart().define("disableWeatherCommand", true);

            nonWinterBiomes = builder.comment("A list of biome IDs that will not be forcibly converted to frozen wastelands. Any changes requires a MC restart to take effect.").worldRestart().defineList("nonWinterBiomes", this::getDefaultNonWinterBiomes, e -> e instanceof String);
            nonWinterDimensions = builder.comment("A list of dimension IDs that will not have winter weather effects set.").worldRestart().defineList("nonWinterDimensions", this::getDefaultNonWinterDimensions, e -> e instanceof String);

            invertNonWinterBiomes = builder.comment("If true, the 'nonWinterBiomes' config option will be interpreted as a list of winter biomes, and all others will be ignored.").define("invertNonWinterBiomes", false);
            invertNonWinterDimensions = builder.comment("If true, the 'nonWinterDimensions' config option will be interpreted as a list of winter dimensions, and all others will be ignored.").define("invertNonWinterDimensions", false);
        }

        public boolean isWinterBiome(@Nullable ResourceLocation id)
        {
            if (id != null)
            {
                final String name = id.toString();
                return invertNonWinterBiomes.get() ? nonWinterBiomes.get().stream().anyMatch(name::equals) : nonWinterBiomes.get().stream().noneMatch(name::equals);
            }
            return false;
        }

        public boolean isWinterDimension(ResourceLocation id)
        {
            final String name = id.toString();
            return invertNonWinterDimensions.get() ? nonWinterDimensions.get().stream().anyMatch(name::equals) : nonWinterDimensions.get().stream().noneMatch(name::equals);
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

        private List<? extends String> getDefaultNonWinterDimensions()
        {
            return Stream.of("minecraft:the_nether", "minecraft:the_end").collect(Collectors.toList());
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