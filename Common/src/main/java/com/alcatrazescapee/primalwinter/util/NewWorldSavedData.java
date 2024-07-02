package com.alcatrazescapee.primalwinter.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import com.alcatrazescapee.primalwinter.PrimalWinter;

public final class NewWorldSavedData extends SavedData
{
    @SuppressWarnings("ConstantConditions") // In NF, the method is patched to allow null, in Fabric, they're a mixin
    public static void onlyForNewWorlds(ServerLevel level, Runnable action)
    {
        level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(
            () -> {
                action.run();
                return new NewWorldSavedData();
            }, (tag, provider) -> new NewWorldSavedData(), null), PrimalWinter.MOD_ID);
    }

    @Override
    public boolean isDirty()
    {
        return true; // Assume it is always dirty, so it is always saved, so we don't run this on existing worlds
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries)
    {
        return tag;
    }
}
