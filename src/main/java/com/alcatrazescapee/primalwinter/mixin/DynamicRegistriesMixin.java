/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin;

import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;

import com.alcatrazescapee.primalwinter.gross.GrossBiomeHacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DynamicRegistries.class)
public abstract class DynamicRegistriesMixin
{
    /**
     * Modify biomes after the dynamic registry manager has been created
     */
    @Inject(method = "create", at = @At(value = "RETURN"), cancellable = true)
    private static void create(CallbackInfoReturnable<DynamicRegistries.Impl> cir)
    {
        GrossBiomeHacks.modifyBiomes(cir.getReturnValue().get(Registry.BIOME_KEY));
    }
}
