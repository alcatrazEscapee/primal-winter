package com.alcatrazescapee.primalwinter.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.alcatrazescapee.primalwinter.util.Config;

@Mixin(DimensionSpecialEffects.class)
public abstract class DimensionSpecialEffectsMixin
{
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getSunriseColor", at = @At("RETURN"), cancellable = true)
    private void noSunriseColor(float skyAngle, float tickDelta, CallbackInfoReturnable<float[]> cir)
    {
        final float[] original = cir.getReturnValue();
        final Level level = Minecraft.getInstance().level;
        if (original != null && Config.INSTANCE.weatherRenderChanges.get() && level != null)
        {
            final BlockPos pos = Minecraft.getInstance().gameRenderer.getMainCamera().getBlockPosition();
            final Holder<Biome> biome = level.getBiome(pos);
            if (biome.value().coldEnoughToSnow(pos) && ((Object) this) instanceof DimensionSpecialEffects.OverworldEffects)
            {
                cir.setReturnValue(null);
            }
        }
    }
}
