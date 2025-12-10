package net.mobilelize.betterplayervisibility.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;
import net.mobilelize.betterplayervisibility.client.BetterPlayerVisibilityClient;
import net.mobilelize.betterplayervisibility.client.highlight.BaseHighlight;
import net.mobilelize.betterplayervisibility.client.ping.BasePing;
import net.mobilelize.betterplayervisibility.client.ping.EnumsPing;
import net.mobilelize.betterplayervisibility.client.priority.BasePriorityGroup;
import net.mobilelize.betterplayervisibility.client.utils.ChatOrActionBar;
import net.mobilelize.betterplayervisibility.client.visibility.EntitiesEnumsVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.EnumsVisibility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.mobilelize.betterplayervisibility.client.utils.DefaultValues.defaultEntitiesVisibilityCycleMap;
import static net.mobilelize.betterplayervisibility.client.utils.DefaultValues.defaultVisibilityCycleMap;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_DIR = new File(MinecraftClient.getInstance().runDirectory, "config/BetterPlayerVisibility");
    private static final File CONFIG_FILE = new File(CONFIG_DIR, "config.json");

    public static ConfigData configData = new ConfigData();

    public static class ConfigData {
        public String prefix = "&b[BPV]";
        public ChatOrActionBar chatOrActionBar = ChatOrActionBar.ACTIONBAR;

        //Player Visibility
        public List<String> visibilityCommandAliases = List.of("visibility");
        public List<String> entitiesVisibilityCommandAliases = List.of("entityvisibility");
        public List<String> pingCommandAliases = List.of("pingget");
        public List<String> priorityCommandAliases = List.of("priority");

        public boolean invisibleMainPlayer = false;
        public boolean showAllPlayers = true;
        public boolean reversedVisibility = false;
        public boolean visibilityRadiusEnabled = false;
        public Integer visibilityRadius = 4;
        public EnumsVisibility visibility = EnumsVisibility.WHITELIST;
        public Map<EnumsVisibility, Boolean> cycleVisibility = defaultVisibilityCycleMap();
        public List<String> visibilityList = new ArrayList<>();
        public List<String> visibilityList2 = new ArrayList<>();
        public List<String> visibilityList3 = new ArrayList<>();
        public List<String> visibilityList4 = new ArrayList<>();
        public List<String> visibilityList5 = new ArrayList<>();

        public boolean visibilityNPCEnabled = true;
        public boolean visibilityNPC3Characters = true;
        public boolean visibilityNPCTabListEnabled = false;

        public boolean visibilityNameTagEnabled = false;
        public boolean visibilityShowFire = false;
        public boolean visibilityShowShadows = false;
        public boolean visibilitySpawnSprintingParticles = false;
        public boolean visibilityChangeSizeEnabled = true;
        public float visibilityChangeSize = 0.3F;

        //Entity Visibility
        public boolean showAllEntities = true;
        public boolean reversedEntitiesVisibility = true;
        public boolean entitiesVisibilityRadiusEnabled = false;
        public Integer entitiesVisibilityRadius = 4;
        public EntitiesEnumsVisibility entitiesVisibility = EntitiesEnumsVisibility.WHITELIST;
        public Map<EntitiesEnumsVisibility, Boolean> entitiesCycleVisibility = defaultEntitiesVisibilityCycleMap();
        public List<String> entitiesVisibilityList = new ArrayList<>();
        public List<String> entitiesVisibilityList2 = new ArrayList<>();
        public List<String> entitiesVisibilityList3 = new ArrayList<>();
        public List<String> entitiesVisibilityList4 = new ArrayList<>();
        public List<String> entitiesVisibilityList5 = new ArrayList<>();

        //Highlight Players
        public BaseHighlight staff = new BaseHighlight(EnumsVisibility.STAFF, 0xFFFF55);
        public BaseHighlight allies = new BaseHighlight(EnumsVisibility.ALLIES, 0x55FF55);
        public BaseHighlight enemies = new BaseHighlight(EnumsVisibility.ENEMIES, 0xFF5555);
        public BaseHighlight highlight = new BaseHighlight(EnumsVisibility.HIGHLIGHT, 0x00AAAA);
        public BaseHighlight highlight2 = new BaseHighlight(EnumsVisibility.HIGHLIGHT_2, 0x0000AA);
        public BaseHighlight highlight3 = new BaseHighlight(EnumsVisibility.HIGHLIGHT_3, 0xFF55FF);
        public BaseHighlight highlight4 = new BaseHighlight(EnumsVisibility.HIGHLIGHT_4, 0xAA00AA);
        public BaseHighlight highlight5 = new BaseHighlight(EnumsVisibility.HIGHLIGHT_5, 0xAAAAAA);

        //Ping
        public boolean showPing = false;
        public boolean showNoPing = false;
        public boolean useDefaultPingText = true;
        public String defaultPingText = "%ping%ms";

        public BasePing noPing = new BasePing(EnumsPing.NO, 9, 0x555555, -1, "0ms");
        public BasePing excellentPing = new BasePing(EnumsPing.EXCELLENT, 8, 0x007F00, 0);
        public BasePing veryGoodPing = new BasePing(EnumsPing.VERY_GOOD, 7, 0x33CC33, 31);
        public BasePing goodPing = new BasePing(EnumsPing.GOOD, 6, 0x99FF66, 62);
        public BasePing decentPing = new BasePing(EnumsPing.DECENT, 5, 0xFFFF55, 93);
        public BasePing averagePing = new BasePing(EnumsPing.AVERAGE, 4, 0xFFCC00, 125);
        public BasePing poorPing = new BasePing(EnumsPing.POOR, 3, 0xFF9900, 156);
        public BasePing badPing = new BasePing(EnumsPing.BAD, 2, 0xFF6640, 187);
        public BasePing worsePing = new BasePing(EnumsPing.WORSE, 1, 0xFF5555, 218);
        public BasePing worstPing = new BasePing(EnumsPing.WORST, 0, 0xFF0000, 250);

        //Priority
        public boolean priorityShowBeginningText = true;
        public boolean priorityShowEntireListForSearch = false;
        public boolean priorityRemoveLastSeparator = true;
        public boolean priorityUseCachedList = true;
        public String priorityBeginningText = "&3Pickup Priority based on people around you.";
        public String prioritySearchedForText = "&a%p% &bhas &e#%n% &bpickup priority&3.";
        public String prioritySearchedForNotFoundText = "&c%p% &3pickup priority couldn't be found.";
        public String prioritySearchedForPlayerText = "&5%n%:&d%p%%s%";
        public String prioritySeparatorText = "&8,%space%";
        public String priorityMainPlayerText = "&l&6%n%:&e%p%&r%s%";
        public String priorityNormalText = "&3%n%:&b%p%%s%";
        public String priorityCachedText = "&8%n%:&7%p%%s%";
        public BasePriorityGroup priorityStaff = new BasePriorityGroup();
        public BasePriorityGroup priorityAllies = new BasePriorityGroup();
        public BasePriorityGroup priorityEnemies = new BasePriorityGroup();
        public BasePriorityGroup priorityHighlight  = new BasePriorityGroup();
        public BasePriorityGroup priorityHighlight2 = new BasePriorityGroup();
        public BasePriorityGroup priorityHighlight3 = new BasePriorityGroup();
        public BasePriorityGroup priorityHighlight4 = new BasePriorityGroup();
        public BasePriorityGroup priorityHighlight5 = new BasePriorityGroup();

        //Full Bright
        public boolean fullBrightEnabled = false;
        public double originalGamma = 0.5;

        //Visible Barriers
        public boolean visibleBarrier = false;
        public boolean visibleBarrierParticles = false;
    }

    public static void loadConfig() {
        try {
            if (!CONFIG_FILE.exists()) {
                saveDefaultConfig();
            }
            try (FileReader reader = new FileReader(CONFIG_FILE, StandardCharsets.UTF_8)) {
                configData = GSON.fromJson(reader, ConfigData.class);
            }
        } catch (Exception e) {
            BetterPlayerVisibilityClient.LOGGER.error("Failed to load config: {}", e.getMessage());
        }
    }

    public static void saveConfig() {
        try {
            if (!CONFIG_DIR.exists()) {
                CONFIG_DIR.mkdirs();
            }
            try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
                GSON.toJson(configData, writer);
            }
        } catch (Exception e) {
            BetterPlayerVisibilityClient.LOGGER.error("Failed to save config: {}", e.getMessage());
        }
    }

    private static void saveDefaultConfig() {
        saveConfig();
    }
}
