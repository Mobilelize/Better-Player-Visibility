package net.mobilelize.betterplayervisibility.client.priority;

import net.mobilelize.betterplayervisibility.client.config.ConfigManager;
import net.mobilelize.betterplayervisibility.client.highlight.BaseHighlight;

public class BasePriorityGroup {
    public boolean showPriorityGroup = true;
    public String priorityText = "%a%%n%:%c%%p%%s%";
    public transient BaseHighlight highlightGroup;

    public BasePriorityGroup build(BaseHighlight highlightGroup) {
        this.highlightGroup = highlightGroup;
        return this;
    }

    public String getPriorityTextFormatted(int number, String name) {
        return priorityText
                .replace("%a%", toHexColor(highlightGroup.accentIdentifierColor))
                .replace("%c%", toHexColor(highlightGroup.identifierColor))
                .replace("%n%", String.valueOf(number))
                .replace("%p%", name)
                .replace("%s%", ConfigManager.configData.prioritySeparatorText.replace("%space%", " "))
                .replace("%space%", " ");
    }

    private static String toHexColor(int color) {
        return String.format("&#%06X", color & 0xFFFFFF);
    }

    // %c% = color
    // %a% = accent color
    // %n% = number
    // %p% = player
    // %s% = separator
    // %space% = space
}
