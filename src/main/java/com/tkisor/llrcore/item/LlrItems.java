package com.tkisor.llrcore.item;

import com.tkisor.llrcore.LlrCore;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LlrItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LlrCore.MOD_ID);
    public static final RegistryObject<Item> cobbleBreaker = ITEMS.register("cobble_breaker", CobbleBreakerPickaxe::new);

    public static final RegistryObject<Item> cobbleSmasher = ITEMS.register("cobble_smasher", CobbleBreakerPickaxe::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
