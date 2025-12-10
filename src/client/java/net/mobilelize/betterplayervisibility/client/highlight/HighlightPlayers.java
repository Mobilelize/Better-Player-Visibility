package net.mobilelize.betterplayervisibility.client.highlight;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;
import java.util.Objects;

public class HighlightPlayers {

    public static void highlightNameArgs (Args args) {
        //State at args.get(0);
        if (!(args.get(0) instanceof PlayerEntityRenderState player)) return;
        if (player.displayName == null) return;
        String playerName = player.displayName.getString();

        BaseHighlight group = getGroupByPlayerName(playerName);
        if (group == null || !group.highlightEnabled) return;

        player.displayName = modifiedName(player.displayName, group);
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

    private static AbstractClientPlayerEntity getPlayerById(int id) {
        if (MinecraftClient.getInstance().world == null) return null;
        return MinecraftClient.getInstance().world.getPlayers().stream().filter(entry -> Objects.equals(entry.getId(), id)).findFirst().orElse(null);
    }

    public static Text modifiedName(Text displayText, BaseHighlight group) {
        MutableText modifiedText = Text.empty();
        if (group.identifierMode == HighlightPlayerModeOption.OFF) {
            return displayText;
        }

        boolean both = group.identifierMode == HighlightPlayerModeOption.BOTH;

        if (group.identifierMode == HighlightPlayerModeOption.TAG || both) {
            modifiedText.append(Text.literal(group.tag).withColor(group.identifierColor)).append(" ");
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
