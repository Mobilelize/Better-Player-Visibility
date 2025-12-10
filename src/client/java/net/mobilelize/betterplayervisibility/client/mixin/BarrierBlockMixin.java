package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.block.BarrierBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrierBlock.class)
public class BarrierBlockMixin {
    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void makeBarrierVisible(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (ConfigManager.configData.visibleBarrier) cir.setReturnValue(BlockRenderType.MODEL);
    }
}

