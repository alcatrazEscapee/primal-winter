/*
 * Part of the Primal Winter mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.util;

import javax.annotation.Nullable;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class NoopStorage<T> implements Capability.IStorage<T>
{
    @Nullable
    @Override
    public INBT writeNBT(Capability<T> capability, T instance, Direction side)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt)
    {
        throw new UnsupportedOperationException();
    }
}
