package net.mobilelize.betterplayervisibility.client.ping;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
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
        if (!(args.get(0) instanceof PlayerEntityRenderState player)) return;

        //Display name at args.get(1);
        if (!(args.get(1) instanceof Text displayText)) return;
        int ping = getPlayersPingById(player.id);
        BasePing group = getPingGroup(ping);

        Text text = modifiedName(displayText, group, ping);
        args.set(1, text);
    }

    public static int getPlayersPing(UUID uuid) {
        if (MinecraftClient.getInstance().getNetworkHandler() == null || uuid == null) return -1;
        return MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream().filter(entry -> entry.getProfile().getId().equals(uuid)).map(PlayerListEntry::getLatency).findFirst().orElse(-1);
    }

    public static int getPlayersPingByName(String name) {
        if (MinecraftClient.getInstance().getNetworkHandler() == null) return -1;
        return getPlayersPing(MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream().map(PlayerListEntry::getProfile).filter(profile -> profile.getName().equalsIgnoreCase(name)).map(GameProfile::getId).findFirst().orElse(null));
    }

    public static int getPlayersPingById(int id) {
        if (MinecraftClient.getInstance().world == null) return -1;
        AbstractClientPlayerEntity abstractClientPlayerEntity = MinecraftClient.getInstance().world.getPlayers().stream().filter(entry -> Objects.equals(entry.getId(), id)).findFirst().orElse(null);
        if (abstractClientPlayerEntity == null) return -1;
        return getPlayersPing(abstractClientPlayerEntity.getUuid());
    }

    public static Text pingFormatted(int ping) {
        BasePing pingGroup = getPingGroup(ping);

        if (!ConfigManager.configData.useDefaultPingText || pingGroup.ping == EnumsPing.NO) {
            return Text.literal(pingGroup.text.replace("%ping%", String.valueOf(ping))).setStyle(Style.EMPTY.withColor(pingGroup.color));
        }

        return Text.literal(ConfigManager.configData.defaultPingText.replace("%ping%", String.valueOf(ping))).setStyle(Style.EMPTY.withColor(pingGroup.color));
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

    public static Text modifiedName(Text displayText, BasePing group, int ping) {

        if (group.ping == EnumsPing.NO && !ConfigManager.configData.showNoPing) {
            return displayText;
        }

        return Text.empty().append(displayText).append(" ").append(pingFormatted(ping));
    }
}
