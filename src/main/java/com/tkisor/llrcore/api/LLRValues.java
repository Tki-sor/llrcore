package com.tkisor.llrcore.api;

import net.minecraft.ChatFormatting;

public class LLRValues {

    public static final String[] VLVHCN;

    static {
        String[] labels = new String[] { "原始", "基础", ChatFormatting.AQUA + "进阶", ChatFormatting.GOLD + "进阶", ChatFormatting.DARK_PURPLE + "进阶", ChatFormatting.BLUE + "精英", ChatFormatting.LIGHT_PURPLE + "精英", ChatFormatting.RED + "精英", ChatFormatting.DARK_AQUA + "终极", ChatFormatting.DARK_RED + "史诗", ChatFormatting.GREEN + "史诗", ChatFormatting.DARK_GREEN + "史诗", ChatFormatting.YELLOW + "史诗", null, null };
        String colorCode = ChatFormatting.BLUE.toString();
        labels[13] = colorCode + ChatFormatting.BOLD + "传奇";

        colorCode = ChatFormatting.RED.toString();
        labels[14] = colorCode + ChatFormatting.BOLD + "MAX";

        VLVHCN = labels;
    }
}
