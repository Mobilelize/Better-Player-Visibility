package net.mobilelize.betterplayervisibility.client.visibility;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.mobilelize.betterplayervisibility.client.config.ConfigManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EntitiesVisibility {
    public static boolean shouldBeInvisible(Entity entity) {

        if (entity == null) return false;

        boolean reverse = ConfigManager.configData.reversedEntitiesVisibility;

        if (entity instanceof AbstractClientPlayerEntity player) {
            if (player.isMainPlayer()) return false;
        }

        if (ConfigManager.configData.showAllEntities
                || (!reverse && ConfigManager.configData.entitiesVisibility.equals(EntitiesEnumsVisibility.ALL))
                || (reverse && ConfigManager.configData.entitiesVisibility.equals(EntitiesEnumsVisibility.NONE))) return false;

        return reverse != shouldBeInvisibleResult(entity);
    }

    private static boolean shouldBeInvisibleResult(Entity entity) {

        if (notInRadius(entity)) return false;

        String entityType = EntityType.getId(entity.getType()).toString();

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
        if (MinecraftClient.getInstance().world == null) return false;
        Entity entity = null;
        for (Entity e : MinecraftClient.getInstance().world.getEntities()) {
            if (Objects.equals(e.getId(), id)) {
                entity = e;
                break;
            }
        }
        if (entity == null) return false;
        return shouldBeInvisible(entity);
    }

    private static boolean notInRadius(Entity entity) {
        return ConfigManager.configData.entitiesVisibilityRadiusEnabled && !entity.isInRange(MinecraftClient.getInstance().player, ConfigManager.configData.entitiesVisibilityRadius);
    }

    private static boolean listDoesNotContainType(List<String> list, String type) {
        return list.stream().noneMatch(s -> s.equalsIgnoreCase(type));
    }

    public static Map<EntitiesEnumsVisibility, List<String>> getGroupMap() {
        return Map.of(
                EntitiesEnumsVisibility.WHITELIST, ConfigManager.configData.entitiesVisibilityList,
                EntitiesEnumsVisibility.WHITELIST_2, ConfigManager.configData.entitiesVisibilityList2,
                EntitiesEnumsVisibility.WHITELIST_3, ConfigManager.configData.entitiesVisibilityList3,
                EntitiesEnumsVisibility.WHITELIST_4, ConfigManager.configData.entitiesVisibilityList4,
                EntitiesEnumsVisibility.WHITELIST_5, ConfigManager.configData.entitiesVisibilityList5);
    }
}
