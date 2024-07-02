package com.alcatrazescapee.primalwinter.mixin;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.repository.KnownPack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Required in order for primal winter to function, due to biome modifications from both NeoForge and Fabric not syncing modifications
 * @see <a href="https://github.com/neoforged/NeoForge/issues/1204">NeoForge#1024</a>
 * @see <a href="https://github.com/FabricMC/fabric/issues/3897">Fabric#3897</a>
 */
@Mixin(RegistrySynchronization.class)
public abstract class RegistrySynchronizationMixin
{
    @Shadow
    private static <T> void packRegistry(
        DynamicOps<Tag> ops,
        RegistryDataLoader.RegistryData<T> registryData,
        RegistryAccess registryAccess, Set<KnownPack> packs,
        BiConsumer<ResourceKey<? extends Registry<?>>, List<RegistrySynchronization.PackedRegistryEntry>> packetSender
    ) {}

    @SuppressWarnings({"RedundantCast", "rawtypes"}) // This is necessary, for some reason IDEA thinks the cast is redundant?
    @Inject(method = "packRegistry", at = @At("HEAD"), cancellable = true)
    private static <T> void ignoreKnownPacksForBiomeModifiers(
        DynamicOps<Tag> ops,
        RegistryDataLoader.RegistryData<T> registryData,
        RegistryAccess registryAccess, Set<KnownPack> packs,
        BiConsumer<ResourceKey<? extends Registry<?>>, List<RegistrySynchronization.PackedRegistryEntry>> packetSender,
        CallbackInfo ci)
    {
        // If we have a non-empty set of packs, and we are packing biomes, then redirect this to pack with an empty set
        // This has the effect of forcing all biomes to be present, since none will be found in the existing packs, which
        // means all biomes are synced. Since we modify pretty much all biomes in Primal Winter, this is fine, at least as
        // a hack to start with
        if (!packs.isEmpty() && registryData.key() == (ResourceKey) Registries.BIOME)
        {
            packRegistry(ops, registryData, registryAccess, Set.of(), packetSender);
            ci.cancel();
        }
    }
}
