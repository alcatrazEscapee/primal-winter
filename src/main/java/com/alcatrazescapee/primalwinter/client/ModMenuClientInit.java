/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.client;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class ModMenuClientInit implements ModMenuApi
{
    /*
    // todo: needs autoconfig update
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory()
    {
        return parent -> AutoConfig.getConfigScreen(ModConfig.ModConfigData.class, parent).get();
    }
     */
}
