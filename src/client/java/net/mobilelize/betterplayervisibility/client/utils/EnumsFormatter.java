package net.mobilelize.betterplayervisibility.client.utils;

public class EnumsFormatter {
    public static String formatEnumName(String name) {
        String[] parts = name.split("_");
        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            sb.append(part.charAt(0))
                    .append(part.substring(1).toLowerCase())
                    .append(" ");
        }

        return sb.toString().trim();
    }
}
