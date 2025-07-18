package com.tkisor.llrcore.mixin;

import com.tkisor.llrcore.gtm.common.data.LLRMachines;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.common.data.machines.GCYMMachines;

import net.minecraft.network.chat.Component;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.INPUT_ENERGY;
import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.api.pattern.Predicates.controller;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.CASING_LARGE_SCALE_ASSEMBLING;
import static com.gregtechceu.gtceu.common.data.GTBlocks.CASING_TEMPERED_GLASS;

@Mixin(value = GCYMMachines.class, remap = false)
public class GCYMMachinesMixin {

    @ModifyExpressionValue(
                           method = "<clinit>",
                           at = @At(
                                    value = "INVOKE",
                                    target = "Lcom/gregtechceu/gtceu/api/registry/registrate/MultiblockMachineBuilder;pattern(Ljava/util/function/Function;)Lcom/gregtechceu/gtceu/api/registry/registrate/MultiblockMachineBuilder;"))
    private static MultiblockMachineBuilder modifyRegisterCall(MultiblockMachineBuilder original) {
        if (!original.id.equals(GTCEu.id("large_assembler"))) return original;

        MachineBuilderAccessor original1 = (MachineBuilderAccessor) (original);
        // if (!original1.getName().equals("large_assembler")) return original;
        original1.getTooltips().set(1, Component.translatable("llrcore.machine.tweaker.large_assembler.tooltip.1"));
        var original2 = (MultiblockMachineBuilder) original1;
        original2.pattern(
                definition -> FactoryBlockPattern.start().aisle("XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX").aisle("XXXXXXXXX", "XAAAXAAAX", "XGGGXXXXX").aisle("XXXXXXXXX", "XGGGXXSXX", "XGGGX###X").where('S', controller(blocks(definition.get()))).where(
                        'X', blocks(CASING_LARGE_SCALE_ASSEMBLING.get()).setMinGlobalLimited(40).or(
                                Predicates.autoAbilities(
                                        definition.getRecipeTypes(), false, false, true, true, true, true))
                                .or(
                                        Predicates.abilities(INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(2))
                                .or(Predicates.autoAbilities(true, false, true)))
                        .where('G', Predicates.blocks(CASING_TEMPERED_GLASS.get())).where('A', Predicates.air()).where('#', Predicates.any()).build());
        original2.tooltipBuilder(LLRMachines.LLR_MODIFY);

        return original2;
    }
}
