package com.tkisor.llrcore.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.registries.ForgeRegistries;

import org.jetbrains.annotations.NotNull;

public class CobbleBreakerPickaxe extends PickaxeItem {

    public CobbleBreakerPickaxe() {
        super(
                new ForgeTier(
                        0, 233, 30, 0, 20, TagKey.create(
                                ForgeRegistries.BLOCKS.getRegistryKey(), new ResourceLocation("forge", "cobblestone")),
                        () -> Ingredient.of(Items.COBBLESTONE)),
                1, -2.8f, new Properties().durability(-1));
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return blockState.is(Blocks.COBBLESTONE);
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull ItemStack stack, BlockState blockState) {
        return blockState.is(Blocks.COBBLESTONE);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }
}
