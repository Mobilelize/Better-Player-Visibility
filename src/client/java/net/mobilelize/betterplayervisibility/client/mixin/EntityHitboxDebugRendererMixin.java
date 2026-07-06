package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.debug.EntityHitboxDebugRenderer;
import net.minecraft.entity.Entity;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.visibility.PlayerVisibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHitboxDebugRenderer.class)
public abstract class EntityHitboxDebugRendererMixin {

    @Inject(method = "drawHitbox", at = @At("HEAD"), cancellable = true)
    private void hideInvisibleHitboxes(Entity entity, float tickProgress, boolean inLocalServer, CallbackInfo ci) {
        if (!ConfigManager.configData.visibilityShowHitboxes
                && entity instanceof AbstractClientPlayerEntity
                && PlayerVisibility.shouldBeInvisibleById(entity.getId())) {
            ci.cancel();
        }
    }
}
