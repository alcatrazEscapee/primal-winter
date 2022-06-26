package com.alcatrazescapee.primalwinter.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import com.alcatrazescapee.primalwinter.PrimalWinter;

public final class NewWorldSavedData extends SavedData
{
    private static final String ID = PrimalWinter.MOD_ID;

    public static void onlyForNewWorlds(ServerLevel level, Runnable action)
    {
        level.getDataStorage().computeIfAbsent(tag -> new NewWorldSavedData(), () -> {
            final NewWorldSavedData data = new NewWorldSavedData();
            data.setDirty();
            action.run();
            return data;
        }, ID);
    }

    @Override
    public CompoundTag save(CompoundTag tag)
    {
        tag.putBoolean("new_world", false);
        return tag;
    }
}
