package com.tkisor.llrcore.item;

import com.tkisor.llrcore.LlrCore;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;

import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

import static com.tkisor.llrcore.item.ToolDefinitions.COMPRESSED_HAMMER;
import static com.tkisor.llrcore.item.ToolDefinitions.PAXEL;

public class TicItem {

    protected static final ItemDeferredRegisterExtension ITEMS = new ItemDeferredRegisterExtension(LlrCore.MOD_ID);

    protected static final Item.Properties Stack1Item = new Item.Properties().stacksTo(1);
    protected static final Item.Properties GENERAL_PROPS = new Item.Properties();

    public static final ItemObject<ModifiableItem> paxel = ITEMS.register("hammer_exn", () -> new ModifiableItem(Stack1Item, PAXEL));

    public static final ItemObject<ModifiableItem> compressed_hammer = ITEMS.register("compressed_hammer", () -> new ModifiableItem(Stack1Item, COMPRESSED_HAMMER));

    public static final ItemObject<ToolPartItem> compressed_hammer_head = ITEMS.register(
            "compressed_hammer_head", () -> new ToolPartItem(GENERAL_PROPS, HeadMaterialStats.ID));
    public static final CastItemObject compressed_hammer_head_cast = ITEMS.registerCast(compressed_hammer_head, GENERAL_PROPS);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
