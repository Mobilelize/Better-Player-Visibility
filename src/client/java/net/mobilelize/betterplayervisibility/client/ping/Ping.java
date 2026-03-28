package net.mobilelize.betterplayervisibility.client.ping;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class Ping {

    public static void pingNameArgs (Args args) {
        if (!ConfigManager.configData.showPing) return;
        //State at args.get(0);
        if (!(args.get(0) instanceof AvatarRenderState player)) return;

        //Display name at args.get(1);
        if (player.nameTag == null) return;
        int ping = getPlayersPingById(player.id);
        BasePing group = getPingGroup(ping);

        player.nameTag = modifiedName(player.nameTag, group, ping);
        args.set(0, player);
    }

    public static int getPlayersPing(UUID uuid) {
        if (Minecraft.getInstance().getConnection() == null || uuid == null) return -1;
        return Minecraft.getInstance().getConnection().getOnlinePlayers().stream().filter(entry -> entry.getProfile().id().equals(uuid)).map(PlayerInfo::getLatency).findFirst().orElse(-1);
    }

    public static int getPlayersPingByName(String name) {
        if (Minecraft.getInstance().getConnection() == null) return -1;
        return getPlayersPing(Minecraft.getInstance().getConnection().getOnlinePlayers().stream().map(PlayerInfo::getProfile).filter(profile -> profile.name().equalsIgnoreCase(name)).map(GameProfile::id).findFirst().orElse(null));
    }

    public static int getPlayersPingById(int id) {
        if (Minecraft.getInstance().level == null) return -1;
        AbstractClientPlayer abstractClientPlayerEntity = Minecraft.getInstance().level.players().stream().filter(entry -> Objects.equals(entry.getId(), id)).findFirst().orElse(null);
        if (abstractClientPlayerEntity == null) return -1;
        return getPlayersPing(abstractClientPlayerEntity.getUUID());
    }

    public static Component pingFormatted(int ping) {
        BasePing pingGroup = getPingGroup(ping);

        if (!ConfigManager.configData.useDefaultPingText || pingGroup.ping == EnumsPing.NO) {
            return Component.literal(pingGroup.text.replace("%ping%", String.valueOf(ping))).setStyle(Style.EMPTY.withColor(pingGroup.color));
        }

        return Component.literal(ConfigManager.configData.defaultPingText.replace("%ping%", String.valueOf(ping))).setStyle(Style.EMPTY.withColor(pingGroup.color));
    }

    public static BasePing getPingGroup(int ping) {
        for (BasePing group : getPingGroupList()) {
            if (ping >= group.range) {
                return group;
            }
        }
        return ConfigManager.configData.noPing;
    }

    public static List<BasePing> getPingGroupList() {
        return Stream.of(
                ConfigManager.configData.worstPing, ConfigManager.configData.worsePing, ConfigManager.configData.badPing,
                ConfigManager.configData.poorPing, ConfigManager.configData.averagePing, ConfigManager.configData.decentPing,
                ConfigManager.configData.goodPing, ConfigManager.configData.veryGoodPing, ConfigManager.configData.excellentPing,
                ConfigManager.configData.noPing)
                .sorted(Comparator.comparingInt(s -> s.index))
                .toList();
    }

    public static Component modifiedName(Component displayText, BasePing group, int ping) {

        if (group.ping == EnumsPing.NO && !ConfigManager.configData.showNoPing) {
            return displayText;
        }

        return Component.empty().append(displayText).append(" ").append(pingFormatted(ping));
    }
}
