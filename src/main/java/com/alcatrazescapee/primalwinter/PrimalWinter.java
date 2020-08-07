/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter;

import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.world.World;

import com.alcatrazescapee.primalwinter.common.ModBlocks;
import com.alcatrazescapee.primalwinter.gross.GrossBiomeHacks;
import com.alcatrazescapee.primalwinter.util.Helpers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public final class PrimalWinter implements ModInitializer
{
    public static final String MOD_ID = "primalwinter";

    @Override
    public void onInitialize()
    {
        ModConfig.init();
        ModBlocks.init();

        // Manually editing biomes... so gross
        if (ModConfig.INSTANCE.enableGrossBiomeHacks)
        {
            GrossBiomeHacks.modifyBiomeCodec();
        }

        // Vanilla weather command... NOT ALLOWED
        if (ModConfig.INSTANCE.enableVanillaWeatherCommand)
        {
            CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
                dispatcher.getRoot().getChildren().removeIf(node -> node.getName().equals("weather"));
                dispatcher.register(CommandManager.literal("weather").executes(source -> {
                    source.getSource().sendFeedback(new LiteralText("Not even a command can overcome this storm... (This command is disabled by Primal Winter)") {}, false);
                    return 0;
                }));
            });
        }

        ServerLifecycleEvents.SERVER_STARTED.register(server -> Helpers.setWeatherToEndlessWinter(server.getWorld(World.OVERWORLD)));
    }
}
