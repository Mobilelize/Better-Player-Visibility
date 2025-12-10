package net.mobilelize.betterplayervisibility.client.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.mobilelize.betterplayervisibility.client.BetterPlayerVisibilityClient;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.utils.TextFormatter;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesEnumsVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesVisibility;

import java.util.List;
import java.util.Map;

public class EntityVisibilityCommandsFunctions {

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
        if (ConfigManager.configData.showAllEntities == value) {
            Text text = Text.empty()
                    .append(BetterPlayerVisibilityClient.PREFIX)
                    .append(TextFormatter.formatText(
                            "&3Show All Entities &bis already " + (value ? "&aON" : "&cOFF") + "&3."
                    ));
            source.sendFeedback(text);
            return 0;
        }

        // Update the value
        ConfigManager.configData.showAllEntities = value;

        Text text = Text.empty()
                .append(BetterPlayerVisibilityClient.PREFIX)
                .append(TextFormatter.formatText(
                        "&3Show All Entities &bhas been set to " + (value ? "&aON" : "&cOFF") + "&3."
                ));
        source.sendFeedback(text);

        ConfigManager.saveConfig();
        return 1;
    }

    public static int reverseVisibility(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        boolean value = context.getArgument("boolean", Boolean.class);

        // If already set to this value, notify
        if (ConfigManager.configData.reversedEntitiesVisibility == value) {
            Text text = Text.empty()
                    .append(BetterPlayerVisibilityClient.PREFIX)
                    .append(TextFormatter.formatText(
                            "&3Reverse Entities Visibility &bis already " + (value ? "&aON" : "&cOFF") + "&3."
                    ));
            source.sendFeedback(text);
            return 0;
        }

        // Update the value
        ConfigManager.configData.reversedEntitiesVisibility = value;

        Text text = Text.empty()
                .append(BetterPlayerVisibilityClient.PREFIX)
                .append(TextFormatter.formatText(
                        "&3Reverse Entities Visibility &bhas been set to " + (value ? "&aON" : "&cOFF") + "&3."
                ));
        source.sendFeedback(text);

        ConfigManager.saveConfig();
        return 1;
    }

    public static int toggleAll(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        if (ConfigManager.configData.entitiesVisibility == EntitiesEnumsVisibility.ALL) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Entity Visibility &bis already set to &eALL&3."));
            source.sendFeedback(text);
            return 0;
        } else {
            ConfigManager.configData.entitiesVisibility = EntitiesEnumsVisibility.ALL;
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Entity Visibility &bhas been set to &eALL&3."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int toggleNone(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        if (ConfigManager.configData.entitiesVisibility == EntitiesEnumsVisibility.NONE) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Entity Visibility &bis already set to &eNONE&3."));
            source.sendFeedback(text);
            return 0;
        } else {
            ConfigManager.configData.entitiesVisibility = EntitiesEnumsVisibility.NONE;
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Entity Visibility &bhas been set to &eNONE&3."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int radiusOn(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        if (ConfigManager.configData.entitiesVisibilityRadiusEnabled) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Entity Visibility Radius &bis already &eon&3."));
            source.sendFeedback(text);
            return 0;
        } else {
            ConfigManager.configData.entitiesVisibilityRadiusEnabled = true;
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Entity Visibility Radius &bhas been turned &bon&3."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int radiusOff(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        if (!ConfigManager.configData.entitiesVisibilityRadiusEnabled) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Entity Visibility Radius &bis already &eoff&3."));
            source.sendFeedback(text);
            return 0;
        } else {
            ConfigManager.configData.entitiesVisibilityRadiusEnabled = false;
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Entity Visibility Radius &bhas been turned &boff&3."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int radiusSet(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        int number = context.getArgument("number", Integer.class);

        ConfigManager.configData.entitiesVisibilityRadius = number;
        Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Entity Visibility Radius Range &bhas been set has to &e" + number  + "&3."));
        source.sendFeedback(text);

        ConfigManager.saveConfig();
        return 1;
    }

    public static int toggleList(CommandContext<FabricClientCommandSource> context, EntitiesEnumsVisibility currentVis) {
        FabricClientCommandSource source = context.getSource();

        if (ConfigManager.configData.entitiesVisibility == currentVis) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Entity Visibility &bis already set to &e" + currentVis.name() + "&3."));
            source.sendFeedback(text);
            return 0;
        } else {
            ConfigManager.configData.entitiesVisibility = currentVis;
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Entity Visibility &bhas been set to &e" + currentVis.name() + "&3."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int toggleAdd(CommandContext<FabricClientCommandSource> context, EntitiesEnumsVisibility currentVis) {
        FabricClientCommandSource source = context.getSource();

        boolean inList = false;
        String name = context.getArgument("name", String.class);

        for (Map.Entry<EntitiesEnumsVisibility, List<String>> entry : EntitiesVisibility.getGroupMap().entrySet()) {
            if (currentVis == entry.getKey()) {
                if (entry.getValue().stream().anyMatch(s -> s.equalsIgnoreCase(name))) {
                    inList = true;
                    break;
                }
                entry.getValue().add(name);
                break;
            }
        }

        if (inList) {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&c" + name + " &bis already in the &e" + currentVis.name() + " &3Entity Visibility list."));
            source.sendFeedback(text);
            return 0;
        } else {
            Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&a" + name + " &bhas been added to the &e" + currentVis.name() + " &3Entity Visibility list."));
            source.sendFeedback(text);
        }
        ConfigManager.saveConfig();
        return 1;
    }

    public static int toggleRemove(CommandContext<FabricClientCommandSource> context, EntitiesEnumsVisibility currentVis) {
        FabricClientCommandSource source = context.getSource();

        boolean inList = false;
        String name = context.getArgument("name", String.class);

        for (Map.Entry<EntitiesEnumsVisibility, List<String>> entry : EntitiesVisibility.getGroupMap().entrySet()) {
            if (currentVis == entry.getKey()) {
                inList = entry.getValue().removeIf(s -> s.equalsIgnoreCase(name));
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

    public static int toggleClear(CommandContext<FabricClientCommandSource> context, EntitiesEnumsVisibility currentVis) {
        FabricClientCommandSource source = context.getSource();

        for (Map.Entry<EntitiesEnumsVisibility, List<String>> entry : EntitiesVisibility.getGroupMap().entrySet()) {
            if (currentVis == entry.getKey()) {
                entry.getValue().clear();
                break;
            }
        }

        Text text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&e" + currentVis.name() + " &3has been cleared&3."));
        source.sendFeedback(text);
        ConfigManager.saveConfig();
        return 1;
    }

    public static int toggleView(CommandContext<FabricClientCommandSource> context, EntitiesEnumsVisibility currentVis) {
        FabricClientCommandSource source = context.getSource();

        List<String> currentList = List.of();

        for (Map.Entry<EntitiesEnumsVisibility, List<String>> entry : EntitiesVisibility.getGroupMap().entrySet()) {
            if (currentVis == entry.getKey()) {
                currentList = entry.getValue();
                break;
            }
        }

        StringBuilder entireList = new StringBuilder();

        entireList.append("&3").append(currentVis.name()).append(" List (&f").append(currentList.size()).append("&3).\n");

        if (currentList.isEmpty()) {
            entireList.append("&3There are no entities in your &e").append(currentVis.name()).append(" list&3.12");
        }

        for (String name : currentList) {
            entireList.append("&b").append(name).append("&8, ");
        }

        source.sendFeedback(Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText(entireList.substring(0, entireList.toString().length() - 2))));
        return 1;
    }
}
