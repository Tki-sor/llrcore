package com.tkisor.llrcore.gtm.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_FRAME;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.METALLIC;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.HIGHEST;

public class LLRMaterials {

    public static final Material Infinity = new Material.Builder(GTCEu.id("my_infinity")).ingot().plasma().radioactiveHazard(20).blastTemp(32000, HIGHEST).element(LLRElements.INFINITY).iconSet(METALLIC).flags(GENERATE_FRAME).cableProperties(GTValues.V[GTValues.MAX], 8192, 0, true).buildAndRegister();

    public static void init() {}
}
