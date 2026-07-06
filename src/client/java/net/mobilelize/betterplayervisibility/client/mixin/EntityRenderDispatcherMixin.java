package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityHitboxAndView;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.priority.Priority;
import net.mobilelize.betterplayervisibility.client.visibility.PlayerVisibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderManager.class)
public abstract class EntityRenderDispatcherMixin {

    @Shadow public abstract <S extends EntityRenderState> EntityRenderer<?, ? super S> getRenderer(S state);

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <S extends EntityRenderState> void hidePlayers(S renderState, CameraRenderState cameraRenderState, double d, double e, double f, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CallbackInfo ci) {
        if (renderState instanceof PlayerEntityRenderState player){
            Priority.addPriorityCache(player.id);
            if (!ConfigManager.configData.visibilityChangeSizeEnabled && PlayerVisibility.shouldBeInvisibleById(player.id)) {
                ci.cancel();
            }
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitDebugHitbox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/entity/state/EntityHitboxAndView;)V"))
    private <S extends EntityRenderState> void hideInvisibleHitboxes(OrderedRenderCommandQueue queue, MatrixStack matrixStack, EntityRenderState renderState, EntityHitboxAndView hitbox) {
        if (!ConfigManager.configData.visibilityShowHitboxes && renderState instanceof PlayerEntityRenderState player && PlayerVisibility.shouldBeInvisibleById(player.id)) {
            return;
        }
        queue.submitDebugHitbox(matrixStack, renderState, hitbox);
    }
}
