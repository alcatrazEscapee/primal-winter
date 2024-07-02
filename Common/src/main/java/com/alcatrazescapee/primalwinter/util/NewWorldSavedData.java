package com.alcatrazescapee.primalwinter.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import com.alcatrazescapee.primalwinter.PrimalWinter;

public final class NewWorldSavedData extends SavedData
{
    private static final String ID = PrimalWinter.MOD_ID;

    @SuppressWarnings("ConstantConditions") // In NF, the method is patched to allow null
    public static void onlyForNewWorlds(ServerLevel level, Runnable action)
    {
        level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(
            () -> {
                action.run();
                return new NewWorldSavedData();
            }, (tag, provider) -> new NewWorldSavedData(), null), ID);
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries)
    {
        tag.putBoolean("new_world", false);
        return tag;
    }
}
