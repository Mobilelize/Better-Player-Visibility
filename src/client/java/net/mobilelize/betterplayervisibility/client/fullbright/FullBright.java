package net.mobilelize.betterplayervisibility.client.fullbright;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.utils.ISimpleOption;

public class FullBright {

    private static final double fullBrightNumber = 14;

    private static boolean hasRun = false;

    public static void init(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!hasRun && MinecraftClient.getInstance().options != null) {
                hasRun = true;
                if (ConfigManager.configData.fullBrightEnabled && !isEnabled()) {
                    toggle();
                }
            }
        });
    }

    private static void set(double value){
        ISimpleOption.get(MinecraftClient.getInstance().options.getGamma()).betterPlayerVisibility$forceSetValue(value);
    }

    public static void toggle(boolean newValue){
        if (newValue != isEnabled()) toggle();
    }

    public static void toggle(){
        if (isEnabled()){
            ConfigManager.configData.fullBrightEnabled = false;
            set(ConfigManager.configData.originalGamma);
        } else {
            ConfigManager.configData.originalGamma = MinecraftClient.getInstance().options.getGamma().getValue();
            ConfigManager.configData.fullBrightEnabled = true;
            set(fullBrightNumber);
        }
        ConfigManager.saveConfig();
    }

    public static boolean isEnabled(){
        return MinecraftClient.getInstance().options.getGamma().getValue() == fullBrightNumber;
    }

}
