package com.tkisor.llrcore.api.registries;

import com.tkisor.llrcore.LlrCore;

import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

public class LLRRegistration extends GTRegistrate {

    public static final LLRRegistration LLR = new LLRRegistration(LlrCore.MOD_ID);

    protected LLRRegistration(String modId) {
        super(modId);
    }
}
