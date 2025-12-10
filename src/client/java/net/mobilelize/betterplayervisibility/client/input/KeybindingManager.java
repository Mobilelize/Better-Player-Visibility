package net.mobilelize.betterplayervisibility.client.input;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.fullbright.FullBright;
import net.mobilelize.betterplayervisibility.client.utils.ChatOrActionBar;
import net.mobilelize.betterplayervisibility.client.utils.TextFormatter;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesEnumsVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.EnumsVisibility;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class KeybindingManager {

    public static Set<KeyBinding> keyBindings = new HashSet<>();

    // Define category for the keybinding
    //public static final String CATEGORY = "key.category.betterplayervisibility.keybinds";

    public static final Identifier CATEGORY_ID = Identifier.of("betterplayervisibility", "keybinds");

    public static final KeyBinding.Category CATEGORY =
            new KeyBinding.Category(CATEGORY_ID);

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

    public static KeyBinding playersCycleKeyBinding = new KeyBinding(
            PLAYERS_CYCLE_VISIBILITY_MODE,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyBinding entitiesCycleKeyBinding = new KeyBinding(
            ENTITIES_CYCLE_VISIBILITY_MODE,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyBinding showAllPlayersToggleBinding = new KeyBinding(
            SHOW_ALL_PLAYERS_TOGGLE,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyBinding showAllEntitiesToggleBinding = new KeyBinding(
            SHOW_ALL_ENTITIES_TOGGLE,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyBinding playersRadiusToggleBinding = new KeyBinding(
            PLAYERS_TOGGLE_VISIBILITY_RADIUS,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyBinding entitiesRadiusToggleBinding = new KeyBinding(
            ENTITIES_TOGGLE_VISIBILITY_RADIUS,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyBinding playersSizeToggle = new KeyBinding(
            PLAYERS_SIZE_TOGGLE,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyBinding playersReverseVisibilityToggle = new KeyBinding(
            REVERSE_PLAYERS_VISIBILITY_TOGGLE,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyBinding entitiesReverseVisibilityToggle = new KeyBinding(
            REVERSE_ENTITIES_VISIBILITY_TOGGLE,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyBinding pingToggle = new KeyBinding(
            PING_TOGGLE,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyBinding barriersToggle = new KeyBinding(
            BARRIER_TOGGLE,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static KeyBinding fullBrightToggle = new KeyBinding(
            FULL_BRIGHT_TOGGLE,
            InputUtil.Type.KEYSYM,
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

        for (KeyBinding keyBinding : keyBindings) {
            KeyBindingHelper.registerKeyBinding(keyBinding);
        }

        // Listen for key presses every tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (showAllPlayersToggleBinding.isPressed()) {
                ConfigManager.configData.showAllPlayers = !ConfigManager.configData.showAllPlayers;
                toggleMessage("&3Show All Players &bis now " + (ConfigManager.configData.showAllPlayers ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }
            if (showAllEntitiesToggleBinding.isPressed()) {
                ConfigManager.configData.showAllEntities = !ConfigManager.configData.showAllEntities;
                toggleMessage("&3Show All Entities &bis now " + (ConfigManager.configData.showAllEntities ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }

            if (playersReverseVisibilityToggle.isPressed()) {
                ConfigManager.configData.reversedVisibility = !ConfigManager.configData.reversedVisibility;
                toggleMessage("&3Reversed Players Visibility &bis now " + (ConfigManager.configData.reversedVisibility ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }
            if (entitiesReverseVisibilityToggle.isPressed()) {
                ConfigManager.configData.reversedEntitiesVisibility = !ConfigManager.configData.reversedEntitiesVisibility;
                toggleMessage("&3Reversed Entities Visibility &bis now " + (ConfigManager.configData.reversedEntitiesVisibility ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }

            if (playersRadiusToggleBinding.isPressed()) {
                ConfigManager.configData.visibilityRadiusEnabled = !ConfigManager.configData.visibilityRadiusEnabled;
                toggleMessage("&3Players Visibility Radius &bis now " + (ConfigManager.configData.visibilityRadiusEnabled ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }
            if (entitiesRadiusToggleBinding.isPressed()) {
                ConfigManager.configData.entitiesVisibilityRadiusEnabled = !ConfigManager.configData.entitiesVisibilityRadiusEnabled;
                toggleMessage("&3Entities Visibility Radius &bis now " + (ConfigManager.configData.entitiesVisibilityRadiusEnabled ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }

            if (playersSizeToggle.isPressed()) {
                ConfigManager.configData.visibilityChangeSizeEnabled = !ConfigManager.configData.visibilityChangeSizeEnabled;
                toggleMessage("&3Players Size &bis now " + (ConfigManager.configData.visibilityChangeSizeEnabled ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }

            if (pingToggle.isPressed()) {
                ConfigManager.configData.showPing = !ConfigManager.configData.showPing;
                toggleMessage("&3Ping &bis now " + (ConfigManager.configData.showPing ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }

            if (barriersToggle.isPressed()) {
                ConfigManager.configData.visibleBarrier = !ConfigManager.configData.visibleBarrier;
                MinecraftClient.getInstance().worldRenderer.reload();
                toggleMessage("&3Visible Barriers &bis now " + (ConfigManager.configData.visibleBarrier ? "&aon" : "&coff"));
                ConfigManager.saveConfig();
            }

            if (fullBrightToggle.isPressed()) {
                FullBright.toggle();
                toggleMessage("&3Full Bright &bis now " + (FullBright.isEnabled() ? "&aon" : "&coff"));
            }

            if (playersCycleKeyBinding.isPressed()) {
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

            if (entitiesCycleKeyBinding.isPressed()) {
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


            for (KeyBinding keyBinding : keyBindings){
                keyBinding.setPressed(false);
            }
        });
    }

    public static void toggleMessage(String text) {
        if (MinecraftClient.getInstance().player == null) return;
        Text switchMsg = TextFormatter.formatText(text);
        if (!ConfigManager.configData.chatOrActionBar.equals(ChatOrActionBar.NONE)) {
            MinecraftClient.getInstance().player.sendMessage(switchMsg, ConfigManager.configData.chatOrActionBar.equals(ChatOrActionBar.ACTIONBAR));
        }
    }
}
