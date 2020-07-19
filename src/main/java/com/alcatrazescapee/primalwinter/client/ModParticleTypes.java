/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.client;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

public final class ModParticleTypes
{
    public static final DefaultParticleType SNOW = Registry.register(Registry.PARTICLE_TYPE, new Identifier(MOD_ID, "snow"), new DefaultParticleType(false) {});
}
