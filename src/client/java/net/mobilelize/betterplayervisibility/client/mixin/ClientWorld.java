package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.GameMode;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(net.minecraft.client.world.ClientWorld.class)
public class ClientWorld {

    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private static Set<Item> BLOCK_MARKER_ITEMS;

    @Inject(method = "getBlockParticle", at = @At("HEAD"), cancellable = true)
    private void getBlockParticle(CallbackInfoReturnable<Block> cir) {
        assert this.client.interactionManager != null;
        if (this.client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE) {
            assert this.client.player != null;
            ItemStack itemStack = this.client.player.getMainHandStack();
            Item item = itemStack.getItem();
            if (BLOCK_MARKER_ITEMS.contains(item) && item instanceof BlockItem blockItem) {
                if (item.equals(Items.BARRIER)) {
                    if (ConfigManager.configData.visibleBarrier && ConfigManager.configData.visibleBarrierParticles) {
                        cir.setReturnValue(blockItem.getBlock());
                    } else if (!ConfigManager.configData.visibleBarrier) {
                        cir.setReturnValue(blockItem.getBlock());
                    } else {
                        cir.setReturnValue(null);
                    }
                } else {
                    cir.setReturnValue(blockItem.getBlock());
                }
            }
            return;
        }
        cir.setReturnValue(null);
    }
}
