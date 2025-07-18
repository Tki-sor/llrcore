package com.tkisor.llrcore.gtm.common.block;

import com.tkisor.llrcore.LlrCore;
import com.tkisor.llrcore.gtm.registry.LLRMaterials;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum CoilType implements StringRepresentable, ICoilType {

    INFINITY(
            "infinity", 36000, 128, 9, LLRMaterials.Infinity, LlrCore.id("block/casings/coils/infinity_coil_block"));

    @NotNull
    private final String name;
    // electric blast furnace properties
    private final int coilTemperature;
    // multi smelter properties
    private final int level;
    private final int energyDiscount;
    @NotNull
    private final Material material;
    @NotNull
    private final ResourceLocation texture;

    CoilType(
             String name, int coilTemperature, int level, int energyDiscount, Material material, ResourceLocation texture) {
        this.name = name;
        this.coilTemperature = coilTemperature;
        this.level = level;
        this.energyDiscount = energyDiscount;
        this.material = material;
        this.texture = texture;
    }

    public int getTier() {
        return this.ordinal();
    }

    @NotNull
    @Override
    public String toString() {
        return getName();
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return name;
    }
}
