package com.tkisor.llrcore.mixin.fix;

import com.mojang.blaze3d.platform.InputConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InputConstants.class)
public class InputConstantsMixin {

    @Inject(method = "isKeyDown", at = @At("HEAD"), cancellable = true)
    private static void shutupglerror$shutUpGLError(
                                                    long window, int code, CallbackInfoReturnable<Boolean> cir) {
        if (code == -1 || code == 0) {
            cir.setReturnValue(false);
        }
    }
}
