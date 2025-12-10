package net.mobilelize.betterplayervisibility.client.highlight;

import net.mobilelize.betterplayervisibility.client.visibility.EnumsVisibility;

import java.util.ArrayList;
import java.util.List;

import static net.mobilelize.betterplayervisibility.client.utils.EnumsFormatter.formatEnumName;

public class BaseHighlight {
    public final EnumsVisibility group;
    public final String formattedName;
    public boolean highlightEnabled = true;
    public String tag = "⬤";
    public HighlightPlayerModeOption identifierMode = HighlightPlayerModeOption.TAG;
    public int identifierColor = 0xFFFFFF;
    public int accentIdentifierColor = 0xFFFFFF;
    public final int defaultIdentifierColor;
    public List<String> list = new ArrayList<>();

    public BaseHighlight(EnumsVisibility group) {
        this.group = group;
        this.defaultIdentifierColor = identifierColor;
        this.formattedName = formatEnumName(group.name());
    }

    public BaseHighlight(EnumsVisibility group, int identifierColor) {
        this.group = group;
        setIdentifierColor(identifierColor);
        this.defaultIdentifierColor = identifierColor;
        this.formattedName = formatEnumName(group.name());
    }

    public void setIdentifierColor(int identifierColor) {
        this.identifierColor = identifierColor;
        this.accentIdentifierColor = getAccentColor(identifierColor);
    }

    private static int getAccentColor(int hex) {
        int r = (hex >> 16) & 0xFF;
        int g = (hex >> 8) & 0xFF;
        int b = hex & 0xFF;

        float[] hsb = java.awt.Color.RGBtoHSB(r, g, b, null);

        // Increase saturation (gives stronger color)
        hsb[1] = Math.min(1f, hsb[1] * 1.4f);

        // Lower brightness (makes it darker)
        hsb[2] *= 0.55f; // 55% brightness keeps it visible but distinctly darker

        int rgb = java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);

        return rgb & 0xFFFFFF;
    }

}
