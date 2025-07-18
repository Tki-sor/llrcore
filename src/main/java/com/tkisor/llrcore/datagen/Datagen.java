package com.tkisor.llrcore.datagen;

import com.tkisor.llrcore.api.registries.LLRRegistration;
import com.tkisor.llrcore.datagen.lang.LangHandler;
import com.tkisor.llrcore.datagen.lang.SimplifiedChineseLanguageProvider;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.tterrag.registrate.providers.ProviderType;

public class Datagen {

    public static void init() {
        if (GTCEu.isDataGen()) {
            // LLRRegistration.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS,
            // TagsHandler::initBlock);
            // LLRRegistration.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TagsHandler::initItem);
            LLRRegistration.LLR.addDataGenerator(ProviderType.LANG, LangHandler::enInit);
            LLRRegistration.LLR.addDataGenerator(
                    SimplifiedChineseLanguageProvider.LANG, LangHandler::cnInit);
        }
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {}
}
