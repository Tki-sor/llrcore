package com.tkisor.llrcore.gtm.event;

import com.tkisor.llrcore.LlrCore;
import com.tkisor.llrcore.gtm.registry.*;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.DimensionMarker;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LlrCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler {

    @SubscribeEvent
    public static void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        // LLRMachines.init();
        LLRItems.init();
        LLRBlocks.init();
        LLRMultiblockMachines.init();
        LLRMachines.init();
    }

    public static void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        LLRRecipeTypes.init();
    }

    public static void registerPart(GTCEuAPI.RegisterEvent<ResourceLocation, PartAbility> event) {
        LLRPartAbility.init();
    }

    public static void registerDimensionMarkers(GTCEuAPI.RegisterEvent<ResourceLocation, DimensionMarker> event) {
        // LLRDimensionMarkers.init();
    }

    @SubscribeEvent
    public static void registerMaterials(MaterialEvent event) {
        LLRMaterials.init();
    }
}
