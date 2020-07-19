/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter;

import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;

import com.alcatrazescapee.primalwinter.common.ModBlocks;
import com.alcatrazescapee.primalwinter.util.Helpers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public final class PrimalWinter implements ModInitializer
{
    public static final String MOD_ID = "primalwinter";

    @Override
    public void onInitialize()
    {
        ModBlocks.setup();
        Helpers.hackWinterBiomes();

        // todo: config?
        // Vanilla weather command... NOT ALLOWED
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.getRoot().getChildren().removeIf(node -> node.getName().equals("weather"));
            dispatcher.register(CommandManager.literal("weather").executes(source -> {
                source.getSource().sendFeedback(new LiteralText("Not even a command can overcome this storm... (This command is disabled by Primal Winter)") {}, false);
                return 0;
            }));
        });
    }
}
