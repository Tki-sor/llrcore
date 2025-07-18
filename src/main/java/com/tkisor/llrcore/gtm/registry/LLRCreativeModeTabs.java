package com.tkisor.llrcore.gtm.registry;

import com.tkisor.llrcore.LlrCore;

import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;

import net.minecraft.world.item.CreativeModeTab;

import com.tterrag.registrate.util.entry.RegistryEntry;

import static com.tkisor.llrcore.api.registries.LLRRegistration.LLR;

public class LLRCreativeModeTabs {

    public static RegistryEntry<CreativeModeTab> MACHINE = LLR.defaultCreativeTab(
            "machine", builder -> builder.displayItems(
                    new GTCreativeModeTabs.RegistrateDisplayItemsGenerator(
                            "machine", LLR))
                    .icon(LLRMultiblockMachines.sifter_factory::asStack).title(LLR.addLang("itemGroup", LlrCore.id("machine"), "LLR Machines")).build())
            .register();

    public static RegistryEntry<CreativeModeTab> ITEM = LLR.defaultCreativeTab(
            "item", builder -> builder.displayItems(
                    new GTCreativeModeTabs.RegistrateDisplayItemsGenerator(
                            "item", LLR))
                    .icon(LLRItems.TESTING_TERMINAL::asStack).title(LLR.addLang("itemGroup", LlrCore.id("item"), "LLR Items")).build())
            .register();

    // public static RegistryEntry<CreativeModeTab> BLOCK = REGISTRATE.defaultCreativeTab("block",
    // builder -> builder.displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("block",
    // REGISTRATE))
    // .icon(LLRBlocks.COIL_INFINITY::asStack)
    // .title(REGISTRATE.addLang("itemGroup", LlrCore.id("block"), "LLR Blocks"))
    // .build())
    // .register();

    public static void init() {}
}
