package net.mobilelize.betterplayervisibility.client.ping;

import static net.mobilelize.betterplayervisibility.client.utils.EnumsFormatter.formatEnumName;

public class BasePing {
    public final EnumsPing ping;
    public final int index;
    public final String formattedName;

    public final String defaultText;
    public final int defaultColor;
    public final int defaultRange;

    public String text = "%ping%ms";
    public int color = 0xFFFFFF;
    public int range = 0;

    public BasePing(EnumsPing ping, int index, int color, int range) {
        this.ping = ping;
        this.index = index;
        this.color = color;
        this.range = range;

        this.defaultText = text;
        this.defaultColor = color;
        this.defaultRange = range;

        this.formattedName = formatEnumName(ping.name());
    }

    public BasePing(EnumsPing ping, int index, int color, int range, String text) {
        this.ping = ping;
        this.index = index;
        this.color = color;
        this.range = range;
        this.text = text;

        this.defaultText = text;
        this.defaultColor = color;
        this.defaultRange = range;

        this.formattedName = formatEnumName(ping.name());
    }
}
