package com.tkisor.llrcore.mixin.fix;

import com.tkisor.llrcore.config.LlrGameRule;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 修复作用存疑 */
@Mixin(BaseRailBlock.class)
public class BaseRailBlock_railDupingFixMixin {

    @Inject(
            method = "neighborChanged",
            at = @At(
                     value = "INVOKE",
                     target = "Lnet/minecraft/world/level/block/BaseRailBlock;updateState(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;)V"),
            require = 1,
            cancellable = true)
    private void checkIfRailStillExists(
                                        CallbackInfo ci, @Local(argsOnly = true) Level world, @Local(argsOnly = true, ordinal = 0) BlockPos pos) {
        if (!world.getGameRules().getBoolean(LlrGameRule.llr_tntAndRailDupingFix)) return;
        if (!(world.getBlockState(pos).getBlock() instanceof BaseRailBlock)) {
            ci.cancel();
        }
    }
}
