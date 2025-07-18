package com.tkisor.llrcore.utils.register;

import com.tkisor.llrcore.LlrCore;

import com.gregtechceu.gtceu.GTCEu;

import java.util.HashMap;
import java.util.Map;

public class LLRBlockRegister {

    public static final Map<String, String> LANG = GTCEu.isDataGen() ? new HashMap() : null;

    public static void addLang(String var0, String var1) {
        if (LANG != null) {
            if (LANG.containsKey(var0)) {
                LlrCore.LOGGER.error("Repetitive Key: {}", var0);
                throw new IllegalStateException();
            } else {
                LANG.put(var0, var1);
            }
        }
    }
}
