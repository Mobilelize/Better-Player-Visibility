package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(net.minecraft.client.multiplayer.ClientLevel.class)
public class ClientWorld {

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private static Set<Item> MARKER_PARTICLE_ITEMS;

    @Inject(method = "getMarkerParticleTarget", at = @At("HEAD"), cancellable = true)
    private void getBlockParticle(CallbackInfoReturnable<Block> cir) {
        if (this.minecraft.gameMode.getPlayerMode() == GameType.CREATIVE) {
            ItemStack itemStack = this.minecraft.player.getMainHandItem();
            Item item = itemStack.getItem();
            if (MARKER_PARTICLE_ITEMS.contains(item) && item instanceof BlockItem blockItem) {
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
