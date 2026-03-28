package net.mobilelize.betterplayervisibility.client.priority;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.Entity;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;

import java.util.*;

public class Priority {

    private static final HashMap<UUID, PriorityCachedEntry> priorityCache = new HashMap<>();
    private record PriorityCachedEntry(int index, String name) {}
    private record PriorityEntry(int index, String name, boolean cached) {}

    public static void addPriorityCache(AbstractClientPlayer player) {
        if (Minecraft.getInstance().getConnection() == null) return;
        UUID uuid = Minecraft.getInstance().getConnection().getOnlinePlayers().stream().map(PlayerInfo::getProfile).map(GameProfile::id).filter(s -> s.equals(player.getUUID())).findFirst().orElse(null);
        if (uuid == null) return;
        priorityCache.put(player.getUUID(), new PriorityCachedEntry(player.getId(), player.getGameProfile().name()));
    }

    public static void addPriorityCache(int id) {
        if (Minecraft.getInstance().level == null) return;
        AbstractClientPlayer abstractClientPlayerEntity = Minecraft.getInstance().level.players().stream().filter(entry -> Objects.equals(entry.getId(), id)).findFirst().orElse(null);
        if (abstractClientPlayerEntity == null || abstractClientPlayerEntity.isLocalPlayer()) return;
        addPriorityCache(abstractClientPlayerEntity);
    }

    public static void removePriorityCache(List<UUID> playersUUIDs) {
        for (UUID uuid : playersUUIDs) {
            priorityCache.remove(uuid);
        }
    }

    public static void clearCache() {
        priorityCache.clear();
    }

    public static String getPriorityListSearch(String name) {
        List<PriorityEntry> all = getPriorityEntries();

        String text = null;
        int index = 1;

        for (PriorityEntry entry : all) {
            if (entry.name.equalsIgnoreCase(name)) {
                text = ConfigManager.configData.prioritySearchedForText
                        .replace("%n%", String.valueOf(index))
                        .replace("%p%", entry.name);
                break;
            }
            index++;
        }

        if (text == null) {
            text = ConfigManager.configData.prioritySearchedForNotFoundText
                    .replace("%p%", name);
        }

        return text;
    }

    public static String getPriorityList() {
        return getPriorityList(null);
    }

    public static String getPriorityList(String name) {
        List<PriorityEntry> all = getPriorityEntries();

        StringBuilder stringBuilder = new StringBuilder();

        if (ConfigManager.configData.priorityShowBeginningText) {
            stringBuilder.append(ConfigManager.configData.priorityBeginningText).append("\n");
        }

        int index = 1;

        for (PriorityEntry entry : all) {
            BasePriorityGroup priorityGroupByPlayerName = getPriorityGroupByPlayerName(entry.name);
            if (entry.index == getMainPlayersIndex()) {
                stringBuilder.append(getPriorityTextFormatted(ConfigManager.configData.priorityMainPlayerText, index++, entry.name));
                continue;
            }
            if (ConfigManager.configData.priorityShowEntireListForSearch && entry.name.equalsIgnoreCase(name)) {
                stringBuilder.append(getPriorityTextFormatted(ConfigManager.configData.prioritySearchedForPlayerText, index++, entry.name));
                continue;
            }
            if (priorityGroupByPlayerName == null || !priorityGroupByPlayerName.showPriorityGroup) {
                if (entry.cached) {
                    stringBuilder.append(getPriorityTextFormatted(ConfigManager.configData.priorityCachedText, index++, entry.name));
                } else {
                    stringBuilder.append(getPriorityTextFormatted(ConfigManager.configData.priorityNormalText, index++, entry.name));
                }
            } else {
                stringBuilder.append(priorityGroupByPlayerName.getPriorityTextFormatted(index++, entry.name));
            }
        }

        String result = stringBuilder.toString();

        if (ConfigManager.configData.priorityRemoveLastSeparator) {
            String sep = ConfigManager.configData.prioritySeparatorText.replace("%space%", " ");
            if (result.endsWith(sep)) {
                result = result.substring(0, result.length() - sep.length());
            }
        }

        return result;
    }

    private static List<PriorityEntry> getPriorityEntries() {
        HashMap<Integer, String> playerList = getVisiblePlayersPriority();

        HashMap<Integer, String> updatedCacheList = new HashMap<>();

        if (ConfigManager.configData.priorityUseCachedList) {
            for (PriorityCachedEntry entry : priorityCache.values()) {
                if (!playerList.containsKey(entry.index)) {
                    updatedCacheList.put(entry.index, entry.name);
                }
            }
        }

        List<PriorityEntry> all = new ArrayList<>();

        for (Map.Entry<Integer, String> e : playerList.entrySet()) {
            all.add(new PriorityEntry(e.getKey(), e.getValue(), false));
        }

        for (Map.Entry<Integer, String> e : updatedCacheList.entrySet()) {
            all.add(new PriorityEntry(e.getKey(), e.getValue(), true));
        }
        return all.stream().sorted(Comparator.comparingInt(s -> s.index)).toList();
    }

    public static String getPriorityTextFormatted(String priorityText, int number, String name) {
        return priorityText
                .replace("%n%", String.valueOf(number))
                .replace("%p%", name)
                .replace("%s%", ConfigManager.configData.prioritySeparatorText.replace("%space%", " "))
                .replace("%space%", " ");
    }

    public static BasePriorityGroup getPriorityGroupByPlayerName(String name) {
        for (BasePriorityGroup group : getPriorityGroupList()) {
            if (group.highlightGroup.list.stream().anyMatch(s -> s.equalsIgnoreCase(name))) {
                return group;
            }
        }
        return null;
    }

    public static HashMap<Integer, String> getVisiblePlayersPriority() {
        if (Minecraft.getInstance().level == null) return new HashMap<>();
        List<AbstractClientPlayer> players = Minecraft.getInstance().level.players().stream().sorted(Comparator.comparingInt(Entity::getId)).toList();
        HashMap<Integer, String> list = new HashMap<>();
        for (AbstractClientPlayer player : players) {
            list.put(player.getId(), player.getGameProfile().name());
        }
        return list;
    }

    public static List<BasePriorityGroup> getPriorityGroupList() {
        return List.of(
                ConfigManager.configData.priorityStaff.build(ConfigManager.configData.staff),
                ConfigManager.configData.priorityAllies.build(ConfigManager.configData.allies),
                ConfigManager.configData.priorityEnemies.build(ConfigManager.configData.enemies),
                ConfigManager.configData.priorityHighlight.build(ConfigManager.configData.highlight),
                ConfigManager.configData.priorityHighlight2.build(ConfigManager.configData.highlight2),
                ConfigManager.configData.priorityHighlight3.build(ConfigManager.configData.highlight3),
                ConfigManager.configData.priorityHighlight4.build(ConfigManager.configData.highlight4),
                ConfigManager.configData.priorityHighlight5.build(ConfigManager.configData.highlight5)
        );
    }

    private static int getMainPlayersIndex() {
        if (Minecraft.getInstance().player == null) return -1;
        return Minecraft.getInstance().player.getId();
    }
}
