/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.client;


import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

public final class ModSoundEvents
{
    public static final SoundEvent WIND = register("wind");

    private static SoundEvent register(String id)
    {
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(new Identifier(MOD_ID, id)));
    }
}
