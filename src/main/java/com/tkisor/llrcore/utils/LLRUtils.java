package com.tkisor.llrcore.utils;

import com.tkisor.llrcore.gtm.registry.LLRRecipeTypes;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

public class LLRUtils {

    public static int getGeneratorAmperage(int var0) {
        return var0 > 0 && var0 < 4 ? 2 : 1;
    }

    public static int getGeneratorEfficiency(GTRecipeType var0, int var1) {
        int var2 = 105 - 5 * var1;
        if (var0 == GTRecipeTypes.STEAM_TURBINE_FUELS) {
            var2 = 135 - 35 * var1;
        }

        if (var0 == LLRRecipeTypes.thermal_generator) {
            var2 = 100 - 25 * var1;
        }

        return var2;
    }
}
