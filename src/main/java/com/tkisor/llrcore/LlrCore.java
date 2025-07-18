package com.tkisor.llrcore;

import com.tkisor.llrcore.config.LlrGameRule;
import com.tkisor.llrcore.datagen.Datagen;
import com.tkisor.llrcore.gtm.client.ClientProxy;
import com.tkisor.llrcore.gtm.common.CommonProxy;
import com.tkisor.llrcore.gtm.event.EventHandler;
import com.tkisor.llrcore.item.LlrItems;
import com.tkisor.llrcore.item.TicItem;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("removal")
@Mod(LlrCore.MOD_ID)
public final class LlrCore {

    public static final String MOD_ID = "llrcore";
    public static final Logger LOGGER = LogManager.getLogger("LLR Core");

    public LlrCore() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // LLRCoreConfig.create();
        modEventBus.addGenericListener(MachineDefinition.class, EventHandler::registerMachines);
        Datagen.init();

        LlrItems.register(modEventBus);
        TicItem.register(modEventBus);
        modEventBus.addListener(this::buildContents);
        modEventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.addListener(this::blockBreak);

        modEventBus.addGenericListener(GTRecipeType.class, EventHandler::registerRecipeTypes);
        modEventBus.addGenericListener(PartAbility.class, EventHandler::registerPart);
        DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }

    private void blockBreak(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        Player player = event.getPlayer();

        ItemStack heldItem = player.getMainHandItem();
        Block block = state.getBlock();

        if (heldItem.getItem() == LlrItems.cobbleBreaker.get()) {
            if (block == ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft", "cobblestone"))) {
                if (isSurroundedByWaterAndLava(level, pos)) {
                    player.addItem(new ItemStack(Blocks.COBBLESTONE));
                }
            }
            // 允许继续破坏，不取消事件
            event.setCanceled(true);
            return;
        }

        if (heldItem.getItem() == LlrItems.cobbleSmasher.get()) {
            if (block == ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft", "cobblestone"))) {
                if (isSurroundedByWaterAndLava(level, pos)) {
                    player.addItem(new ItemStack(Blocks.GRAVEL));
                }
            }
            event.setCanceled(true);
            return;
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(
                () -> {
                    // 触发静态初始化，确保规则被注册
                    LlrGameRule.llr_tntAndRailDupingFix.toString();
                });
    }

    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(new ItemStack(LlrItems.cobbleBreaker.get()));
            event.accept(new ItemStack(LlrItems.cobbleSmasher.get()));
        }
    }

    private static boolean isSurroundedByWaterAndLava(Level level, BlockPos pos) {
        boolean hasWater = false;
        boolean hasLava = false;

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);

            if (neighborState.getBlock() == Blocks.WATER) {
                hasWater = true;
            }
            if (neighborState.getBlock() == Blocks.LAVA) {
                hasLava = true;
            }

            if (hasWater && hasLava) {
                return true;
            }
        }

        return false;
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
}
