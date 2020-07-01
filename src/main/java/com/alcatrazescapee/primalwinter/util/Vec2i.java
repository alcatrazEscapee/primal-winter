/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.util;

public final class Vec2i
{
    private final int x, z;

    public Vec2i(int x, int z)
    {
        this.x = x;
        this.z = z;
    }

    public int getX()
    {
        return x;
    }

    public int getZ()
    {
        return z;
    }

    @Override
    public int hashCode()
    {
        return 31 * x + z;
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Vec2i vec2i = (Vec2i) other;
        return x == vec2i.x && z == vec2i.z;
    }
}
