package com.tkisor.llrcore.datagen.lang;

import com.tkisor.llrcore.utils.register.LLRBlockRegister;

import net.minecraftforge.common.data.LanguageProvider;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

public class LangHandler {

    private static final Map<String, CNEN> LANGS = new Object2ObjectOpenHashMap<>();

    private static void addCNEN(String key, CNEN CNEN) {
        if (LANGS.containsKey(key)) throw new IllegalArgumentException("Duplicate key: " + key);
        LANGS.put(key, CNEN);
    }

    public static void addCNEN(String key, String cn, String en) {
        addCNEN(key, new CNEN(cn, en));
    }

    private static void addCN(String key, String cn) {
        addCNEN(key, cn, null);
    }

    private static void init() {
        LLRBlockRegister.LANG.forEach((k, v) -> addCN("block.llrcore." + k, v));
    }

    public static void enInit(LanguageProvider provider) {
        init();
        LANGS.forEach(
                (k, v) -> {
                    if (v.en() == null) return;
                    provider.add(k, v.en());
                });
    }

    public static void cnInit(SimplifiedChineseLanguageProvider provider) {
        LANGS.forEach(
                (k, v) -> {
                    if (v.cn() == null) return;
                    provider.add(k, v.cn());
                });
    }
}
