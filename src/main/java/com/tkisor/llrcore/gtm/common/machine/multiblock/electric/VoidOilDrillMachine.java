package com.tkisor.llrcore.gtm.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;

import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VoidOilDrillMachine extends WorkableElectricMultiblockMachine implements IRecipeLogicMachine {

    public HashMap<FluidStack[], Integer> outWeight = new HashMap<>();
    public int totalWeight = 0;
    GTRecipe recipe = null;
    private Random random = new Random();
    int itemSize;
    int xv;

    public VoidOilDrillMachine(IMachineBlockEntity holder, int itemSize, int xv) {
        super(holder);
        this.itemSize = itemSize;
        this.xv = xv;
    }

    public FluidStack[] getRandomItemByWeight() {
        // 如果没有物品或者总权重大于0
        if (outWeight.isEmpty() || totalWeight <= 0) {
            return null; // 返回 null 表示没有选中物品
        }

        int randomValue = random.nextInt(totalWeight); // 生成一个小于 totalWeight 的随机数
        int accumulatedWeight = 0;

        for (Map.Entry<FluidStack[], Integer> entry : outWeight.entrySet()) {
            accumulatedWeight += entry.getValue();
            if (randomValue < accumulatedWeight) {
                return entry.getKey(); // 返回选中的物品
            }
        }

        return null; // 如果没有选中（理论上不应该发生）
    }

    public void reset(GTRecipe recipe) {
        outWeight.clear();
        totalWeight = 0;

        if (recipe == null) return;
        recipe.outputs.get(FluidRecipeCapability.CAP).forEach(
                (content) -> {
                    if (content.getContent() instanceof FluidIngredient fluidIn) {
                        var item = fluidIn.getStacks();
                        var weight = fluidIn.getAmount();

                        outWeight.put(item, weight);
                    }
                });
        totalWeight = outWeight.values().stream().mapToInt(Integer::intValue).sum();
    }

    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe originalRecipe) {
        return recipe -> {
            if (originalRecipe == null) return null;

            // 1. 创建基础配方副本并应用超频
            GTRecipe modifiedRecipe = createBaseModifiedRecipe(machine, originalRecipe.copy());
            if (modifiedRecipe == null) return null;

            // 2. 处理虚空采矿机特有逻辑
            if (machine instanceof VoidOilDrillMachine vmachine) {

                handleVoidMinerSpecifics(vmachine, originalRecipe, modifiedRecipe);
            }

            return modifiedRecipe;
        };
    }

    // 基础配方预处理
    private static GTRecipe createBaseModifiedRecipe(MetaMachine machine, GTRecipe recipeCopy) {
        recipeCopy.outputs.clear(); // 清空原有输出
        return GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(getOverclockingLogic(machine)).applyModifier(machine, recipeCopy);
    }

    // 获取超频逻辑
    private static OverclockingLogic getOverclockingLogic(MetaMachine machine) {
        return (machine instanceof VoidOilDrillMachine vmachine && vmachine.xv == 3) ? OverclockingLogic.create(1, 1, false) : OverclockingLogic.NON_PERFECT_OVERCLOCK;
    }

    // 处理虚空采矿机特有逻辑
    private static void handleVoidMinerSpecifics(
                                                 VoidOilDrillMachine vmachine, GTRecipe originalRecipe, GTRecipe modifiedRecipe) {
        // 重置配置
        if (vmachine.recipe == null || vmachine.recipe != originalRecipe) {
            vmachine.reset(originalRecipe);
            vmachine.recipe = originalRecipe;
        }

        // 调整持续时间
        modifiedRecipe.duration = calculateDuration(originalRecipe.duration, vmachine.xv);

        // 添加输出物品
        FluidStack outputItem = generateVoidMinerOutput(vmachine);

        // 如果电压为EV+，则允许使用黑名单
        if (outputItem != null) {
            modifiedRecipe.outputs.put(
                    FluidRecipeCapability.CAP, List.of(new Content(FluidIngredient.of(outputItem), 1, 1, 0)));
        }
    }

    // 持续时间计算
    private static int calculateDuration(int baseDuration, int xvLevel) {
        return (baseDuration * 2) / ((xvLevel - 1) * 2);
    }

    // 生成输出物品
    private static FluidStack generateVoidMinerOutput(VoidOilDrillMachine vmachine) {
        if (vmachine.getRandomItemByWeight().length == 0) return null;

        FluidStack selectedIngredient = vmachine.getRandomItemByWeight()[0];
        selectedIngredient.setAmount(vmachine.itemSize);
        return selectedIngredient;
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        textList.add(textList.size(), Component.literal("总权重：").append(String.valueOf(totalWeight)));
    }
}
