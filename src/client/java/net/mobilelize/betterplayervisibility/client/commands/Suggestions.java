package net.mobilelize.betterplayervisibility.client.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.mobilelize.betterplayervisibility.client.highlight.BaseHighlight;
import net.mobilelize.betterplayervisibility.client.highlight.HighlightPlayers;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesEnumsVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.EnumsVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.PlayerVisibility;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Suggestions {
    public static final SuggestionProvider<FabricClientCommandSource> getOnlinePlayersNames = (context, builder) -> {
        for (String playerName : Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getPlayerList().stream().map(PlayerListEntry::getProfile).map(GameProfile::name).toList()) {
            if (CommandSource.shouldSuggest(builder.getRemaining().toLowerCase(), playerName.toLowerCase())) {
                builder.suggest(playerName);
            }
        }

        return builder.buildFuture();
    };

    public static SuggestionProvider<FabricClientCommandSource> getPlayerVisibilityList(EnumsVisibility currentVis) {
        return (context, builder) -> {
            String currentName = builder.getRemaining();

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

            for (String name : currentList) {
                if (CommandSource.shouldSuggest(currentName.toLowerCase(), name.toLowerCase())) {
                    builder.suggest(name);
                }
            }

            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<FabricClientCommandSource> getEntityVisibilityList(EntitiesEnumsVisibility currentVis) {
        return (context, builder) -> {
            String remaining = builder.getRemaining().toLowerCase();

            List<String> currentList = List.of();

            for (Map.Entry<EntitiesEnumsVisibility, List<String>> entry : EntitiesVisibility.getGroupMap().entrySet()) {
                if (currentVis == entry.getKey()) {
                    currentList = entry.getValue();
                    break;
                }
            }

            for (String identifier : currentList) {

                int index = identifier.indexOf(':');
                String namespace = index == -1 ? "" : identifier.substring(0, index);
                String path = index == -1 ? "" : identifier.substring(index + 1);

                if (CommandSource.shouldSuggest(remaining, identifier.toLowerCase())
                        || CommandSource.shouldSuggest(remaining, path.toLowerCase())
                        || CommandSource.shouldSuggest(remaining, namespace.toLowerCase())) {

                    builder.suggest(identifier);
                }
            }

            return builder.buildFuture();
        };
    }

    public static final SuggestionProvider<FabricClientCommandSource> getAllEntitiesType = (context, builder) -> {
        String remaining = builder.getRemaining().toLowerCase();

        for (Identifier id : Registries.ENTITY_TYPE.getIds()) {
            String full = id.toString();         // "minecraft:cow"
            String path = id.getPath();           // "cow"
            String namespace = id.getNamespace(); // "minecraft"

            if (CommandSource.shouldSuggest(remaining, full.toLowerCase())
                    || CommandSource.shouldSuggest(remaining, path.toLowerCase())
                    || CommandSource.shouldSuggest(remaining, namespace.toLowerCase())) {

                builder.suggest(full);
            }
        }

        return builder.buildFuture();
    };
}
