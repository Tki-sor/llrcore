package com.tkisor.llrcore.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ForgeGui.class, remap = false)
public abstract class GuiMixin extends Gui {

    @Shadow
    public int rightHeight;

    public GuiMixin(Minecraft arg, ItemRenderer arg2) {
        super(arg, arg2);
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    public void renderFood(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {
        ci.cancel();
        minecraft.getProfiler().push("food");

        Player player = (Player) this.minecraft.getCameraEntity();
        RenderSystem.enableBlend();
        int left = width / 2 + 91;
        int top = height - rightHeight;
        rightHeight += 10;

        FoodData stats = minecraft.player.getFoodData();
        int level = stats.getFoodLevel();
        int maxFoodLevel = 40; // 最大饥饿值
        int iconsCount = 10; // 饥饿图标数量

        // 计算当前饥饿条的进度，转换为图标数量
        float progress = (float) level / maxFoodLevel;
        int fullIcons = (int) (progress * iconsCount); // 完全填充的图标数量

        // 渲染 10 个图标
        for (int i = 0; i < iconsCount; ++i) {
            int x = left - i * 8 - 9;
            int y = top;
            int icon = 16;
            byte background = 0;

            // 饥饿状态下图标变化
            if (minecraft.player.hasEffect(MobEffects.HUNGER)) {
                icon += 36;
                background = 13;
            }

            // 饥饿图标位置微调
            if (player.getFoodData().getSaturationLevel() <= 0.0F && tickCount % (level * 3 + 1) == 0) {
                y = top + (random.nextInt(3) - 1);
            }

            // 渲染背景图标
            guiGraphics.blit(GUI_ICONS_LOCATION, x, y, 16 + background * 9, 27, 9, 9);

            // 渲染填充图标
            if (i < fullIcons) {
                // 完全填充的图标
                guiGraphics.blit(GUI_ICONS_LOCATION, x, y, icon + 36, 27, 9, 9);
            } else if (i == fullIcons && progress * iconsCount % 1 != 0) {
                // 部分填充的图标（如果进度为小数时）
                guiGraphics.blit(GUI_ICONS_LOCATION, x, y, icon + 45, 27, 9, 9);
            }
        }
        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }
}
