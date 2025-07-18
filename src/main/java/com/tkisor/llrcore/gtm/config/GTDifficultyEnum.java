package com.tkisor.llrcore.gtm.config;

public enum GTDifficultyEnum {

    Easy(0.25),
    Normal(0.5),
    Hard(1);

    private final double difficulty_1x;

    GTDifficultyEnum(double difficulty_1x) {
        this.difficulty_1x = difficulty_1x;
    }

    public double getDifficultyMultiplier() {
        return difficulty_1x;
    }

    public static GTDifficultyEnum fromMultiplier(double value) {
        for (GTDifficultyEnum difficulty : values()) {
            if (Math.abs(difficulty.difficulty_1x - value) < 0.0001) { // 误差处理
                return difficulty;
            }
        }
        return Normal;
    }

    public static GTDifficultyEnum fromString(String name) {
        try {
            return GTDifficultyEnum.valueOf(name);
        } catch (IllegalArgumentException e) {
            return Normal;
        }
    }
}
