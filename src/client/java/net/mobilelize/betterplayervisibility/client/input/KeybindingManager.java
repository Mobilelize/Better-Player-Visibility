package net.mobilelize.betterplayervisibility.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.fullbright.FullBright;
import net.mobilelize.betterplayervisibility.client.utils.ChatOrActionBar;
import net.mobilelize.betterplayervisibility.client.utils.TextFormatter;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesEnumsVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.EnumsVisibility;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class KeybindingManager {

    public static Set<KeyMapping> keyBindings = new HashSet<>();

    // Define category for the keybinding
    //public static final String CATEGORY = "key.category.betterplayervisibility.keybinds";

    public static final Identifier CATEGORY_ID = Identifier.fromNamespaceAndPath("betterplayervisibility", "keybinds");

    public static final KeyMapping.Category CATEGORY =
            new KeyMapping.Category(CATEGORY_ID);

    public static final String SHOW_ALL_PLAYERS_TOGGLE = "key.betterplayervisibility.showallplayers";
    public static final String SHOW_ALL_ENTITIES_TOGGLE = "key.betterplayervisibility.showallentities";

    public static final String PLAYERS_CYCLE_VISIBILITY_MODE = "key.betterplayervisibility.playerscyclevisibility";
    public static final String ENTITIES_CYCLE_VISIBILITY_MODE = "key.betterplayervisibility.entitiescyclevisibility";
    public static final String PLAYERS_TOGGLE_VISIBILITY_RADIUS = "key.betterplayervisibility.playersradiusvisibility";
    public static final String ENTITIES_TOGGLE_VISIBILITY_RADIUS = "key.betterplayervisibility.entitiesradiusvisibility";

    public static final String PING_TOGGLE = "key.betterplayervisibility.ping";
    public static final String BARRIER_TOGGLE = "key.betterplayervisibility.barriers";
    public static final String FULL_BRIGHT_TOGGLE = "key.betterplayervisibility.fullbright";

    public static final String PLAYERS_SIZE_TOGGLE = "key.betterplayervisibility.playerssize";
    public static final String REVERSE_PLAYERS_VISIBILITY_TOGGLE = "key.betterplayervisibility.playersreversedvisibility";
    public static final String REVERSE_ENTITIES_VISIBILITY_TOGGLE = "key.betterplayervisibility.entitiesreversedvisibility";

    public static KeyMapping playersCycleKeyBinding = new KeyMapping(
            PLAYERS_CYCLE_VISIBILITY_MODE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyMapping entitiesCycleKeyBinding = new KeyMapping(
            ENTITIES_CYCLE_VISIBILITY_MODE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyMapping showAllPlayersToggleBinding = new KeyMapping(
            SHOW_ALL_PLAYERS_TOGGLE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyMapping showAllEntitiesToggleBinding = new KeyMapping(
            SHOW_ALL_ENTITIES_TOGGLE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyMapping playersRadiusToggleBinding = new KeyMapping(
            PLAYERS_TOGGLE_VISIBILITY_RADIUS,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyMapping entitiesRadiusToggleBinding = new KeyMapping(
            ENTITIES_TOGGLE_VISIBILITY_RADIUS,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyMapping playersSizeToggle = new KeyMapping(
            PLAYERS_SIZE_TOGGLE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyMapping playersReverseVisibilityToggle = new KeyMapping(
            REVERSE_PLAYERS_VISIBILITY_TOGGLE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyMapping entitiesReverseVisibilityToggle = new KeyMapping(
            REVERSE_ENTITIES_VISIBILITY_TOGGLE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyMapping pingToggle = new KeyMapping(
            PING_TOGGLE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyMapping barriersToggle = new KeyMapping(
            BARRIER_TOGGLE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyMapping fullBrightToggle = new KeyMapping(
            FULL_BRIGHT_TOGGLE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static void registerKeybindings() {
        // Register keybindings with Fabric API (Minecraft will save the key automatically)

        keyBindings.add(showAllPlayersToggleBinding);
        keyBindings.add(showAllEntitiesToggleBinding);
        keyBindings.add(playersCycleKeyBinding);
        keyBindings.add(entitiesCycleKeyBinding);

        keyBindings.add(playersRadiusToggleBinding);
        keyBindings.add(entitiesRadiusToggleBinding);

        keyBindings.add(playersSizeToggle);
        keyBindings.add(playersReverseVisibilityToggle);
        keyBindings.add(entitiesReverseVisibilityToggle);

        keyBindings.add(pingToggle);
        keyBindings.add(barriersToggle);
        keyBindings.add(fullBrightToggle);

        for (KeyMapping keyBinding : keyBindings) {
            KeyMappingHelper.registerKeyMapping(keyBinding);
        }

        // Listen for key presses every tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (showAllPlayersToggleBinding.isDown()) {
                ConfigManager.configData.showAllPlayers = !ConfigManager.configData.showAllPlayers;
                toggleMessage("&3Show All Players &bis now " + (ConfigManager.configData.showAllPlayers ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }
            if (showAllEntitiesToggleBinding.isDown()) {
                ConfigManager.configData.showAllEntities = !ConfigManager.configData.showAllEntities;
                toggleMessage("&3Show All Entities &bis now " + (ConfigManager.configData.showAllEntities ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }

            if (playersReverseVisibilityToggle.isDown()) {
                ConfigManager.configData.reversedVisibility = !ConfigManager.configData.reversedVisibility;
                toggleMessage("&3Reversed Players Visibility &bis now " + (ConfigManager.configData.reversedVisibility ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }
            if (entitiesReverseVisibilityToggle.isDown()) {
                ConfigManager.configData.reversedEntitiesVisibility = !ConfigManager.configData.reversedEntitiesVisibility;
                toggleMessage("&3Reversed Entities Visibility &bis now " + (ConfigManager.configData.reversedEntitiesVisibility ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }

            if (playersRadiusToggleBinding.isDown()) {
                ConfigManager.configData.visibilityRadiusEnabled = !ConfigManager.configData.visibilityRadiusEnabled;
                toggleMessage("&3Players Visibility Radius &bis now " + (ConfigManager.configData.visibilityRadiusEnabled ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }
            if (entitiesRadiusToggleBinding.isDown()) {
                ConfigManager.configData.entitiesVisibilityRadiusEnabled = !ConfigManager.configData.entitiesVisibilityRadiusEnabled;
                toggleMessage("&3Entities Visibility Radius &bis now " + (ConfigManager.configData.entitiesVisibilityRadiusEnabled ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }

            if (playersSizeToggle.isDown()) {
                ConfigManager.configData.visibilityChangeSizeEnabled = !ConfigManager.configData.visibilityChangeSizeEnabled;
                toggleMessage("&3Players Size &bis now " + (ConfigManager.configData.visibilityChangeSizeEnabled ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }

            if (pingToggle.isDown()) {
                ConfigManager.configData.showPing = !ConfigManager.configData.showPing;
                toggleMessage("&3Ping &bis now " + (ConfigManager.configData.showPing ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }

            if (barriersToggle.isDown()) {
                ConfigManager.configData.visibleBarrier = !ConfigManager.configData.visibleBarrier;
                Minecraft.getInstance().levelExtractor.allChanged();
                toggleMessage("&3Visible Barriers &bis now " + (ConfigManager.configData.visibleBarrier ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }

            if (fullBrightToggle.isDown()) {
                FullBright.toggle();
                toggleMessage("&3Full Bright &bis now " + (FullBright.isEnabled() ? "&aon" : "&coff"));
            }

            if (playersCycleKeyBinding.isDown()) {
                EnumsVisibility current = ConfigManager.configData.visibility;

                // Filter only enabled visibilities and sort by ordinal
                List<EnumsVisibility> enabled = ConfigManager.configData.cycleVisibility.entrySet().stream()
                        .filter(Map.Entry::getValue)
                        .map(Map.Entry::getKey)
                        .sorted(Comparator.comparing(Enum::ordinal))
                        .toList();

                EnumsVisibility next = enabled.isEmpty()
                        ? current
                        : enabled.stream()
                        .filter(v -> v.ordinal() > current.ordinal())
                        .findFirst()
                        .orElse(enabled.get(0));

                ConfigManager.configData.visibility = next;
                toggleMessage("&3Players Visibility Mode &bis now &e" + next);
                ConfigManager.saveConfig();
            }

            if (entitiesCycleKeyBinding.isDown()) {
                EntitiesEnumsVisibility current = ConfigManager.configData.entitiesVisibility;

                // Filter only enabled visibilities and sort by ordinal
                List<EntitiesEnumsVisibility> enabled = ConfigManager.configData.entitiesCycleVisibility.entrySet().stream()
                        .filter(Map.Entry::getValue)
                        .map(Map.Entry::getKey)
                        .sorted(Comparator.comparing(Enum::ordinal))
                        .toList();

                EntitiesEnumsVisibility next = enabled.isEmpty()
                        ? current
                        : enabled.stream()
                        .filter(v -> v.ordinal() > current.ordinal())
                        .findFirst()
                        .orElse(enabled.get(0));

                ConfigManager.configData.entitiesVisibility = next;
                toggleMessage("&3Entities Visibility Mode &bis now &e" + next);
                ConfigManager.saveConfig();
            }


            for (KeyMapping keyBinding : keyBindings){
                keyBinding.setDown(false);
            }
        });
    }

    public static void toggleMessage(String text) {
        if (Minecraft.getInstance().player == null) return;
        Component switchMsg = TextFormatter.formatText(text);
        if (!ConfigManager.configData.chatOrActionBar.equals(ChatOrActionBar.NONE)) {
            if (ConfigManager.configData.chatOrActionBar.equals(ChatOrActionBar.ACTIONBAR)) {
                Minecraft.getInstance().player.sendOverlayMessage(switchMsg);
            } else {
                Minecraft.getInstance().player.sendSystemMessage(switchMsg);
            }

        }
    }
}
