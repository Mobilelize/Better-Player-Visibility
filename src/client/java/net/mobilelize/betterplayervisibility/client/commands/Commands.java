package net.mobilelize.betterplayervisibility.client.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesEnumsVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.EnumsVisibility;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Commands {

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            for (String command : ConfigManager.configData.visibilityCommandAliases) {
                dispatcher.register(visibility(command));
            }

            for (String command : ConfigManager.configData.entitiesVisibilityCommandAliases) {
                dispatcher.register(entitiesVisibility(command));
            }

            for (String command : ConfigManager.configData.pingCommandAliases) {
                dispatcher.register(ping(command));
            }

            for (String command : ConfigManager.configData.priorityCommandAliases) {
                dispatcher.register(priority(command));
            }
        });
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> visibility(String command) {
        return literal(command)
                .executes(PlayerVisibilityCommandsFunctions::openConfig)

                .then(literal("showall")
                        .then(argument("boolean", BoolArgumentType.bool())
                                .executes(PlayerVisibilityCommandsFunctions::showAll)))

                .then(literal("reverse")
                        .then(argument("boolean", BoolArgumentType.bool())
                                .executes(PlayerVisibilityCommandsFunctions::reverseVisibility)))

                .then(literal("all")
                        .executes(PlayerVisibilityCommandsFunctions::toggleAll))

                .then(literal("none")
                        .executes(PlayerVisibilityCommandsFunctions::toggleNone))

                .then(literal("radius")
                        .executes(sourceCommandContext -> PlayerVisibilityCommandsFunctions.nothing(sourceCommandContext,
                                "&3Use this to enable and disable player radius and change the size."))
                        .then(literal("on")
                                .executes(PlayerVisibilityCommandsFunctions::radiusOn)
                        )
                        .then(literal("off")
                                .executes(PlayerVisibilityCommandsFunctions::radiusOff)
                        )
                        .then(literal("set")
                                .executes(sourceCommandContext -> PlayerVisibilityCommandsFunctions.nothing(sourceCommandContext,
                                        "&3Use this to change the player radius size."))
                                .then(argument("number", IntegerArgumentType.integer(0))
                                        .executes(PlayerVisibilityCommandsFunctions::radiusSet)
                                )
                        )
                )

                .then(literal("size")
                        .executes(sourceCommandContext -> PlayerVisibilityCommandsFunctions.nothing(sourceCommandContext,
                                "&3Use this to enable and disable player size and also change the player size."))
                        .then(literal("on")
                                .executes(PlayerVisibilityCommandsFunctions::sizeOn)
                        )
                        .then(literal("off")
                                .executes(PlayerVisibilityCommandsFunctions::sizeOff)
                        )
                        .then(literal("set")
                                .executes(sourceCommandContext -> PlayerVisibilityCommandsFunctions.nothing(sourceCommandContext,
                                        "&3Use this to change the player size."))
                                .then(argument("number", FloatArgumentType.floatArg(0, 0.9375F))
                                        .executes(PlayerVisibilityCommandsFunctions::sizeSet)
                                )
                        )
                )

                .then(entitiesVisibility("entities"))

                .then(visibilityEnums(EnumsVisibility.WHITELIST))
                .then(visibilityEnums(EnumsVisibility.WHITELIST_2))
                .then(visibilityEnums(EnumsVisibility.WHITELIST_3))
                .then(visibilityEnums(EnumsVisibility.WHITELIST_4))
                .then(visibilityEnums(EnumsVisibility.WHITELIST_5))
                .then(visibilityEnumsHighlight(EnumsVisibility.STAFF))
                .then(visibilityEnumsHighlight(EnumsVisibility.ALLIES))
                .then(visibilityEnumsHighlight(EnumsVisibility.ENEMIES))
                .then(visibilityEnumsHighlight(EnumsVisibility.HIGHLIGHT))
                .then(visibilityEnumsHighlight(EnumsVisibility.HIGHLIGHT_2))
                .then(visibilityEnumsHighlight(EnumsVisibility.HIGHLIGHT_3))
                .then(visibilityEnumsHighlight(EnumsVisibility.HIGHLIGHT_4))
                .then(visibilityEnumsHighlight(EnumsVisibility.HIGHLIGHT_5));
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> entitiesVisibility(String command) {
        return literal(command)

                .executes(sourceCommandContext -> EntityVisibilityCommandsFunctions.nothing(sourceCommandContext,
                        "&3Use this to control entity visibility."))

                .then(literal("showall")
                        .then(argument("boolean", BoolArgumentType.bool())
                                .executes(EntityVisibilityCommandsFunctions::showAll)))

                .then(literal("reverse")
                        .then(argument("boolean", BoolArgumentType.bool())
                                .executes(EntityVisibilityCommandsFunctions::reverseVisibility)))

                .then(literal("all")
                        .executes(EntityVisibilityCommandsFunctions::toggleAll))

                .then(literal("none")
                        .executes(EntityVisibilityCommandsFunctions::toggleNone))

                .then(literal("radius")
                        .executes(sourceCommandContext -> EntityVisibilityCommandsFunctions.nothing(sourceCommandContext,
                                "&3Use this to enable and disable entity radius and change the size."))
                        .then(literal("on")
                                .executes(EntityVisibilityCommandsFunctions::radiusOn)
                        )
                        .then(literal("off")
                                .executes(EntityVisibilityCommandsFunctions::radiusOff)
                        )
                        .then(literal("set")
                                .executes(sourceCommandContext -> EntityVisibilityCommandsFunctions.nothing(sourceCommandContext,
                                        "&3Use this to change the entity radius size."))
                                .then(argument("number", IntegerArgumentType.integer(0))
                                        .executes(EntityVisibilityCommandsFunctions::radiusSet)
                                )
                        )
                )

                .then(entitiesVisibilityEnums(EntitiesEnumsVisibility.WHITELIST))
                .then(entitiesVisibilityEnums(EntitiesEnumsVisibility.WHITELIST_2))
                .then(entitiesVisibilityEnums(EntitiesEnumsVisibility.WHITELIST_3))
                .then(entitiesVisibilityEnums(EntitiesEnumsVisibility.WHITELIST_4))
                .then(entitiesVisibilityEnums(EntitiesEnumsVisibility.WHITELIST_5));

    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> entitiesVisibilityEnums(EntitiesEnumsVisibility visibility) {
        return literal(visibility.name().toLowerCase())
                .executes(context -> EntityVisibilityCommandsFunctions.toggleList(context, visibility))
                .then(literal("add")
                        .executes(sourceCommandContext -> EntityVisibilityCommandsFunctions.nothing(sourceCommandContext,
                                "&3Add entities to the " + visibility.name() + " visibility group."))
                        .then(argument("name", StringArgumentType.greedyString())
                                .suggests(Suggestions.getAllEntitiesType)
                                .executes(context -> EntityVisibilityCommandsFunctions.toggleAdd(context, visibility))))
                .then(literal("remove")
                        .executes(sourceCommandContext -> EntityVisibilityCommandsFunctions.nothing(sourceCommandContext,
                                "&3Remove entities from the " + visibility.name() + " visibility group."))
                        .then(argument("name", StringArgumentType.greedyString())
                                .suggests(Suggestions.getEntityVisibilityList(visibility))
                                .executes(context -> EntityVisibilityCommandsFunctions.toggleRemove(context, visibility))))

                .then(literal("clear")
                        .executes(context -> EntityVisibilityCommandsFunctions.toggleClear(context, visibility)))

                .then(literal("view")
                        .executes(context -> EntityVisibilityCommandsFunctions.toggleView(context, visibility)));
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> visibilityEnums(EnumsVisibility visibility) {
        return literal(visibility.name().toLowerCase())
                .executes(context -> PlayerVisibilityCommandsFunctions.toggleList(context, visibility))
                .then(literal("add")
                        .executes(sourceCommandContext -> PlayerVisibilityCommandsFunctions.nothing(sourceCommandContext,
                                "&3Add players to the " + visibility.name() + " visibility group."))
                        .then(argument("name", StringArgumentType.greedyString())
                                .suggests(Suggestions.getOnlinePlayersNames)
                                .executes(context -> PlayerVisibilityCommandsFunctions.toggleAdd(context, visibility))))
                .then(literal("remove")
                        .executes(sourceCommandContext -> PlayerVisibilityCommandsFunctions.nothing(sourceCommandContext,
                                "&3Remove players from the " + visibility.name() + " visibility group."))
                        .then(argument("name", StringArgumentType.greedyString())
                                .suggests(Suggestions.getPlayerVisibilityList(visibility))
                                .executes(context -> PlayerVisibilityCommandsFunctions.toggleRemove(context, visibility))))

                .then(literal("clear")
                        .executes(context -> PlayerVisibilityCommandsFunctions.toggleClear(context, visibility)))

                .then(literal("view")
                        .executes(context -> PlayerVisibilityCommandsFunctions.toggleView(context, visibility)));
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> visibilityEnumsHighlight(EnumsVisibility highlight) {
        return literal(highlight.name().toLowerCase())
                .executes(context -> PlayerVisibilityCommandsFunctions.toggleList(context, highlight))
                .then(literal("add")
                        .executes(sourceCommandContext -> PlayerVisibilityCommandsFunctions.nothing(sourceCommandContext,
                                "&3Add players to the " + highlight.name() + " highlight group."))
                        .then(argument("name", StringArgumentType.greedyString())
                                .suggests(Suggestions.getOnlinePlayersNames)
                                .executes(context -> PlayerVisibilityCommandsFunctions.toggleAdd(context, highlight))))
                .then(literal("remove")
                        .executes(sourceCommandContext -> PlayerVisibilityCommandsFunctions.nothing(sourceCommandContext,
                                "&3Remove players from the " + highlight.name() + " highlight group."))
                        .then(argument("name", StringArgumentType.greedyString())
                                .suggests(Suggestions.getPlayerVisibilityList(highlight))
                                .executes(context -> PlayerVisibilityCommandsFunctions.toggleRemove(context, highlight))))

                .then(literal("clear")
                        .executes(context -> PlayerVisibilityCommandsFunctions.toggleClear(context, highlight)))

                .then(literal("view")
                        .executes(context -> PlayerVisibilityCommandsFunctions.toggleView(context, highlight)));
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> ping(String command) {
        return literal(command)
                .executes(PingCommandsFunctions::showYourPing)
                .then(argument("name", StringArgumentType.greedyString())
                        .suggests(Suggestions.getOnlinePlayersNames)
                        .executes(PingCommandsFunctions::showPing)
                );
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> priority(String command) {
        return literal(command)
                .executes(PriorityCommandsFunctions::priorityList)
                .then(argument("name", StringArgumentType.greedyString())
                        .suggests(Suggestions.getOnlinePlayersNames)
                        .executes(PriorityCommandsFunctions::priorityListSearch));
    }
}
