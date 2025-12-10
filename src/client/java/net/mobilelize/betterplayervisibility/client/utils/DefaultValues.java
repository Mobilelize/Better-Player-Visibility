package net.mobilelize.betterplayervisibility.client.utils;

import net.mobilelize.betterplayervisibility.client.visibility.EntitiesEnumsVisibility;
import net.mobilelize.betterplayervisibility.client.visibility.EnumsVisibility;

import java.util.HashMap;
import java.util.Map;

public class DefaultValues {
    public static Map<EnumsVisibility, Boolean> defaultVisibilityCycleMap() {
        Map<EnumsVisibility, Boolean> value = new HashMap<>();

        for (EnumsVisibility visibility : EnumsVisibility.values()) {
            switch (visibility) {
                case ALL, NONE, WHITELIST -> value.put(visibility, true);
                default -> value.put(visibility, false);
            }
        }

        return value;
    }

    public static Map<EntitiesEnumsVisibility, Boolean> defaultEntitiesVisibilityCycleMap() {
        Map<EntitiesEnumsVisibility, Boolean> value = new HashMap<>();

        for (EntitiesEnumsVisibility visibility : EntitiesEnumsVisibility.values()) {
            switch (visibility) {
                case ALL, NONE, WHITELIST -> value.put(visibility, true);
                default -> value.put(visibility, false);
            }
        }

        return value;
    }
}
