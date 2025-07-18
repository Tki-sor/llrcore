package com.tkisor.llrcore.gtm.common.machine.multiblock.steam;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.steam.SteamEnergyRecipeHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.*;

import java.util.List;

public class SteamMultiblockMachine extends SteamParallelMultiblockMachine implements IFancyUIMachine, IDisplayUIMachine {

    public SteamMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    //////////////////////////////////////
    // ********** GUI ***********//
    //////////////////////////////////////
    @Override
    public void addDisplayText(List<Component> textList) {
        MultiblockDisplayText.builder(textList, isFormed()).addCustom(
                components -> {
                    if (isFormed()) {
                        var handlers = capabilitiesProxy.get(IO.IN);
                        if (handlers != null && !handlers.isEmpty() && handlers.get(0).getCapability(EURecipeCapability.CAP).get(0) instanceof SteamEnergyRecipeHandler steamHandler) {
                            if (steamHandler.getCapacity() > 0) {
                                long steamStored = steamHandler.getStored();
                                components.add(
                                        Component.translatable(
                                                "gtceu.multiblock.steam.steam_stored", steamStored, steamHandler.getCapacity()));
                            }
                        }
                    }
                }).setWorkingStatus(recipeLogic.isWorkingEnabled(), recipeLogic.isActive()).addMachineModeLine(getRecipeType(), getRecipeTypes().length > 1).addWorkingStatusLine().addProgressLine(
                        recipeLogic.getProgress(), recipeLogic.getMaxProgress(), recipeLogic.getProgressPercent())
                .addOutputLines(recipeLogic.getLastRecipe());
        getDefinition().getAdditionalDisplay().accept(this, textList);
        IaddDisplayText(textList);
    }

    private void IaddDisplayText(List<Component> textList) {
        for (var part : this.getParts()) {
            part.addMultiText(textList);
        }
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        group.addWidget(
                new DraggableScrollableWidgetGroup(4, 4, 182, 117).setBackground(getScreenTexture()).addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId())).addWidget(
                        new ComponentPanelWidget(4, 17, this::addDisplayText).textSupplier(this.getLevel().isClientSide ? null : this::addDisplayText).setMaxWidthLimit(200).clickHandler(this::handleDisplayClick)));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(198, 208, this, entityPlayer).widget(new FancyMachineUIWidget(this, 198, 208));
    }
}
