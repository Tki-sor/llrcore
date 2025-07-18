package com.tkisor.llrcore.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {

    @Shadow
    private int foodLevel;
    @Shadow
    private float saturationLevel;
    @Shadow
    private float exhaustionLevel;
    @Shadow
    private int tickTimer;
    @Shadow
    private int lastFoodLevel;

    @Unique
    private int maxFoodLevel = 40;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruct(CallbackInfo ci) {
        foodLevel = maxFoodLevel;
        lastFoodLevel = maxFoodLevel;
    }

    @Inject(method = "eat(IF)V", at = @At("HEAD"), cancellable = true)
    public void eat(int foodLevelModifier, float saturationLevelModifier, CallbackInfo ci) {
        ci.cancel();
        this.foodLevel = Math.min(foodLevelModifier + this.foodLevel, maxFoodLevel);
        this.saturationLevel = Math.min(
                this.saturationLevel + (float) foodLevelModifier * saturationLevelModifier * 2.0f, (float) this.foodLevel);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(Player player, CallbackInfo ci) {
        ci.cancel(); // 取消默认的 tick 处理

        // 获取玩家的世界难度和游戏规则
        Difficulty difficulty = player.level().getDifficulty();
        boolean naturalRegenerationEnabled = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        boolean isPlayerHurt = player.isHurt();

        // 记录玩家的食物值
        // if (this.foodLevel > maxFoodLevel) this.foodLevel = maxFoodLevel;
        this.lastFoodLevel = this.foodLevel;

        // 处理消耗体力（exhaustion）
        if (this.exhaustionLevel > 4.0f) {
            this.exhaustionLevel -= 4.0f;
            if (this.saturationLevel > 0.0f) {
                // 饱和度减少
                this.saturationLevel = Math.max(this.saturationLevel - 1.0f, 0.0f);
            } else if (difficulty != Difficulty.PEACEFUL) {
                // 在非和平模式下减少食物值
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        // 如果自然恢复启用并且饱和度大于0并且玩家受伤且食物值足够
        if (naturalRegenerationEnabled && this.saturationLevel > 0.0f && isPlayerHurt && this.foodLevel >= maxFoodLevel) {
            // 每10个tick恢复一定的生命值
            this.tickTimer++;
            if (this.tickTimer >= 10) {
                float healAmount = Math.min(this.saturationLevel, 6.0f); // 恢复的最大生命值为饱和度值与6.0f的较小值
                player.heal(healAmount / 6.0f); // 恢复生命
                this.addExhaustion(healAmount); // 添加消耗体力
                this.tickTimer = 0; // 重置计时器
            }
        }
        // 如果自然恢复启用并且食物值足够并且玩家受伤
        else if (naturalRegenerationEnabled && this.foodLevel >= (maxFoodLevel * 0.9) && isPlayerHurt) {
            // 每80个tick恢复1点生命
            this.tickTimer++;
            if (this.tickTimer >= 80) {
                player.heal(1.0f); // 恢复1点生命
                this.addExhaustion(6.0f); // 添加消耗体力
                this.tickTimer = 0; // 重置计时器
            }
        }
        // 如果玩家饥饿
        else if (this.foodLevel <= 0) {
            // 每80个tick对玩家造成伤害
            this.tickTimer++;
            if (this.tickTimer >= 80) {
                // 根据不同的难度对玩家造成伤害
                if (player.getHealth() > 10.0f || difficulty == Difficulty.HARD || player.getHealth() > 1.0f && difficulty == Difficulty.NORMAL) {
                    player.hurt(player.damageSources().starve(), 1.0f); // 饥饿造成伤害
                }
                this.tickTimer = 0; // 重置计时器
            }
        } else {
            // 如果食物值正常，重置计时器
            this.tickTimer = 0;
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains("foodLevel", 99)) {
            this.foodLevel = compoundTag.getInt("maxFoodLevel");
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putInt("maxFoodLevel", this.maxFoodLevel);
    }

    @Inject(method = "needsFood", at = @At("HEAD"), cancellable = true)
    public void needsFood(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Shadow
    public abstract void addExhaustion(float exhaustion);
}
