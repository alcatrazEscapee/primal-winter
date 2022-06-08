/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class SnowParticle extends WaterDropParticle
{
    protected SnowParticle(ClientLevel level, double x, double y, double z)
    {
        super(level, x, y, z);
    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType>
    {
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz)
        {
            final WaterDropParticle particle = new SnowParticle(level, x, y, z);
            particle.pickSprite(sprite);
            return particle;
        }
    }
}