package com.tkisor.llrcore.gtm.common.machine.multiblock.part;

import com.tkisor.llrcore.gtm.common.machine.multiblock.electric.NetworkStoreMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import appeng.api.implementations.blockentities.IChestOrDrive;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.orientation.BlockOrientation;
import appeng.api.storage.IStorageMounts;
import appeng.api.storage.IStorageProvider;
import appeng.api.storage.MEStorage;
import appeng.api.storage.StorageCells;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.StorageCell;
import appeng.blockentity.inventory.AppEngCellInventory;
import appeng.helpers.IPriorityHost;
import appeng.hooks.ticking.TickHandler;
import appeng.me.storage.DriveWatcher;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.implementations.DriveMenu;
import appeng.menu.locator.MenuLocators;
import appeng.util.inv.InternalInventoryHost;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class NetworkStoreHatch extends MultiblockPartMachine implements IInteractedMachine, IGridConnectedMachine, IChestOrDrive, IPriorityHost, IStorageProvider, InternalInventoryHost {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(NetworkStoreHatch.class, TieredPartMachine.MANAGED_FIELD_HOLDER);

    @DescSynced
    @Getter
    @Setter
    private int priority;

    private DriveWatcher[] invBySlot = new DriveWatcher[getCellCount()];
    @Getter
    private AppEngCellInventory inv = new AppEngCellInventory(this, getCellCount());
    @DescSynced
    @Getter
    @Setter
    private boolean isCached = false;
    @DescSynced
    @Getter
    @Setter
    private boolean wasOnline = false;

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @DescSynced
    @Getter
    @Setter
    protected boolean isOnline;

    @Persisted
    protected final GridNodeHolder nodeHolder;
    protected final IActionSource actionSource;

    public NetworkStoreHatch(IMachineBlockEntity holder) {
        super(holder);
        this.nodeHolder = createNodeHolder();
        this.actionSource = IActionSource.ofMachine(nodeHolder.getMainNode()::getNode);

        getMainNode().addService(IStorageProvider.class, this).setFlags(GridFlags.REQUIRE_CHANNEL);
        // getMainNode().addService(IStorageProvider.class, this);
        this.subscribeServerTick(
                () -> {
                    if (this.getLevel().getGameTime() % 5 == 0) {
                        networkUpdate();
                    }
                });
    }

    protected GridNodeHolder createNodeHolder() {
        return new GridNodeHolder(this);
    }

    @Override
    public IManagedGridNode getMainNode() {
        return nodeHolder.getMainNode();
    }

    @Override
    public Set<Direction> getGridConnectableSides(BlockOrientation orientation) {
        return IGridConnectedMachine.super.getGridConnectableSides(orientation);
    }

    @Override
    public @Nullable IGridNode getGridNode() {
        return IGridConnectedMachine.super.getGridNode();
    }

    @Override
    public IGridNode getGridNode(Direction dir) {
        return IGridConnectedMachine.super.getGridNode(dir);
    }

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        IGridConnectedMachine.super.onMainNodeStateChanged(reason);
        var currentOnline = getMainNode().isOnline();
        if (this.wasOnline != currentOnline) {
            this.wasOnline = currentOnline;
            IStorageProvider.requestUpdate(this.getMainNode());
        }
        onUpdate();
    }

    @Override
    public void onRotated(Direction oldFacing, Direction newFacing) {
        super.onRotated(oldFacing, newFacing);
        getMainNode().setExposedOnSides(EnumSet.of(newFacing));
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(new Position(0, 0));
        // ME Network status
        group.addWidget(
                new LabelWidget(
                        0, 0, () -> this.isOnline() ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));

        return group;
    }

    public NetworkStoreMultiblockMachine getController() {
        if (this.getControllers().isEmpty()) {
            return null;
        }
        return (NetworkStoreMultiblockMachine) (this.getControllers().first());
    }

    @Override
    public void onChangeInventory(InternalInventory internalInventory, int i) {
        if (this.isCached) {
            this.isCached = false; // recalculate the storage cell.
            this.updateState();
        }

        IStorageProvider.requestUpdate(this.getMainNode());
    }

    // IChestOrDrive
    public boolean isClientSide() {
        Level level = getLevel();
        return level == null || level.isClientSide();
    }

    @Override
    public int getCellCount() {
        // todo
        // 从控制器拿到所有仓的总槽数，然后再（
        NetworkStoreMultiblockMachine controller = this.getController();
        if (controller == null) return 0;

        return this.getController().getStoreCellHatchList().size();
    }

    @Override
    public CellState getCellStatus(int slot) {
        var handler = this.invBySlot[slot];
        if (handler == null) {
            return CellState.ABSENT;
        }

        return handler.getStatus();
    }

    @Override
    public boolean onWorking(IWorkableMultiController controller) {
        return super.onWorking(controller);
    }

    public void onUpdate() {
        this.isCached = false;
        updateState();
    }

    public void invReset() {
        this.inv = new AppEngCellInventory(this, getCellCount());
    }

    public void invBySlotReset() {
        this.invBySlot = new DriveWatcher[getCellCount()];
    }

    private void updateState() {
        if (!this.isCached) {
            this.invBySlot = new DriveWatcher[getCellCount()];

            double power = 2.0;

            for (int slot = 0; slot < this.inv.size(); slot++) {
                power += updateStateForSlot(slot);
            }
            this.getMainNode().setIdlePowerUsage(power);

            this.isCached = true;
        }
    }

    private double updateStateForSlot(int slot) {
        invBySlot[slot] = null; // 重置插槽状态
        ItemStack stack = inv.getStackInSlot(slot);

        if (!stack.isEmpty()) {
            // 从物品创建存储单元对象
            StorageCell cell = StorageCells.getCellInventory(stack, null);
            if (cell != null) {
                // 创建监控器并挂载
                DriveWatcher watcher = new DriveWatcher(cell, () -> {});
                invBySlot[slot] = watcher;

                inv.setHandler(slot, cell); // 将存储单元绑定到插槽
                return cell.getIdleDrain(); // 返回该单元待机能耗
            }
        }
        return 0; // 空插槽不耗能
    }

    public void networkUpdate() {
        var controller = getController();
        int invSize = inv.size();

        // 将物品从库存传输到控制器存储
        if (controller != null) {
            List<StoreCellHatch> storeList = controller.getStoreCellHatchList();
            int storeSize = storeList.size();
            int transferLimit = Math.min(invSize, storeSize);

            for (int i = 0; i < transferLimit; i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    storeList.get(i).machineStorage.storage.setStackInSlot(0, stack);
                }
            }
        }

        invReset(); // 重置库存

        // 处理无控制器情况
        if (controller == null) {
            updateStorageState();
            return;
        }

        // 将物品从控制器存储同步回库存
        List<StoreCellHatch> storeList = controller.getStoreCellHatchList();
        int storeSize = storeList.size();
        int syncLimit = Math.min(storeSize, invSize);

        for (int i = 0; i < syncLimit; i++) {
            ItemStack stack = storeList.get(i).machineStorage.storage.getStackInSlot(0);
            inv.insertItem(i, stack, false);
        }

        updateStorageState();
    }

    private void updateStorageState() {
        setCached(false);
        IStorageProvider.requestUpdate(getMainNode());
    }

    @Override
    public boolean isPowered() {
        return this.isOnline();
    }

    @Override
    public boolean isCellBlinking(int i) {
        return false;
    }

    @Override
    public @Nullable Item getCellItem(int slot) {
        ItemStack stackInSlot = inv.getStackInSlot(slot);
        if (!stackInSlot.isEmpty()) {
            return stackInSlot.getItem();
        }
        return null;
    }

    @Override
    public @Nullable MEStorage getCellInventory(int slot) {
        return this.invBySlot[slot];
    }

    @Override
    public @Nullable StorageCell getOriginalCellInventory(int slot) {
        var handler = this.invBySlot[slot];
        if (handler != null) {
            return handler.getCell();
        }
        return null;
    }

    // IStorageProvider
    @Override
    public void mountInventories(IStorageMounts storageMounts) {
        if (this.getMainNode().isOnline()) {
            this.updateState();
            for (DriveWatcher watcher : this.invBySlot) {
                if (watcher != null) storageMounts.mount(watcher, priority);
            }
        }
    }

    // IPriorityHost
    @Override
    public void returnToMainMenu(Player player, ISubMenu iSubMenu) {
        MenuOpener.returnTo(DriveMenu.TYPE, player, MenuLocators.forBlockEntity(this.holder.self()));
    }

    @Override
    public ItemStack getMainMenuIcon() {
        return Items.APPLE.getDefaultInstance();
    }

    private boolean setChangedQueued = false;

    public void saveChanges() {
        if (this.getLevel() == null) {
            return;
        }

        if (this.getLevel().isClientSide) {
            this.holder.self().setChanged();
        } else {
            this.getLevel().blockEntityChanged(this.holder.getSelf().getBlockPos());
            if (!this.setChangedQueued) {
                TickHandler.instance().addCallable(null, this::setChangedAtEndOfTick);
                this.setChangedQueued = true;
            }
        }
    }

    private Object setChangedAtEndOfTick(Level level) {
        this.holder.self().setChanged();
        this.setChangedQueued = false;
        return null;
    }
}
