package com.tkisor.llrcore.config;

import net.minecraft.world.level.GameRules;

public class LlrGameRule {

    public static final GameRules.Key<GameRules.BooleanValue> llr_tntAndRailDupingFix = GameRules.register(
            "llr_tntAndRailDupingFix", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
}
