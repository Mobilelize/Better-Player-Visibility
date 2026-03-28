package net.mobilelize.betterplayervisibility.client.visibility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.highlight.BaseHighlight;
import net.mobilelize.betterplayervisibility.client.highlight.HighlightPlayers;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayerVisibility {

    public static boolean shouldBeInvisible(AbstractClientPlayer player) {

        boolean reverse = ConfigManager.configData.reversedVisibility;

        if (ConfigManager.configData.invisibleMainPlayer && player.isLocalPlayer()) return true;

        if (ConfigManager.configData.showAllPlayers || player.isLocalPlayer() || isAnNPC(player)
                || (!reverse && ConfigManager.configData.visibility.equals(EnumsVisibility.ALL))
                || (reverse && ConfigManager.configData.visibility.equals(EnumsVisibility.NONE))) return false;

        return reverse != shouldBeInvisibleResult(player);
    }

    private static boolean shouldBeInvisibleResult(AbstractClientPlayer player) {

        if (notInRadius(player)) return false;

        String playerName = player.getGameProfile().name();

        if (ConfigManager.configData.visibility == EnumsVisibility.NONE) {
            return true;
        }

        for (Map.Entry<EnumsVisibility, List<String>> entry : getGroupMap().entrySet()) {
            if (ConfigManager.configData.visibility == entry.getKey()) {
                return listDoesNotContainName(entry.getValue(), playerName);
            }
        }

        for (BaseHighlight group : HighlightPlayers.getGroupList()) {
            if (ConfigManager.configData.visibility == group.group) {
                return listDoesNotContainName(group.list, playerName);
            }
        }

        return false;
    }

    public static boolean shouldBeInvisibleById(int id) {
        if (Minecraft.getInstance().level == null) return false;
        AbstractClientPlayer abstractClientPlayerEntity = Minecraft.getInstance().level.players().stream().filter(entry -> Objects.equals(entry.getId(), id)).findFirst().orElse(null);
        if (abstractClientPlayerEntity == null) return false;
        return shouldBeInvisible(abstractClientPlayerEntity);
    }

    private static boolean notInRadius(AbstractClientPlayer player) {
        if (!ConfigManager.configData.visibilityRadiusEnabled) return false;
        assert Minecraft.getInstance().player != null;
        return !player.closerThan(Minecraft.getInstance().player, ConfigManager.configData.visibilityRadius);
    }

    private static boolean listDoesNotContainName(List<String> list, String name) {
        return list.stream().noneMatch(s -> s.equalsIgnoreCase(name));
    }

    public static Map<EnumsVisibility, List<String>> getGroupMap() {
        return Map.of(
                EnumsVisibility.WHITELIST, ConfigManager.configData.visibilityList,
                EnumsVisibility.WHITELIST_2, ConfigManager.configData.visibilityList2,
                EnumsVisibility.WHITELIST_3, ConfigManager.configData.visibilityList3,
                EnumsVisibility.WHITELIST_4, ConfigManager.configData.visibilityList4,
                EnumsVisibility.WHITELIST_5, ConfigManager.configData.visibilityList5);
    }

    public static boolean isAnNPC(AbstractClientPlayer player) {

        if (!ConfigManager.configData.visibilityNPCEnabled)
            return false;

        String name = player.getGameProfile().name();

        if (name.length() > 16) return true;
        if (ConfigManager.configData.visibilityNPC3Characters && name.length() < 3) return true;

        if (!name.matches("^[a-zA-Z0-9_]{3,16}$")) return true;   // ← this catches ANY forbidden character

        // ---- 2. Check tab list presence ----
        if (ConfigManager.configData.visibilityNPCTabListEnabled) {
            if (Minecraft.getInstance().getConnection() == null) return false;
            PlayerInfo entry = Minecraft.getInstance()
                    .getConnection()
                    .getPlayerInfo(player.getGameProfile().id());

            return entry == null;
        }

        return false;
    }

}
