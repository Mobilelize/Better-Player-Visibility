package net.mobilelize.betterplayervisibility.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.priority.Priority;
import net.mobilelize.betterplayervisibility.client.visibility.PlayerVisibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    @Shadow public abstract <S extends EntityRenderState> EntityRenderer<?, ? super S> getRenderer(S state);

    @Inject(method = "submit", at = @At("HEAD"), cancellable = true)
    private <S extends EntityRenderState> void hidePlayers(S renderState, CameraRenderState cameraRenderState, double d, double e, double f, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, CallbackInfo ci) {
        if (renderState instanceof AvatarRenderState player){
            Priority.addPriorityCache(player.id);
            if (!ConfigManager.configData.visibilityChangeSizeEnabled && PlayerVisibility.shouldBeInvisibleById(player.id)) {
                ci.cancel();
            }
        }
    }
}
