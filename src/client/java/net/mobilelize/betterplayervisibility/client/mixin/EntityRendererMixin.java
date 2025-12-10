package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.highlight.HighlightPlayers;
import net.mobilelize.betterplayervisibility.client.ping.Ping;
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
    public <T extends Entity> void getShadowRadius(T entity, CallbackInfoReturnable<Float> cir) {
        if (entity instanceof AbstractClientPlayerEntity player) {
            if (PlayerVisibility.shouldBeInvisible(player) && !ConfigManager.configData.visibilityShowShadows) {
                cir.setReturnValue(0f);
            }
        }
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V"))
    public <T extends Entity> void renderLabelIfPresent(Args args) {
        //Args.get(0) = Entity
        //Args.get(1) = Name
        HighlightPlayers.highlightNameArgs(args);
        Ping.pingNameArgs(args);
    }
}
