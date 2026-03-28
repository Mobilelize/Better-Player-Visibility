package net.mobilelize.betterplayervisibility.client.fullbright;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.utils.ISimpleOption;

public class FullBright {

    private static final double fullBrightNumber = 14;

    private static boolean hasRun = false;

    public static void init(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!hasRun) {
                hasRun = true;
                if (ConfigManager.configData.fullBrightEnabled && !isEnabled()) {
                    toggle();
                }
            }
        });
    }

    private static void set(double value){
        ISimpleOption.get(Minecraft.getInstance().options.gamma()).betterPlayerVisibility$forceSetValue(value);
    }

    public static void toggle(boolean newValue){
        if (newValue != isEnabled()) toggle();
    }

    public static void toggle(){
        if (isEnabled()){
            ConfigManager.configData.fullBrightEnabled = false;
            set(ConfigManager.configData.originalGamma);
        } else {
            ConfigManager.configData.originalGamma = Minecraft.getInstance().options.gamma().get();
            ConfigManager.configData.fullBrightEnabled = true;
            set(fullBrightNumber);
        }
        ConfigManager.saveConfig();
    }

    public static boolean isEnabled(){
        return Minecraft.getInstance().options.gamma().get() == fullBrightNumber;
    }

}
