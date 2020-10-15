/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */


package com.alcatrazescapee.primalwinter;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;

public final class Config
{
    public static final Common COMMON = register(ModConfig.Type.COMMON, Common::new);
    public static final Client CLIENT = register(ModConfig.Type.CLIENT, Client::new);

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
            snowSounds = builder.comment("Enable snow (actually rain) weather sounds.").define("snowSounds", false);
            windSounds = builder.comment("Enable wind / snow storm weather sounds.").define("windSounds", false);

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

        Common(ForgeConfigSpec.Builder builder)
        {
            disableWeatherCommand = builder.comment("Should the vanilla /weather be disabled? Any changes require a world restart to take effect.").worldRestart().define("disableWeatherCommand", false);

            nonWinterBiomes = builder.comment("A list of biome IDs that will not be forcibly converted to frozen wastelands. Any changes requires a MC restart to take effect.").worldRestart().define("nonWinterBiomes", this::getDefaultNonWinterBiomes, this::validateNonWinterBiomes);
        }

        public Predicate<Biome> getNonWinterBiomesFilter()
        {
            List<Biome> biomes = nonWinterBiomes.get()
                .stream()
                .map(id -> ForgeRegistries.BIOMES.getValue(new ResourceLocation(id)))
                .collect(Collectors.toList());
            return biomeIn -> biomes.stream().noneMatch(biome -> biome == biomeIn);
        }

        private boolean validateNonWinterBiomes(Object e)
        {
            return e instanceof String && ForgeRegistries.BIOMES.containsKey(new ResourceLocation(e.toString()));
        }

        @SuppressWarnings("ConstantConditions")
        private List<? extends String> getDefaultNonWinterBiomes()
        {
            return Stream.of(
                ForgeRegistries.BIOMES.getKey(Biomes.NETHER).toString(),
                ForgeRegistries.BIOMES.getKey(Biomes.END_BARRENS).toString(),
                ForgeRegistries.BIOMES.getKey(Biomes.END_HIGHLANDS).toString(),
                ForgeRegistries.BIOMES.getKey(Biomes.END_MIDLANDS).toString(),
                ForgeRegistries.BIOMES.getKey(Biomes.THE_END).toString(),
                ForgeRegistries.BIOMES.getKey(Biomes.THE_VOID).toString()
            ).collect(Collectors.toList());
        }
    }
}
