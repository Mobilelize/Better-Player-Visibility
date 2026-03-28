package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.mobilelize.betterplayervisibility.client.priority.Priority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "handlePlayerInfoRemove", at = @At("TAIL"))
    public void onPlayerRemove(ClientboundPlayerInfoRemovePacket packet, CallbackInfo ci) {
        Priority.removePriorityCache(packet.profileIds());
    }

    @Inject(method = "handleLogin", at = @At("TAIL"))
    public void onGameJoin(ClientboundLoginPacket packet, CallbackInfo ci) {
        Priority.clearCache();
    }
}
