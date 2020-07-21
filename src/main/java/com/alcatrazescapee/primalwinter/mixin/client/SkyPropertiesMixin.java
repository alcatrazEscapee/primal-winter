/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.client;

import net.minecraft.client.render.SkyProperties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SkyProperties.class)
public class SkyPropertiesMixin
{
    /**
     * Ensures that the sunrise / sunset effect is never rendered as it has a weird interaction with the fog color
     */
    @SuppressWarnings("ConstantConditions")
    @ModifyVariable(method = "getSkyColor", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    public float getAngle(float angle)
    {
        if ((Object) this instanceof SkyProperties.Overworld)
        {
            return 0f;
        }
        return angle;
    }
}
