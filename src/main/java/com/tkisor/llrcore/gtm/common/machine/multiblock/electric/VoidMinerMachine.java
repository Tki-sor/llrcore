package com.tkisor.llrcore.gtm.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class VoidMinerMachine extends WorkableElectricMultiblockMachine implements IRecipeLogicMachine {

    public HashMap<Ingredient, Integer> outWeight = new HashMap<>();
    public int totalWeight = 0;
    GTRecipe recipe = null;
    private Random random = new Random();
    int itemSize = 1;
    int xv = 3;

    public VoidMinerMachine(IMachineBlockEntity holder, int itemSize, int xv) {
        super(holder);
        this.itemSize = itemSize;
        this.xv = xv;
    }

    public Ingredient getRandomItemByWeight() {
        // 如果没有物品或者总权重大于0
        if (outWeight.isEmpty() || totalWeight <= 0) {
            return null; // 返回 null 表示没有选中物品
        }

        int randomValue = random.nextInt(totalWeight); // 生成一个小于 totalWeight 的随机数
        int accumulatedWeight = 0;

        for (var entry : outWeight.entrySet()) {
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
        recipe.outputs.get(ItemRecipeCapability.CAP).forEach(
                (content) -> {
                    if (content.getContent() instanceof SizedIngredient sizedIn) {
                        var item = sizedIn.getInner();
                        var weight = sizedIn.getAmount();

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
            if (machine instanceof VoidMinerMachine vmachine) {
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
        return (machine instanceof VoidMinerMachine vmachine && vmachine.xv == 3) ? OverclockingLogic.create(1, 1, false) : OverclockingLogic.NON_PERFECT_OVERCLOCK;
    }

    // 处理虚空采矿机特有逻辑
    private static void handleVoidMinerSpecifics(
                                                 VoidMinerMachine vmachine, GTRecipe originalRecipe, GTRecipe modifiedRecipe) {
        // 重置配置
        if (vmachine.recipe == null || vmachine.recipe != originalRecipe) {
            vmachine.reset(originalRecipe);
            vmachine.recipe = originalRecipe;
        }

        // 调整持续时间
        modifiedRecipe.duration = calculateDuration(originalRecipe.duration, vmachine.xv);

        // 添加输出物品
        ItemStack outputItem = generateVoidMinerOutput(vmachine);

        // 如果电压为EV+，则允许使用黑名单
        if (outputItem != null) {
            modifiedRecipe.outputs.put(
                    ItemRecipeCapability.CAP, List.of(new Content(SizedIngredient.create(outputItem), 1, 1, 0)));
        }
    }

    // 持续时间计算
    private static int calculateDuration(int baseDuration, int xvLevel) {
        return (baseDuration * 2) / ((xvLevel - 2) * 2);
    }

    // 生成输出物品
    private static ItemStack generateVoidMinerOutput(VoidMinerMachine vmachine) {
        Ingredient selectedIngredient = vmachine.getRandomItemByWeight();
        return (selectedIngredient != null && !selectedIngredient.isEmpty()) ? selectedIngredient.getItems()[0].copyWithCount(vmachine.itemSize) : null;
    }

    // public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
    // if (recipe == null) return recipe1 -> null;
    // GTRecipe gtRecipe = recipe.copy();
    // gtRecipe.outputs.clear();
    //
    ////
    // GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK).applyModifier(machine,
    // gtRecipe);
    //
    // GTRecipe newrecipe = null;
    // if (machine instanceof VoidMinerMachine vmachine) {
    // if (vmachine.recipe == null || vmachine.recipe != recipe) {
    // vmachine.reset(recipe);
    // vmachine.recipe = recipe;
    // }
    //
    // OverclockingLogic logic;
    // if (vmachine.xv == 3) {
    // logic = OverclockingLogic.create(1, 1, false);
    // } else {
    // logic = OverclockingLogic.NON_PERFECT_OVERCLOCK;
    // }
    //
    // newrecipe = GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(logic).applyModifier(machine, gtRecipe);
    //
    // if (newrecipe == null) return recipe1 -> null;
    //
    // newrecipe.duration = ((recipe.duration * 2) / ((vmachine.xv - 2) * 2));
    // Ingredient item = vmachine.getRandomItemByWeight();
    // if (item == null) {
    // GTRecipe finalNewrecipe = newrecipe;
    // return recipe1 -> finalNewrecipe;
    // }
    //
    // List<Content> itemList = List.of(new Content(SizedIngredient.create(item, vmachine.itemSize),
    // 1, 1, 0, null,
    // null));
    // newrecipe.outputs.put(ItemRecipeCapability.CAP, itemList);
    // }
    // GTRecipe finalNewrecipe1 = newrecipe;
    // return recipe1 -> finalNewrecipe1;
    // }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        textList.add(textList.size(), Component.literal("总权重：").append(String.valueOf(totalWeight)));
    }
}
