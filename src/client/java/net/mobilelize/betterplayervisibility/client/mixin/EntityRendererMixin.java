package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.Entity;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.highlight.HighlightPlayers;
import net.mobilelize.betterplayervisibility.client.ping.Ping;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.PlayerVisibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Inject(method = "getShadowRadius", at = @At("HEAD"), cancellable = true)
    public <S extends EntityRenderState> void getShadowRadius(S state, CallbackInfoReturnable<Float> cir) {
        if (state instanceof PlayerEntityRenderState player) {
            if (PlayerVisibility.shouldBeInvisibleById(player.id) && !ConfigManager.configData.visibilityShowShadows) {
                cir.setReturnValue(0f);
            }
        }
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V"))
    public <T extends Entity> void renderLabelIfPresent(Args args) {
        //Args.get(0) = State
        HighlightPlayers.highlightNameArgs(args);
        Ping.pingNameArgs(args);
    }

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    public <T extends Entity> void shouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (EntitiesVisibility.shouldBeInvisible(entity)) cir.setReturnValue(false);
    }
}
