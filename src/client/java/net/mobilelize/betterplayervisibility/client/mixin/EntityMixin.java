package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.visibility.PlayerVisibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract EntityType<?> getType();

    @Shadow public abstract int getId();

    @Inject(method = "spawnSprintingParticles", at = @At("HEAD"), cancellable = true)
    public void spawnSprintingParticles(CallbackInfo ci){
        if (!ConfigManager.configData.visibilitySpawnSprintingParticles && getType() == EntityType.PLAYER && PlayerVisibility.shouldBeInvisibleById(getId())){
            ci.cancel();
        }
    }

    @Inject(method = "doesRenderOnFire", at = @At("HEAD"), cancellable = true)
    public void doesRenderOnFire(CallbackInfoReturnable<Boolean> cir){
        if (!ConfigManager.configData.visibilityShowFire && getType() == EntityType.PLAYER && PlayerVisibility.shouldBeInvisibleById(getId())){
            cir.setReturnValue(false);
        }
    }
}
