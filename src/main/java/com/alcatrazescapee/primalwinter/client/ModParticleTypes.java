/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.client;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

public final class ModParticleTypes
{
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = new DeferredRegister<>(ForgeRegistries.PARTICLE_TYPES, MOD_ID);

    public static final RegistryObject<BasicParticleType> SNOW = PARTICLE_TYPES.register("snow", () -> new BasicParticleType(false));
}
