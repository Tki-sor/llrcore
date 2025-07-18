package com.tkisor.llrcore.gtm.common.machine.multiblock.electric;

import com.tkisor.llrcore.gtm.common.machine.multiblock.part.NetworkStoreHatch;
import com.tkisor.llrcore.gtm.common.machine.multiblock.part.StoreCellHatch;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

import appeng.api.storage.IStorageProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NetworkStoreMultiblockMachine extends WorkableElectricMultiblockMachine {

    public NetworkStoreMultiblockMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    // 成型时，每次加载世界可以触发
    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        networkUpdate();
    }

    @Override
    public boolean isWorkingEnabled() {
        return true;
    }

    @Override
    protected void onPartsUpdated(BlockPos[] newValue, BlockPos[] oldValue) {
        super.onPartsUpdated(newValue, oldValue);
        // networkReset();
    }

    @Nullable
    public NetworkStoreHatch getNetWorkStoreHatch() {
        return this.getParts().stream().filter(NetworkStoreHatch.class::isInstance).findFirst().map(NetworkStoreHatch.class::cast).orElse(null);
    }

    public List<StoreCellHatch> getStoreCellHatchList() {
        return this.getParts().stream().filter(StoreCellHatch.class::isInstance).map(StoreCellHatch.class::cast).toList();
    }

    public void networkReset() {
        NetworkStoreHatch networkStoreHatch = getNetWorkStoreHatch();
        if (networkStoreHatch == null) {
            return;
        }

        networkStoreHatch.invReset();
        networkStoreHatch.invBySlotReset();
        getNetWorkStoreHatch().setCached(false);
        IStorageProvider.requestUpdate(networkStoreHatch.getMainNode());
    }

    /** 更新网络仓的网络连接和，以及重新检索存储状态 */
    public void networkUpdate() {
        NetworkStoreHatch networkStoreHatch = getNetWorkStoreHatch();
        if (networkStoreHatch == null) {
            return;
        }

        networkStoreHatch.invReset();

        List<StoreCellHatch> storeCellHatchList = getStoreCellHatchList();
        int size = storeCellHatchList.size();
        for (int i = 0; i < size; i++) {
            ItemStack stack = storeCellHatchList.get(i).machineStorage.storage.getStackInSlot(0);
            networkStoreHatch.getInv().insertItem(i, stack, false);
        }

        getNetWorkStoreHatch().setCached(false);
        IStorageProvider.requestUpdate(networkStoreHatch.getMainNode());
    }
}
