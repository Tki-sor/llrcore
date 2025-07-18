package com.tkisor.llrcore.mixin;

import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;

import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = MachineBuilder.class, remap = false)
public interface MachineBuilderAccessor {

    @Accessor("tooltips")
    List<Component> getTooltips();

    @Accessor("name")
    String getName();
}
