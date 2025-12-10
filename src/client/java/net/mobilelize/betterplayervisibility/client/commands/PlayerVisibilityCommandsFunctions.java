package net.mobilelize.betterplayervisibility.client.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.mobilelize.betterplayervisibility.client.BetterPlayerVisibilityClient;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.config.ConfigMenu;
import net.mobilelize.betterplayervisibility.client.highlight.BaseHighlight;
import net.mobilelize.betterplayervisibility.client.highlight.HighlightPlayers;
import net.mobilelize.betterplayervisibility.client.utils.TextFormatter;
import net.mobilelize.betterplayervisibility.client.visibility.EnumsVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.PlayerVisibility;

import java.util.List;
import java.util.Map;

public class PlayerVisibilityCommandsFunctions {
    public static int openConfig(CommandContext<FabricClientCommandSource> context){
        MinecraftClient client = MinecraftClient.getInstance();
        new Thread(() -> client.execute(() -> client.setScreen(ConfigMenu.create(null)))).start();
        return 1;
    }

    public static int nothing(CommandContext<FabricClientCommandSource> context){
        FabricClientCommandSource source = context.getSource();
        return 0;
    }

    public static int nothing(CommandContext<FabricClientCommandSource> context, String message){
        FabricClientCommandSource source = context.getSource();
        MutableText text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText(message));
        source.sendFeedback(text);
        return 0;
    }

    public static int showAll(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        boolean value = context.getArgument("boolean", Boolean.class);

        // If already set to this value, notify
        if (ConfigManager.configData.showAllPlayers == value) {
            Text text = Text.empty()
                    .append(BetterPlayerVisibilityClient.PREFIX)
                    .append(TextFormatter.formatText(
                            "&3Show All Players &bis already " + (value ? "&aON" : "&cOFF") + "&3."
                    ));
            source.sendFeedback(text);
            return 0;
        }

        // Update the value
        ConfigManager.configData.showAllPlayers = value;

        Text text = Text.empty()
                .append(BetterPlayerVisibilityClient.PREFIX)
                .append(TextFormatter.formatText(
                        "&3Show All Players &bhas been set to " + (value ? "&aON" : "&cOFF") + "&3."
                ));
        source.sendFeedback(text);

        ConfigManager.saveConfig();
        return 1;
    }

    public static int reverseVisibility(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        boolean value = context.getArgument("boolean", Boolean.class);

        // If already set to this value, notify
        if (ConfigManager.configData.reversedVisibility == value) {
            Text text = Text.empty()
                    .append(BetterPlayerVisibilityClient.PREFIX)
                    .append(TextFormatter.formatText(
                            "&3Reverse Players Visibility &bis already " + (value ? "&aON" : "&cOFF") + "&3."
                    ));
            source.sendFeedback(text);
            return 0;
        }

        // Update the value
        ConfigManager.configData.reversedVisibility = value;

        Text text = Text.empty()
                .append(BetterPlayerVisibilityClient.PREFIX)
                .append(TextFormatter.formatText(
                        "&3Reverse Players Visibility &bhas been set to " + (value ? "&aON" : "&cOFF") + "&3."
                ));
        source.sendFeedback(text);

        ConfigManager.saveConfig();
        return 1;
    }


    public static int toggleAll(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        if (ConfigManager.configData.visibility == EnumsVisibility.ALL) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Player Visibility Mode &bis already set to &eALL&3."));
            source.sendFeedback(text);
            return 0;
        } else {
            ConfigManager.configData.visibility = EnumsVisibility.ALL;
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Player Visibility &bhas been set to &eALL&3."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int toggleNone(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        if (ConfigManager.configData.visibility == EnumsVisibility.NONE) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Player Visibility &bis already set to &eNONE&3."));
            source.sendFeedback(text);
            return 0;
        } else {
            ConfigManager.configData.visibility = EnumsVisibility.NONE;
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Player Visibility &bhas been set to &eNONE&3."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int radiusOn(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        if (ConfigManager.configData.visibilityRadiusEnabled) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Player Visibility Radius &bis already &eon&3."));
            source.sendFeedback(text);
            return 0;
        } else {
            ConfigManager.configData.visibilityRadiusEnabled = true;
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Player Visibility Radius &bhas been turned &eon&3."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int radiusOff(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        if (!ConfigManager.configData.visibilityRadiusEnabled) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Player Visibility Radius &bis already &eoff&3."));
            source.sendFeedback(text);
            return 0;
        } else {
            ConfigManager.configData.visibilityRadiusEnabled = false;
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Player Visibility Radius has been turned &eoff&3."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int radiusSet(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        int number = context.getArgument("number", Integer.class);

        ConfigManager.configData.visibilityRadius = number;
        Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Player Visibility Radius Range &bhas been set has to &e" + number  + "&3."));
        source.sendFeedback(text);

        ConfigManager.saveConfig();
        return 1;
    }

    public static int sizeOn(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        if (ConfigManager.configData.visibilityChangeSizeEnabled) {
            Text text = Text.empty().append(Text.empty().append(BetterPlayerVisibilityClient.PREFIX)).append(TextFormatter.formatText("&3Player Visibility Size &bis already &eon&3."));
            source.sendFeedback(text);
            return 0;
        } else {
            ConfigManager.configData.visibilityChangeSizeEnabled = true;
            Text text = Text.empty().append(Text.empty().append(BetterPlayerVisibilityClient.PREFIX)).append(TextFormatter.formatText("&3Player Visibility Size &bhas been turned &eon&3."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int sizeOff(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        if (!ConfigManager.configData.visibilityChangeSizeEnabled) {
            Text text = Text.empty().append(Text.empty().append(BetterPlayerVisibilityClient.PREFIX)).append(TextFormatter.formatText("&3Player Visibility Size &bis already &eoff&3."));
            source.sendFeedback(text);
            return 0;
        } else {
            ConfigManager.configData.visibilityChangeSizeEnabled = false;
            Text text = Text.empty().append(Text.empty().append(BetterPlayerVisibilityClient.PREFIX)).append(TextFormatter.formatText("&3Player Visibility Size &bhas been turned &eoff&3."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int sizeSet(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        float number = context.getArgument("number", Float.class);

        ConfigManager.configData.visibilityChangeSize = number;
        Text text = Text.empty().append(Text.empty().append(BetterPlayerVisibilityClient.PREFIX)).append(TextFormatter.formatText("&3Player Visibility Size &bhas been set to &e" + number  + "&3."));
        source.sendFeedback(text);

        ConfigManager.saveConfig();
        return 1;
    }

    public static int toggleList(CommandContext<FabricClientCommandSource> context, EnumsVisibility currentVis) {
        FabricClientCommandSource source = context.getSource();

        if (ConfigManager.configData.visibility == currentVis) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Player Visibility &bis already set to &e" + currentVis.name() + "&3."));
            source.sendFeedback(text);
            return 0;
        } else {
            ConfigManager.configData.visibility = currentVis;
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Player Visibility &bhas been set to &e" + currentVis.name() + "&3."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int toggleAdd(CommandContext<FabricClientCommandSource> context, EnumsVisibility currentVis) {
        FabricClientCommandSource source = context.getSource();

        boolean inList = false;
        String name = context.getArgument("name", String.class);

        for (Map.Entry<EnumsVisibility, List<String>> entry : PlayerVisibility.getGroupMap().entrySet()) {
            if (currentVis == entry.getKey()) {
                if (entry.getValue().stream().anyMatch(s -> s.equalsIgnoreCase(name))) {
                    inList = true;
                    break;
                }
                entry.getValue().add(name);
                break;
            }
        }

        for (BaseHighlight group : HighlightPlayers.getGroupList()) {
            if (currentVis == group.group) {
                if (group.list.stream().anyMatch(s -> s.equalsIgnoreCase(name))) {
                    inList = true;
                    break;
                }
                group.list.add(name);
                break;
            }
        }

        if (inList) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&c" + name + " &bis already in the &e" + currentVis.name() + " &3Player list."));
            source.sendFeedback(text);
            return 0;
        } else {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&a" + name + " &bhas been added to the &e" + currentVis.name() + " &3Player list."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int toggleRemove(CommandContext<FabricClientCommandSource> context, EnumsVisibility currentVis) {
        FabricClientCommandSource source = context.getSource();

        boolean inList = false;
        String name = context.getArgument("name", String.class);

        for (Map.Entry<EnumsVisibility, List<String>> entry : PlayerVisibility.getGroupMap().entrySet()) {
            if (currentVis == entry.getKey()) {
                inList = entry.getValue().removeIf(s -> s.equalsIgnoreCase(name));
                break;
            }
        }

        for (BaseHighlight group : HighlightPlayers.getGroupList()) {
            if (currentVis.equals(group.group)) {
                inList = group.list.removeIf(s -> s.equalsIgnoreCase(name));
                break;
            }
        }

        if (!inList) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&c" + name + " &3isn't in the &e" + currentVis.name() + " &3list."));
            source.sendFeedback(text);
            return 0;
        } else {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&a" + name + " &3has been removed from the &e" + currentVis.name() + " &3list."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int toggleClear(CommandContext<FabricClientCommandSource> context, EnumsVisibility currentVis) {
        FabricClientCommandSource source = context.getSource();

        for (Map.Entry<EnumsVisibility, List<String>> entry : PlayerVisibility.getGroupMap().entrySet()) {
            if (currentVis == entry.getKey()) {
                entry.getValue().clear();
                break;
            }
        }

        for (BaseHighlight group : HighlightPlayers.getGroupList()) {
            if (currentVis.equals(group.group)) {
                group.list.clear();
                break;
            }
        }

        Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&e" + currentVis.name() + " &3has been cleared&3."));
        source.sendFeedback(text);
        ConfigManager.saveConfig();
        return 1;
    }

    public static int toggleView(CommandContext<FabricClientCommandSource> context, EnumsVisibility currentVis) {
        FabricClientCommandSource source = context.getSource();

        List<String> currentList = List.of();

        for (Map.Entry<EnumsVisibility, List<String>> entry : PlayerVisibility.getGroupMap().entrySet()) {
            if (currentVis == entry.getKey()) {
                currentList = entry.getValue();
                break;
            }
        }

        for (BaseHighlight group : HighlightPlayers.getGroupList()) {
            if (currentVis.equals(group.group)) {
                currentList = group.list;
                break;
            }
        }

        StringBuilder entireList = new StringBuilder();

        entireList.append("&3").append(currentVis.name()).append(" List (&f").append(currentList.size()).append("&3).\n");

        if (currentList.isEmpty()) {
            entireList.append("&3There are no players in your &e").append(currentVis.name()).append(" list&3.12");
        }

        for (String name : currentList) {
            entireList.append("&b").append(name).append("&8, ");
        }

        source.sendFeedback(Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText(entireList.substring(0, entireList.toString().length() - 2))));
        return 1;
    }
}
