package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Entity;
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
        if (state instanceof AvatarRenderState player) {
            if (PlayerVisibility.shouldBeInvisibleById(player.id) && !ConfigManager.configData.visibilityShowShadows) {
                cir.setReturnValue(0f);
            }
        }
    }

    @ModifyArgs(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"))
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
