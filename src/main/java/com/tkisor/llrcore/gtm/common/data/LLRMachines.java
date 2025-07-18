package com.tkisor.llrcore.gtm.common.data;

import com.gregtechceu.gtceu.client.util.TooltipHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;

public class LLRMachines {

    private static final ChatFormatting[] ALL_COLOR = new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.DARK_AQUA, ChatFormatting.DARK_BLUE, ChatFormatting.BLUE, ChatFormatting.DARK_PURPLE, ChatFormatting.LIGHT_PURPLE
    };
    public static final TooltipHelper.GTFormattingCode RAINBOW_SLOW_S;

    static {
        RAINBOW_SLOW_S = TooltipHelper.createNewCode(50, ALL_COLOR);
    }

    public static final BiConsumer<ItemStack, List<Component>> LLR_MODIFY = (stack, components) -> components.add(
            Component.translatable("llrcore.registry.modify").withStyle(style -> style.withColor(TooltipHelper.RAINBOW.getCurrent())));

    public static final BiConsumer<ItemStack, List<Component>> LLR_ADD = (stack, components) -> components.add(
            Component.translatable("llrcore.registry.add").withStyle(style -> style.withColor(TooltipHelper.RAINBOW_SLOW.getCurrent())));

    public static final BiConsumer<ItemStack, List<Component>> TST_ADD = (stack, components) -> components.add(
            Component.translatable("llrcore.registry.add.structure", "Twist Space Technology").withStyle(style -> style.withColor(RAINBOW_SLOW_S.getCurrent())));

    public static final BiConsumer<ItemStack, List<Component>> CTNH_ADD = (stack, components) -> components.add(
            Component.translatable("llrcore.registry.add.structure", "CTNH").withStyle(style -> style.withColor(RAINBOW_SLOW_S.getCurrent())));

    public static final BiConsumer<ItemStack, List<Component>> GTPP_ADD = (stack, components) -> components.add(
            Component.translatable("llrcore.registry.add.structure", "GT++").withStyle(style -> style.withColor(RAINBOW_SLOW_S.getCurrent())));
}
