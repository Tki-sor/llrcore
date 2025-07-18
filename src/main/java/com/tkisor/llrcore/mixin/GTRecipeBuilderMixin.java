package com.tkisor.llrcore.mixin;

import com.tkisor.llrcore.gtm.config.GTDifficultyEnum;
import com.tkisor.llrcore.gtm.config.LLRCoreConfig;

import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GTRecipeBuilder.class, remap = false)
public abstract class GTRecipeBuilderMixin {

    @Shadow
    @Mutable
    public int duration;

    @Unique
    private void modifyDuration() {
        LLRCoreConfig config = LLRCoreConfig.get();
        GTDifficultyEnum gtDifficulty = config.getGTDifficulty();
        double multiplier = gtDifficulty.getDifficultyMultiplier();
        this.duration = (int) (100 * multiplier);
    }

    @Inject(method = "duration", at = @At("RETURN"), cancellable = true)
    public void duration(int duration, CallbackInfoReturnable<GTRecipeBuilder> cir) {
        LLRCoreConfig config = LLRCoreConfig.get();
        GTDifficultyEnum gtDifficulty = config.getGTDifficulty();
        double multiplier = gtDifficulty.getDifficultyMultiplier();

        GTRecipeBuilder value = cir.getReturnValue();
        value.duration = (int) (duration * multiplier);
        cir.setReturnValue(value);
    }
}
