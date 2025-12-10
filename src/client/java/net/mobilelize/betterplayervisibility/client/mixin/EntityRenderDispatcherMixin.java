package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.mobilelize.betterplayervisibility.client.BetterPlayerVisibilityClient;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.priority.Priority;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.PlayerVisibility;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

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
}
