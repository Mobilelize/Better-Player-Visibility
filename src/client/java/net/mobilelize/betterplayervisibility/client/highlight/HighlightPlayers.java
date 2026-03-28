package net.mobilelize.betterplayervisibility.client.highlight;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.mobilelize.betterplayervisibility.client.BetterPlayerVisibilityClient;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;
import java.util.Objects;

public class HighlightPlayers {

    public static void highlightNameArgs (Args args) {
        //State at args.get(0);
        if (!(args.get(0) instanceof AvatarRenderState player)) return;
        if (player.nameTag == null) return;
        AbstractClientPlayer playerEntity = getPlayerById(player.id);
        if (playerEntity == null) return;
        String playerName = playerEntity.getName().getString();

        BaseHighlight group = getGroupByPlayerName(playerName);
        if (group == null || !group.highlightEnabled) return;

        player.nameTag = modifiedName(player.nameTag, group);
        args.set(0, player);
    }

    public static BaseHighlight getGroupByPlayerName(String name) {
        for (BaseHighlight group : getGroupList()) {
            if (group.list.stream().anyMatch(s -> s.equalsIgnoreCase(name))) {
                return group;
            }
        }
        return null;
    }

    private static AbstractClientPlayer getPlayerById(int id) {
        if (Minecraft.getInstance().level == null) return null;
        return Minecraft.getInstance().level.players().stream().filter(entry -> Objects.equals(entry.getId(), id)).findFirst().orElse(null);
    }

    public static Component modifiedName(Component displayText, BaseHighlight group) {
        MutableComponent modifiedText = Component.empty();
        if (group.identifierMode == HighlightPlayerModeOption.OFF) {
            return displayText;
        }

        boolean both = group.identifierMode == HighlightPlayerModeOption.BOTH;

        if (group.identifierMode == HighlightPlayerModeOption.TAG || both) {
            modifiedText.append(Component.literal(group.tag).withColor(group.identifierColor)).append(" ");
        }

        if (group.identifierMode == HighlightPlayerModeOption.NAME || both) {
            displayText = displayText.copy().withColor(group.identifierColor);
        }

        modifiedText.append(displayText);

        return modifiedText;
    }

    public static List<BaseHighlight> getGroupList() {
        return List.of(ConfigManager.configData.staff, ConfigManager.configData.allies, ConfigManager.configData.enemies, ConfigManager.configData.highlight, ConfigManager.configData.highlight2, ConfigManager.configData.highlight3, ConfigManager.configData.highlight4, ConfigManager.configData.highlight5);
    }
}
