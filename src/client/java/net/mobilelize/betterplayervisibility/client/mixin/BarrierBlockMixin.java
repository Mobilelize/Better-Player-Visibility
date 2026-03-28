package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.world.level.block.BarrierBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrierBlock.class)
public class BarrierBlockMixin {
    @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
    private void makeBarrierVisible(BlockState state, CallbackInfoReturnable<RenderShape> cir) {
        if (ConfigManager.configData.visibleBarrier) cir.setReturnValue(RenderShape.MODEL);
    }
}
