package com.tkisor.llrcore.gtm.registry;

import com.tkisor.llrcore.api.registries.LLRRegistration;
import com.tkisor.llrcore.gtm.common.machine.multiblock.part.NetworkStoreHatch;
import com.tkisor.llrcore.gtm.common.machine.multiblock.part.StoreCellHatch;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.tkisor.llrcore.utils.register.LLRMachineUtils.registerSimpleGenerator;

public class LLRMachines {

    public static final Int2IntFunction genericGeneratorTankSizeFunction = (tier) -> Math.min(4 * (1 << tier - 1), 16) * 1000;

    public static final MachineDefinition NETWORK_STORE_HATCH = LLRRegistration.LLR.machine("network_store_hatch", NetworkStoreHatch::new).abilities(LLRPartAbility.NETWORK_STORE).rotationState(RotationState.ALL).simpleModel(GTCEu.id("block/machine/part/maintenance.full_auto")).register();

    public static final MachineDefinition StoreCellHatch = LLRRegistration.LLR.machine("store_cell_hatch", StoreCellHatch::new).abilities(LLRPartAbility.STORE_CELL_HATCH).rotationState(RotationState.ALL).simpleModel(GTCEu.id("block/machine/part/maintenance.full_auto")).register();

    public static final MachineDefinition[] SemiFluidGenerator = registerSimpleGenerator(
            "semi_fluid_generator",
            "半流质发电机",
            LLRRecipeTypes.semi_fluid_generator,
            t -> 6400,
            LV, MV, HV);

    public static final MachineDefinition[] ThermalGenerator = registerSimpleGenerator(
            "thermal_generator",
            "热力发电机",
            LLRRecipeTypes.thermal_generator,
            tier -> (tier + 1) * 1000, LV, MV, HV);

    public static void init() {
    }
}
