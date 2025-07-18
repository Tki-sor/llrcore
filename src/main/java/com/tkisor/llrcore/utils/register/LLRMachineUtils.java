package com.tkisor.llrcore.utils.register;

import com.tkisor.llrcore.LlrCore;
import com.tkisor.llrcore.api.LLRValues;
import com.tkisor.llrcore.api.registries.LLRRegistration;
import com.tkisor.llrcore.utils.LLRUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.network.chat.Component;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LLRMachineUtils {

    /**
     * 注册一系列按电压等级分层的发电机
     *
     * @param machineName  机器名称（英文标识符，如 "semi_fluid"）
     * @param langName     本地化名称（如 "发电机"）
     * @param recipeType   该机器使用的配方类型
     * @param amperageFunc 电流输出函数（根据电压等级返回输出安培数）
     * @param tiers        要注册的电压等级（如 ULV, LV, MV, HV）
     * @return 所有电压等级的机器定义数组
     */
    public static MachineDefinition[] registerSimpleGenerator(
                                                              String machineName, String langName, GTRecipeType recipeType, Int2IntFunction amperageFunc, int... tiers) {
        return registerTieredMachines(
                machineName, tier -> String.format("%s%s %s", LLRValues.VLVHCN[tier], langName, GTValues.VLVT[tier]), (machine, tier) -> new SimpleGeneratorMachine(machine, tier, 0.1F * tier, amperageFunc, new Object[0]), (tier, builder) -> builder.langValue(
                        String.format(
                                "%s %s %s", GTValues.VLVH[tier], FormattingUtil.toEnglishName(machineName), GTValues.VLVT[tier]))
                        .editableUI(
                                SimpleGeneratorMachine.EDITABLE_UI_CREATOR.apply(
                                        GTCEu.id(machineName), recipeType))
                        .rotationState(RotationState.ALL).recipeType(recipeType).recipeModifier(SimpleGeneratorMachine::recipeModifier, true).addOutputLimit(ItemRecipeCapability.CAP, 0).addOutputLimit(FluidRecipeCapability.CAP, 0).simpleGeneratorModel(LlrCore.id("block/generators/"))
                        // .tooltips(Component.translatable("llrcore.machine.efficiency.tooltip",
                        // LLRUtils.getGeneratorEfficiency(recipeType, tier)).append("%"))
                        .tooltips(
                                Component.translatable(
                                        "gtceu.universal.tooltip.amperage_out", LLRUtils.getGeneratorAmperage(tier)))
                        .tooltips(
                                GTMachineUtils.workableTiered(
                                        tier, GTValues.V[tier], GTValues.V[tier] << 6, recipeType, (long) amperageFunc.apply(tier), false))
                        .register(),
                tiers);
    }

    /**
     * 注册多个电压等级的机器定义
     *
     * @param machineName    机器名称（英文标识符）
     * @param langNameFunc   本地化名称生成函数
     * @param machineFactory 机器实体工厂函数
     * @param builderFunc    机器构建器配置函数
     * @param tiers          要注册的电压等级
     * @return 所有电压等级的机器定义数组
     */
    public static MachineDefinition[] registerTieredMachines(
                                                             String machineName, Function<Integer, String> langNameFunc, BiFunction<IMachineBlockEntity, Integer, MetaMachine> machineFactory, BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builderFunc, int... tiers) {
        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];

        for (int tier : tiers) {
            String registryName = GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + machineName;
            if (langNameFunc != null) {
                LLRBlockRegister.addLang(registryName, langNameFunc.apply(tier));
            }

            MachineBuilder<MachineDefinition> builder = LLRRegistration.LLR.machine(registryName, (machine) -> machineFactory.apply(machine, tier)).tier(tier);

            definitions[tier] = builderFunc.apply(tier, builder);
        }

        return definitions;
    }
}
