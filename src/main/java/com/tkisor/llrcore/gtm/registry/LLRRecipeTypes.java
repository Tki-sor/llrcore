package com.tkisor.llrcore.gtm.registry;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.utils.CycleItemStackHandler;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import java.util.ArrayList;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.GENERATOR;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;

public class LLRRecipeTypes {

    public static final GTRecipeType sieve_factory = GTRecipeTypes.register("sieve_factory", GTRecipeTypes.ELECTRIC).setMaxIOSize(2, 30, 1, 0).setEUIO(IO.IN).setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY).setProgressBar(GuiTextures.PROGRESS_BAR_SIFT, ProgressTexture.FillDirection.UP_TO_DOWN).setSound(GTSoundEntries.ELECTROLYZER);

    public static final GTRecipeType hammer_factory = GTRecipeTypes.register("hammer_factory", GTRecipeTypes.ELECTRIC).setEUIO(IO.IN).setMaxIOSize(1, 4, 0, 0) // ItemI,
                                                                                                                                                               // ItemO,
                                                                                                                                                               // FluidI,
                                                                                                                                                               // FluidO
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY).setProgressBar(GuiTextures.PROGRESS_BAR_SIFT, ProgressTexture.FillDirection.UP_TO_DOWN).setSound(GTSoundEntries.ELECTROLYZER);

    public static final GTRecipeType large_thermoelectric_generator = GTRecipeTypes.register("large_thermoelectric_generator", GTRecipeTypes.ELECTRIC).setEUIO(IO.OUT).setMaxIOSize(0, 2, 2, 2).setSlotOverlay(true, false, GuiTextures.SOLIDIFIER_OVERLAY).setProgressBar(GuiTextures.PROGRESS_BAR_SIFT, ProgressTexture.FillDirection.UP_TO_DOWN).setSound(GTSoundEntries.ELECTROLYZER);

    public static final GTRecipeType martial_morality_eye = GTRecipeTypes.register("martial_morality_eye", GTRecipeTypes.ELECTRIC).setEUIO(IO.IN).setMaxIOSize(2, 27, 1, 3).setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY).setProgressBar(
            GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT).setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType slaughter_house = GTRecipeTypes.register("slaughter_house", GTRecipeTypes.ELECTRIC).setMaxIOSize(4, 4, 2, 2).setEUIO(IO.IN).setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY).setProgressBar(
            GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT).setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType void_miner = GTRecipeTypes.register("void_miner", GTRecipeTypes.ELECTRIC).setMaxIOSize(2, 81, 2, 0).setEUIO(IO.IN).setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY).setProgressBar(
            GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT).setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType void_oil_drill = GTRecipeTypes.register("void_oil_drill", GTRecipeTypes.ELECTRIC).setMaxIOSize(0, 0, 1, 8).setEUIO(IO.IN).setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY).setProgressBar(
            GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT).setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType void_gas_collection_chamber = GTRecipeTypes.register("void_gas_collection_chamber", GTRecipeTypes.ELECTRIC).setEUIO(IO.IN).setMaxIOSize(2, 2, 2, 2).setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY).setProgressBar(
            GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT).setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType fuel_refining = GTRecipeTypes.register("fuel_refining", GTRecipeTypes.ELECTRIC).setEUIO(IO.IN).setMaxIOSize(3, 3, 3, 3).setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY).setProgressBar(
            GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT).setSound(GTSoundEntries.ELECTROLYZER).addDataInfo(
                    data -> LocalizationUtils.format(
                            "gtceu.recipe.temperature", FormattingUtil.formatNumbers(data.getInt("ebf_temp"))))
            .addDataInfo(
                    data -> {
                        var requiredCoil = ICoilType.getMinRequiredType(data.getInt("ebf_temp"));
                        if (LDLib.isClient() && requiredCoil != null && requiredCoil.getMaterial() != null) {
                            return LocalizationUtils.format(
                                    "gtceu.recipe.coil.tier", I18n.get(requiredCoil.getMaterial().getUnlocalizedName()));
                        }
                        return "";
                    })
            .setUiBuilder(
                    (recipe, widgetGroup) -> {
                        var temp = recipe.data.getInt("ebf_temp");
                        var items = new ArrayList();
                        items.add(
                                GTCEuAPI.HEATING_COILS.entrySet().stream().filter(coil -> coil.getKey().getCoilTemperature() >= temp).map(coil -> new ItemStack(coil.getValue().get())).toList());
                        widgetGroup.addWidget(
                                new SlotWidget(
                                        new CycleItemStackHandler(items), 0, widgetGroup.getSize().width - 25, widgetGroup.getSize().height - 32, false, false));
                    });

    public static final GTRecipeType quantum_force_transformer = GTRecipeTypes.register("quantum_force_transformer", GTRecipeTypes.ELECTRIC).setEUIO(IO.IN).setMaxIOSize(6, 12, 6, 12).setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY).setProgressBar(
            GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT).setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType steam_blast_furnace = GTRecipeTypes.register("steam_blast_furnace", GTRecipeTypes.MULTIBLOCK).setEUIO(IO.IN).setMaxIOSize(2, 2, 0, 0).setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY).setProgressBar(
            GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT).setSound(GTSoundEntries.FIRE);

    public static final GTRecipeType storage_cell = GTRecipeTypes.register("storage_cell", GTRecipeTypes.ELECTRIC).setEUIO(IO.IN).setMaxIOSize(1, 0, 0, 0).setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY).setProgressBar(
            GuiTextures.PROGRESS_BAR_ARROW, ProgressTexture.FillDirection.LEFT_TO_RIGHT).setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType semi_fluid_generator = GTRecipeTypes.register("semi_fluid_generator", GENERATOR).setMaxIOSize(0, 0, 2, 0).setEUIO(IO.OUT).setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2).setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT).setSound(GTSoundEntries.COMBUSTION);

    public static final GTRecipeType thermal_generator = GTRecipeTypes.register("thermal_generator", GENERATOR).setMaxIOSize(1, 0, 1, 0).setEUIO(IO.OUT).setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2).setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT).setSound(GTSoundEntries.COMBUSTION);

    public static void init() {}
}
