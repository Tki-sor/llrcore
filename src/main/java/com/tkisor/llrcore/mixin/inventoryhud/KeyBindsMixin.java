package com.tkisor.llrcore.mixin.inventoryhud;

import dlovin.inventoryhud.keybinds.KeyBinds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinds.class)
public class KeyBindsMixin {

    @Inject(method = "initialize", at = @At("HEAD"), remap = false, cancellable = true)
    private static void initialize(CallbackInfo ci) {
        ci.cancel();
    }
}
