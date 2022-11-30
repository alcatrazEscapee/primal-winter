package com.alcatrazescapee.primalwinter.client;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;

import com.alcatrazescapee.primalwinter.platform.RegistryHolder;
import com.alcatrazescapee.primalwinter.platform.RegistryInterface;
import com.alcatrazescapee.primalwinter.platform.XPlatform;
import com.alcatrazescapee.primalwinter.util.Helpers;

public final class PrimalWinterAmbience
{
    public static final RegistryInterface<ParticleType<?>> PARTICLE_TYPES = XPlatform.INSTANCE.registryInterface(Registry.PARTICLE_TYPE);
    public static final RegistryInterface<SoundEvent> SOUND_EVENTS = XPlatform.INSTANCE.registryInterface(Registry.SOUND_EVENT);

    public static final RegistryHolder<SimpleParticleType> SNOW = PARTICLE_TYPES.register("snow", () -> new SimpleParticleType(false) {});
    public static final RegistryHolder<SoundEvent> WIND = SOUND_EVENTS.register("wind", () -> new SoundEvent(Helpers.identifier("wind")));
}
