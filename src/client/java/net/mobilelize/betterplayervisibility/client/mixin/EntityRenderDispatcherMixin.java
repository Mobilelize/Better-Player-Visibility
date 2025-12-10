package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.priority.Priority;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.PlayerVisibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void hidePlayers(E entity, double x, double y, double z, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity instanceof AbstractClientPlayerEntity player){
            if (!player.isMainPlayer()) Priority.addPriorityCache(player);
            if (!ConfigManager.configData.visibilityChangeSizeEnabled && PlayerVisibility.shouldBeInvisible(player)) {
                ci.cancel();
            }
        }
        if (EntitiesVisibility.shouldBeInvisible(entity)) ci.cancel();
    }
}
