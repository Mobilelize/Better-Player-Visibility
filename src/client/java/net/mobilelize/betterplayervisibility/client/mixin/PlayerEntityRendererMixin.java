package net.mobilelize.betterplayervisibility.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.visibility.PlayerVisibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("HEAD"), cancellable = true )
    protected void renderLabelIfPresent(AvatarRenderState playerEntityRenderState, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (!ConfigManager.configData.visibilityNameTagEnabled && PlayerVisibility.shouldBeInvisibleById(playerEntityRenderState.id)){
            ci.cancel();
        }
    }

    @Inject(method = "scale(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At("HEAD"), cancellable = true)
    public void scale(AvatarRenderState playerEntityRenderState, PoseStack matrixStack, CallbackInfo ci){
        if (PlayerVisibility.shouldBeInvisibleById(playerEntityRenderState.id) && ConfigManager.configData.visibilityChangeSizeEnabled) {
            //float base = 0.9375F;
            //prevents them from making them any bigger :)
            float h = Math.min(ConfigManager.configData.visibilityChangeSize, 0.9375F);
            float g = Math.max(h, 0);
            matrixStack.scale(g, g, g);
            ci.cancel();
        }
    }
}
