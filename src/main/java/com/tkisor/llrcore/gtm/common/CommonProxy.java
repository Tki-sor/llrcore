package com.tkisor.llrcore.gtm.common;

import com.tkisor.llrcore.api.registries.LLRRegistration;
import com.tkisor.llrcore.datagen.Datagen;
import com.tkisor.llrcore.gtm.registry.LLRCreativeModeTabs;

import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("removal")
public class CommonProxy {

    public CommonProxy() {
        init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::addMaterialFlag);
        modEventBus.addListener(Datagen::onGatherData);
    }

    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        LLRCreativeModeTabs.init();
        // LLRBlockEntities.init();
        LLRRegistration.LLR.registerRegistrate();

        // LLRRecipes.init(modEventBus);
        // LLRTemperatureModifierRegister.init();
        // LLRCoreDatagen.init();
    }

    public void addMaterialFlag(MaterialEvent event) {
        // GTMaterialAddon.init();
    }
}
