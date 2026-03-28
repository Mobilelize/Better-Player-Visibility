package net.mobilelize.betterplayervisibility.client.visibility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EntitiesVisibility {
    public static boolean shouldBeInvisible(Entity entity) {

        if (entity == null) return false;

        boolean reverse = ConfigManager.configData.reversedEntitiesVisibility;

        if (entity instanceof AbstractClientPlayer player) {
            if (player.isLocalPlayer()) return false;
        }

        if (ConfigManager.configData.showAllEntities
                || (!reverse && ConfigManager.configData.entitiesVisibility.equals(EntitiesEnumsVisibility.ALL))
                || (reverse && ConfigManager.configData.entitiesVisibility.equals(EntitiesEnumsVisibility.NONE))) return false;

        return reverse != shouldBeInvisibleResult(entity);
    }

    private static boolean shouldBeInvisibleResult(Entity entity) {

        if (notInRadius(entity)) return false;

        String entityType = EntityType.getKey(entity.getType()).toString();

        if (ConfigManager.configData.entitiesVisibility == EntitiesEnumsVisibility.NONE) {
            return true;
        }

        for (Map.Entry<EntitiesEnumsVisibility, List<String>> entry : getGroupMap().entrySet()) {
            if (ConfigManager.configData.entitiesVisibility == entry.getKey()) {
                return listDoesNotContainType(entry.getValue(), entityType);
            }
        }

        return false;
    }

    public static boolean shouldBeInvisibleById(int id) {
        if (Minecraft.getInstance().level == null) return false;
        Entity entity = null;
        for (Entity e : Minecraft.getInstance().level.entitiesForRendering()) {
            if (Objects.equals(e.getId(), id)) {
                entity = e;
                break;
            }
        }
        if (entity == null) return false;
        return shouldBeInvisible(entity);
    }

    private static boolean notInRadius(Entity entity) {
        if (!ConfigManager.configData.entitiesVisibilityRadiusEnabled) return false;
        assert Minecraft.getInstance().player != null;
        return !entity.closerThan(Minecraft.getInstance().player, ConfigManager.configData.entitiesVisibilityRadius);
    }

    private static boolean listDoesNotContainType(List<String> list, String type) {
        return list.stream().noneMatch(s -> getTypeOrAddOne(s).equalsIgnoreCase(type));
    }

    public static Map<EntitiesEnumsVisibility, List<String>> getGroupMap() {
        return Map.of(
                EntitiesEnumsVisibility.WHITELIST, ConfigManager.configData.entitiesVisibilityList,
                EntitiesEnumsVisibility.WHITELIST_2, ConfigManager.configData.entitiesVisibilityList2,
                EntitiesEnumsVisibility.WHITELIST_3, ConfigManager.configData.entitiesVisibilityList3,
                EntitiesEnumsVisibility.WHITELIST_4, ConfigManager.configData.entitiesVisibilityList4,
                EntitiesEnumsVisibility.WHITELIST_5, ConfigManager.configData.entitiesVisibilityList5);
    }

    public static String getTypeOrAddOne(String type) {
        if (type.contains(":")) return type;
        return "minecraft:" + type;
    }
}
