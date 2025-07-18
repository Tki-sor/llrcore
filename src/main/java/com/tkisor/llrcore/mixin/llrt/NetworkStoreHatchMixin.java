package com.tkisor.llrcore.mixin.llrt;

import com.tkisor.llrcore.gtm.common.machine.multiblock.part.NetworkStoreHatch;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = NetworkStoreHatch.class, remap = false)
public class NetworkStoreHatchMixin extends MetaMachine {

    public NetworkStoreHatchMixin(IMachineBlockEntity holder) {
        super(holder);
    }
}
