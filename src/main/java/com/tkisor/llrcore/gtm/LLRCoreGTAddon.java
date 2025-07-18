package com.tkisor.llrcore.gtm;

import com.tkisor.llrcore.LlrCore;
import com.tkisor.llrcore.api.registries.LLRRegistration;
import com.tkisor.llrcore.gtm.registry.LLRElements;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

@GTAddon
public class LLRCoreGTAddon implements IGTAddon {

    @Override
    public GTRegistrate getRegistrate() {
        return LLRRegistration.LLR;
    }

    @Override
    public void initializeAddon() {}

    @Override
    public String addonModId() {
        return LlrCore.MOD_ID;
    }

    @Override
    public void registerElements() {
        LLRElements.init();
    }

    public static int qwq = 0;

    public static int getIntNext() {
        return qwq++;
    }

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {}
}
