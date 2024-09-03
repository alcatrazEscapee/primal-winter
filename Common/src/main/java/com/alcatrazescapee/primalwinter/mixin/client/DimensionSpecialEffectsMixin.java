package com.alcatrazescapee.primalwinter.mixin.client;

import com.alcatrazescapee.primalwinter.platform.XPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionSpecialEffects.class)
public abstract class DimensionSpecialEffectsMixin
{
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getSunriseColor", at = @At("RETURN"), cancellable = true)
    private void noSunriseColor(float skyAngle, float tickDelta, CallbackInfoReturnable<float[]> cir)
    {
        final float[] original = cir.getReturnValue();
        final Level level = Minecraft.getInstance().level;
        if (original != null && level != null && XPlatform.INSTANCE.config().isWinterDimension(level.dimension()))
        {
            cir.setReturnValue(null);
        }
    }
}
