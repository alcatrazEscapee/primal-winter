package com.alcatrazescapee.primalwinter.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.Logger;

import com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks;

public final class EventHandler
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        final boolean enable = Config.INSTANCE.enableWeatherCommand.get();
        LOGGER.info("Vanilla /weather enabled = {}", enable);
        if (!enable)
        {
            // Vanilla weather command... NOT ALLOWED
            dispatcher.getRoot().getChildren().removeIf(node -> node.getName().equals("weather"));
            dispatcher.register(Commands.literal("weather").executes(source -> {
                source.getSource().sendSuccess(new TextComponent("Not even a command can overcome this storm... (This command is disabled by Primal Winter)"), false);
                return 0;
            }));
        }
    }

    /**
     * During {@link ServerLevel#tickChunk(LevelChunk, int)}, places additional snow layers
     */
    public static void placeExtraSnow(ServerLevel level, ChunkAccess chunk)
    {
        if (Config.INSTANCE.enableSnowAccumulationDuringWeather.get() && level.random.nextInt(16) == 0)
        {
            final int blockX = chunk.getPos().getMinBlockX();
            final int blockZ = chunk.getPos().getMinBlockZ();
            final BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, level.getBlockRandomPos(blockX, 0, blockZ, 15));
            final BlockState state = level.getBlockState(pos);
            final Biome biome = level.getBiome(pos).value();
            if (level.isRaining() && biome.coldEnoughToSnow(pos) && level.getBrightness(LightLayer.BLOCK, pos) < 10)
            {
                if (state.getBlock() == Blocks.SNOW)
                {
                    // Stack snow layers
                    final int layers = state.getValue(BlockStateProperties.LAYERS);
                    if (layers < 5)
                    {
                        level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LAYERS, 1 + layers));
                    }

                    final BlockPos belowPos = pos.below();
                    final BlockState belowState = level.getBlockState(belowPos);
                    final Block replacementBlock = PrimalWinterBlocks.SNOWY_TERRAIN_BLOCKS.getOrDefault(belowState.getBlock(), () -> null).get();
                    if (replacementBlock != null)
                    {
                        level.setBlockAndUpdate(belowPos, replacementBlock.defaultBlockState());
                    }
                }
            }
        }
    }

    public static void setLevelToThunder(LevelAccessor maybeLevel)
    {
        if (maybeLevel instanceof ServerLevel level && Config.INSTANCE.isWinterDimension(level.dimension().location()))
        {
            NewWorldSavedData.onlyForNewWorlds(level, () -> {
                LOGGER.info("Modifying weather for world {}", level.dimension().location());
                level.setWeatherParameters(0, Integer.MAX_VALUE, true, true);  // Copied from WeatherCommand
                level.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, level.getServer());
            });
        }
    }
}
