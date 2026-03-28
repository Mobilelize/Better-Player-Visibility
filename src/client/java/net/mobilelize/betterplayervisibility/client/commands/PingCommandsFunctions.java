package net.mobilelize.betterplayervisibility.client.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.mobilelize.betterplayervisibility.client.BetterPlayerVisibilityClient;
import net.mobilelize.betterplayervisibility.client.ping.Ping;
import net.mobilelize.betterplayervisibility.client.utils.TextFormatter;

public class PingCommandsFunctions {
    public static int showYourPing(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        Component ping = Ping.pingFormatted(Ping.getPlayersPing(source.getPlayer().getUUID()));

        MutableComponent text = Component.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter
                .formatText("&3Your ping &bis currently ").append(ping).append(TextFormatter.formatText("&3.")));
        source.sendFeedback(text);
        return 1;
    }

    public static int showPing(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        String name = context.getArgument("name", String.class).trim();

        int pingNumber = Ping.getPlayersPingByName(name);
        Component ping = Ping.pingFormatted(pingNumber);

        if (pingNumber == -1) {
            MutableComponent text = Component.literal("").append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Ping &bfor the player &e" + name + " &bcouldn't be found&3."));
            source.sendFeedback(text);
            return 0;
        }

        MutableComponent text = Component.literal("").append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&e" + name + "'s &3ping &bis currently ")).append(ping).append(TextFormatter.formatText("&3."));
        source.sendFeedback(text);
        return 1;
    }
}
