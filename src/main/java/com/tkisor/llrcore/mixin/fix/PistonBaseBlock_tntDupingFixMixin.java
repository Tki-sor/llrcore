package com.tkisor.llrcore.mixin.fix;

import com.tkisor.llrcore.config.LlrGameRule;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(PistonBaseBlock.class)
public class PistonBaseBlock_tntDupingFixMixin {

    @Inject(
            method = "moveBlocks",
            slice = @Slice(
                           from = @At(
                                      value = "INVOKE",
                                      target = "Lnet/minecraft/world/level/block/state/BlockState;hasBlockEntity()Z")),
            at = @At(
                     value = "INVOKE",
                     target = "Ljava/util/List;size()I",
                     shift = At.Shift.AFTER,
                     ordinal = 0))
    private void setAllToBeMovedBlockToAirFirst(
                                                Level world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, @Local Map<BlockPos, BlockState> map, @Local(ordinal = 0) List<BlockPos> list, // pistonHandler.getMovedBlocks()
                                                @Local(ordinal = 1) List<BlockState> list2 // states of list
    ) {
        if (!world.getGameRules().getBoolean(LlrGameRule.llr_tntAndRailDupingFix)) return;

        // vanilla iterating order
        for (int l = list.size() - 1; l >= 0; --l) {
            BlockPos toBeMovedBlockPos = list.get(l);
            BlockState toBeMovedBlockState = world.getBlockState(toBeMovedBlockPos);
            world.setBlock(toBeMovedBlockPos, Blocks.AIR.defaultBlockState(), 2 | 4 | 16 | 64);

            list2.set(l, toBeMovedBlockState);
            map.put(toBeMovedBlockPos, toBeMovedBlockState);
        }
    }

    /**
     * Just to make sure blockStates array contains the correct values But ..., when reading states
     * from it, mojang itself inverts the order and reads the wrong state releative to the blockpos
     * When assigning: blockStates = (list concat with list3 in order).map(world::getBlockState) When
     * reading: match list3[list3.size()-1] with blockStates[0] match list3[list3.size()-2] with
     * blockStates[1] ... The block pos matches wrongly with block state, so mojang uses the wrong
     * block as the source block to emit block updates :thonk: EDITED in 1.16.4: mojang has fixed it
     * now
     *
     * <p>
     * Whatever, just make it behave like vanilla
     */
    @Inject(
            method = "moveBlocks",
            slice = @Slice(
                           from = @At(
                                      value = "FIELD",
                                      target = "Lnet/minecraft/world/level/block/piston/PistonBaseBlock;isSticky:Z")),
            at = @At(value = "INVOKE", target = "Ljava/util/Map;keySet()Ljava/util/Set;", ordinal = 0))
    private void makeSureStatesInBlockStatesIsCorrect(
                                                      Level world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) List<BlockPos> list, // pistonHandler.getMovedBlocks()
                                                      @Local(ordinal = 1) List<BlockState> list2, // states of list
                                                      @Local(ordinal = 2) List<BlockPos> list3, // pistonHandler.getBrokenBlocks()
                                                      @Local BlockState[] blockStates, @Local(ordinal = 0) int j) {
        if (!world.getGameRules().getBoolean(LlrGameRule.llr_tntAndRailDupingFix)) return;

        int j2 = list3.size();
        for (int l2 = list.size() - 1; l2 >= 0; --l2) {
            blockStates[j2++] = list2.get(l2);
        }
    }
}
