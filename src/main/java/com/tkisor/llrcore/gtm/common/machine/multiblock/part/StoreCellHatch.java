package com.tkisor.llrcore.gtm.common.machine.multiblock.part;

import com.tkisor.llrcore.gtm.common.machine.multiblock.electric.NetworkStoreMultiblockMachine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import appeng.api.storage.StorageCells;
import appeng.api.storage.cells.IBasicCellItem;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StoreCellHatch extends MultiblockPartMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(StoreCellHatch.class, TieredPartMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    public final NotifiableItemStackHandler machineStorage;

    public StoreCellHatch(IMachineBlockEntity holder) {
        super(holder);
        this.machineStorage = createMachineStorage((byte) 1);
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    protected NotifiableItemStackHandler createMachineStorage(byte value) {
        return new NotifiableItemStackHandler(
                this, 1, IO.NONE, IO.BOTH, slots -> new CustomItemStackHandler(1) {

                    @Override
                    public int getSlotLimit(int slot) {
                        return value;
                    }

                    @Override
                    public void onContentsChanged(int slot) {
                        super.onContentsChanged(slot);
                    }
                });
    }

    public @Nullable NetworkStoreMultiblockMachine getController() {
        if (this.getControllers().isEmpty()) {
            return null;
        }
        return (NetworkStoreMultiblockMachine) (this.getControllers().first());
    }

    @Override
    public void onChanged() {
        super.onChanged();
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(new Position(0, 0));
        var size = group.getSize();
        group.addWidget(
                new SlotWidget(machineStorage.storage, 0, size.width - 30, size.height - 30, true, true) {

                    @Override
                    public boolean canPutStack(ItemStack stack) {
                        boolean handled = StorageCells.isCellHandled(stack);
                        return super.canPutStack(stack) && handled;
                    }
                }.setChangeListener(
                        () -> {
                            // 物品更新触发
                            if (getController() != null) {
                                getController().networkUpdate();
                            }
                        }));
        group.addWidget(new LabelWidget(0, 0, () -> ""));
        return group;
    }

    public @Nullable IBasicCellItem getStorageCell() {
        ItemStack stackInSlot = machineStorage.storage.getStackInSlot(0);

        if (stackInSlot.isEmpty()) return null;

        Item item = stackInSlot.getItem();
        return (item instanceof IBasicCellItem iBasicCellItem) ? iBasicCellItem : null;
    }
}
