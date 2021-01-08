package me.lortseam.uglyscoreboardfix.mixin;

import me.lortseam.uglyscoreboardfix.Config;
import me.lortseam.uglyscoreboardfix.HidePart;
import me.lortseam.uglyscoreboardfix.SidebarPosition;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique
    private int xShift;

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void uglyscoreboardfix$modifySidebar(MatrixStack matrices, ScoreboardObjective objective, CallbackInfo ci) {
        if (Config.getInstance().shouldHide(HidePart.SIDEBAR, objective)) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At("STORE"))
    private String uglyscoreboardfix$modifyScore(String score, MatrixStack matrices, ScoreboardObjective objective) {
        return Config.getInstance().shouldHide(HidePart.SCORES, objective) ? "" : score;
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At("STORE"), ordinal = 2)
    private int uglyscoreboardfix$modifySeperatorWidth(int seperatorWidth, MatrixStack matrices, ScoreboardObjective objective) {
        return Config.getInstance().shouldHide(HidePart.SCORES, objective) ? 0 : seperatorWidth;
    }

    @Redirect(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Ljava/lang/String;)I", ordinal = 1))
    private int uglyscoreboardfix$modifyScoreWidth(TextRenderer textRenderer, String score, MatrixStack matrices, ScoreboardObjective objective) {
        return Config.getInstance().shouldHide(HidePart.SCORES, objective) ? 0 : textRenderer.getWidth(score);
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At("STORE"), ordinal = 5)
    private int uglyscoreboardfix$modifyX1(int x1) {
        if (Config.getInstance().getPosition() == SidebarPosition.LEFT) {
            xShift = x1;
            return 2;
        }
        return x1;
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At("STORE"), ordinal = 11)
    private int uglyscoreboardfix$modifyX2(int x2) {
        if (Config.getInstance().getPosition() == SidebarPosition.LEFT) {
            return x2 - xShift;
        }
        return x2;
    }

}