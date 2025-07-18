package com.tkisor.llrcore.mixin.everyx_hotpot;

import net.minecraftforge.event.OnDatapackSyncEvent;

import com.github.argon4w.hotpot.events.HotpotCommonModEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HotpotCommonModEvents.class, remap = false)
public class EveryxHotpotMixin {

    @Inject(method = "onDataPackSync", at = @At("HEAD"), cancellable = true)
    private static void onDataPackSync(OnDatapackSyncEvent event, CallbackInfo ci) {
        if (event.getPlayer() == null) {
            ci.cancel();
        }
    }
}
