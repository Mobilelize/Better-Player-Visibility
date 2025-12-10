package net.mobilelize.betterplayervisibility.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.MutableText;
import net.mobilelize.betterplayervisibility.client.commands.Commands;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.fullbright.FullBright;
import net.mobilelize.betterplayervisibility.client.input.KeybindingManager;
import net.mobilelize.betterplayervisibility.client.utils.BlockRenderUtils;
import net.mobilelize.betterplayervisibility.client.utils.TextFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterPlayerVisibilityClient implements ClientModInitializer {

    public static MutableText PREFIX = TextFormatter.formatText(ConfigManager.configData.prefix).append(" ");

    public static final Logger LOGGER = LoggerFactory.getLogger("Better Player Visibility");

    @Override
    public void onInitializeClient() {
        ConfigManager.loadConfig();
        KeybindingManager.registerKeybindings();
        Commands.registerCommands();
        FullBright.init();
        BlockRenderUtils.barrier();
    }
}
