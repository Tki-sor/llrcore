package com.tkisor.llrcore.gtm.registry;

public class LLRBlocks {

    static {
        // REGISTRATE.creativeModeTab(() -> LLRCreativeModeTabs.BLOCK);
    }

    // @SuppressWarnings("all")
    // private static BlockEntry<CoilBlock> createCoilBlock(ICoilType coilType) {
    // BlockEntry<CoilBlock> coilBlock =
    // REGISTRATE.block("%s_coil_block".formatted(coilType.getName()), p -> new
    // CoilBlock(p, coilType))
    // .initialProperties(() -> Blocks.IRON_BLOCK)
    // .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
    // .addLayer(() -> RenderType::cutoutMipped)
    // .blockstate(LLRModels.createCoilModel("%s_coil_block".formatted(coilType.getName()), coilType))
    // .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
    // .item(BlockItem::new)
    // .build()
    // .register();
    // GTCEuAPI.HEATING_COILS.put(coilType, coilBlock);
    // return coilBlock;
    // }

    // public static final BlockEntry<CoilBlock> COIL_INFINITY = createCoilBlock(CoilType.INFINITY);

    public static void init() {}
}
