package com.tkisor.llrcore.gtm.registry;

import com.tkisor.llrcore.gtm.common.item.TestingTerminalBehavior;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import com.tterrag.registrate.util.entry.ItemEntry;

import static com.gregtechceu.gtceu.common.data.GTItems.attach;
import static com.tkisor.llrcore.api.registries.LLRRegistration.LLR;

public class LLRItems {

    static {
        LLR.creativeModeTab(() -> LLRCreativeModeTabs.ITEM);
    }

    public static ItemEntry<ComponentItem> TESTING_TERMINAL = LLR.item("testing_terminal", ComponentItem::create).lang("Test Terminal").properties(p -> p.stacksTo(1)).onRegister(attach(new TestingTerminalBehavior())).onRegister(
            attach(
                    new TooltipBehavior(
                            list -> {
                                list.add(
                                        Component.translatable("llrcore.testing_terminal.tooltip.1").withStyle(ChatFormatting.GRAY));
                                list.add(Component.translatable("llrcore.testing_terminal.tooltip.2"));
                            })))
            .register();

    public static void init() {}
}
