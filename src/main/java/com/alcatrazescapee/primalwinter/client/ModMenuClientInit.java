/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.client;

import net.minecraft.client.gui.screen.Screen;

import com.alcatrazescapee.primalwinter.ModConfig;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class ModMenuClientInit implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory()
    {
        return parent -> AutoConfig.getConfigScreen(ModConfig.ModConfigData.class, parent).get();
    }
}
