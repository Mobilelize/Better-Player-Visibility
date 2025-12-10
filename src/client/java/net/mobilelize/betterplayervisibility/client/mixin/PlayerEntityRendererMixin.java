package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.visibility.PlayerVisibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At("HEAD"), cancellable = true )
    protected void renderLabelIfPresent(PlayerEntityRenderState playerEntityRenderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (!ConfigManager.configData.visibilityNameTagEnabled && PlayerVisibility.shouldBeInvisibleById(playerEntityRenderState.id)){
            ci.cancel();
        }
    }

    @Inject(method = "scale(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("HEAD"), cancellable = true)
    public void scale(PlayerEntityRenderState playerEntityRenderState, MatrixStack matrixStack, CallbackInfo ci){
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
