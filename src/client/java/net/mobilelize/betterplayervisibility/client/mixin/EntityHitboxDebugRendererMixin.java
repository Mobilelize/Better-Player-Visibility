package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.world.entity.Entity;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.PlayerVisibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHitboxDebugRenderer.class)
public abstract class EntityHitboxDebugRendererMixin {

    @Inject(method = "showHitboxes", at = @At("HEAD"), cancellable = true)
    private void hideInvisibleHitboxes(Entity entity, float tickProgress, boolean inLocalServer, CallbackInfo ci) {
        if (entity instanceof AbstractClientPlayer) {
            if (!ConfigManager.configData.visibilityShowHitboxes && PlayerVisibility.shouldBeInvisibleById(entity.getId())) {
                ci.cancel();
            }
        } else if (!ConfigManager.configData.entitiesVisibilityShowHitboxes && EntitiesVisibility.shouldBeInvisible(entity)) {
            ci.cancel();
        }
    }
}
