package com.tkisor.llrcore.gtm.registry;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;

public class LLRRecipeModifiers {

    public static ModifierFunction accurateParallel(
                                                    MetaMachine machine, GTRecipe recipe, int parallel) {
        var maxParallel = ParallelLogic.getParallelAmount(machine, recipe, parallel);
        if (recipe.hasTick()) {
            return ModifierFunction.builder().parallels(maxParallel).inputModifier(ContentModifier.multiplier(maxParallel)).outputModifier(ContentModifier.multiplier(maxParallel)).eutMultiplier(maxParallel).build();
        } else {
            return ModifierFunction.builder().parallels(maxParallel).inputModifier(ContentModifier.multiplier(maxParallel)).outputModifier(ContentModifier.multiplier(maxParallel)).build();
        }
    }
}
