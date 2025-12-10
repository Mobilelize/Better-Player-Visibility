package net.mobilelize.betterplayervisibility.client.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.mobilelize.betterplayervisibility.client.BetterPlayerVisibilityClient;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.priority.Priority;
import net.mobilelize.betterplayervisibility.client.utils.TextFormatter;

public class PriorityCommandsFunctions {

    public static int priorityList(CommandContext<FabricClientCommandSource> context){
        FabricClientCommandSource source = context.getSource();
        MutableText text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText(Priority.getPriorityList()));
        source.sendFeedback(text);
        return 0;
    }

    public static int priorityListSearch(CommandContext<FabricClientCommandSource> context){
        FabricClientCommandSource source = context.getSource();

        String name = context.getArgument("name", String.class).trim();

        String priority;

        if (ConfigManager.configData.priorityShowEntireListForSearch) {
            priority = Priority.getPriorityList(name);
        } else {
            priority = Priority.getPriorityListSearch(name);
        }

        MutableText text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText(priority));
        source.sendFeedback(text);
        return 0;
    }
}
