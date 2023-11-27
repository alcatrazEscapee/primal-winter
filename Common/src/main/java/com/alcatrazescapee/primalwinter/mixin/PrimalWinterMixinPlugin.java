package com.alcatrazescapee.primalwinter.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class PrimalWinterMixinPlugin implements IMixinConfigPlugin {
    private boolean optifineLoaded;

    @Override
    public void onLoad(String mixinPackage) {
        try {
            Class.forName("optifine.Installer", false, getClass().getClassLoader());
            this.optifineLoaded = true;
        } catch (ClassNotFoundException e) {
            this.optifineLoaded = false;
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (optifineLoaded) return !mixinClassName.equals("com.alcatrazescapee.primalwinter.mixin.client.LevelRendererMixin");
        return !mixinClassName.equals("com.alcatrazescapee.primalwinter.mixin.client.optifine.LevelRendererMixin");
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
