package net.mobilelize.betterplayervisibility.client.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.mobilelize.betterplayervisibility.client.BetterPlayerVisibilityClient;
import net.mobilelize.betterplayervisibility.client.ping.Ping;
import net.mobilelize.betterplayervisibility.client.utils.TextFormatter;

public class PingCommandsFunctions {
    public static int showYourPing(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        Text ping = Ping.pingFormatted(Ping.getPlayersPing(source.getPlayer().getUuid()));

        MutableText text = Text.empty().append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter
                .formatText("&3Your ping &bis currently ").append(ping).append(TextFormatter.formatText("&3.")));
        source.sendFeedback(text);
        return 1;
    }

    public static int showPing(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();

        String name = context.getArgument("name", String.class).trim();

        int pingNumber = Ping.getPlayersPingByName(name);
        Text ping = Ping.pingFormatted(pingNumber);

        if (pingNumber == -1) {
            MutableText text = Text.literal("").append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&3Ping &bfor the player &e" + name + " &bcouldn't be found&3."));
            source.sendFeedback(text);
            return 0;
        }

        MutableText text = Text.literal("").append(BetterPlayerVisibilityClient.PREFIX).append(TextFormatter.formatText("&e" + name + "'s &3ping &bis currently ")).append(ping).append(TextFormatter.formatText("&3."));
        source.sendFeedback(text);
        return 1;
    }
}
