package net.mobilelize.betterplayervisibility.client.utils;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.ChatFormatting;

public class TextFormatter {

    public static MutableComponent formatText(String input) {
        MutableComponent finalText = Component.literal(""); // Root text component
        Style currentStyle = Style.EMPTY;         // Current style

        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);

            // Check for hex code indicator "&#"
            if (c == '&' && i + 1 < input.length() && input.charAt(i + 1) == '#') {
                // Check if there are enough characters for a full hex sequence ("&#" + 6 digits = 8 characters)
                if (i + 8 <= input.length()) {
                    String hexPart = input.substring(i + 2, i + 8); // Expected hex digits
                    try {
                        int color = Integer.parseInt(hexPart, 16);
                        currentStyle = currentStyle.withColor(TextColor.fromRgb(color));
                        i += 8; // Consume the full hex sequence
                        continue;
                    } catch (NumberFormatException e) {
                        // Invalid hex: append literal "&#" and only consume these 2 characters.
                        finalText.append(Component.literal("&#").setStyle(currentStyle));
                        i += 2;
                        continue;
                    }
                } else {
                    // Not enough characters for a full hex sequence.
                    finalText.append(Component.literal("&#").setStyle(currentStyle));
                    i += 2;
                    continue;
                }
            }
            // Check for standard formatting codes (like &a, &l, etc.)
            else if (c == '&' && i + 1 < input.length()) {
                char code = Character.toLowerCase(input.charAt(i + 1));
                currentStyle = switch (code) {
                    case '0' -> currentStyle.withColor(ChatFormatting.BLACK); // Black
                    case '1' -> currentStyle.withColor(ChatFormatting.DARK_BLUE); // Dark Blue
                    case '2' -> currentStyle.withColor(ChatFormatting.DARK_GREEN); // Dark Green
                    case '3' -> currentStyle.withColor(ChatFormatting.DARK_AQUA); // Dark Aqua
                    case '4' -> currentStyle.withColor(ChatFormatting.DARK_RED); // Dark Red
                    case '5' -> currentStyle.withColor(ChatFormatting.DARK_PURPLE); // Dark Purple
                    case '6' -> currentStyle.withColor(ChatFormatting.GOLD); // Gold
                    case '7' -> currentStyle.withColor(ChatFormatting.GRAY); // Gray
                    case '8' -> currentStyle.withColor(ChatFormatting.DARK_GRAY); // Dark Gray
                    case '9' -> currentStyle.withColor(ChatFormatting.BLUE); // Blue
                    case 'a' -> currentStyle.withColor(ChatFormatting.GREEN); // Green
                    case 'b' -> currentStyle.withColor(ChatFormatting.AQUA); // Aqua
                    case 'c' -> currentStyle.withColor(ChatFormatting.RED); // Red
                    case 'd' -> currentStyle.withColor(ChatFormatting.LIGHT_PURPLE); // Light Purple
                    case 'e' -> currentStyle.withColor(ChatFormatting.YELLOW); // Yellow
                    case 'f' -> currentStyle.withColor(ChatFormatting.WHITE); // White
                    case 'k' -> currentStyle.withObfuscated(true); // Obfuscated
                    case 'l' -> currentStyle.withBold(true);         // Bold
                    case 'm' -> currentStyle.withStrikethrough(true);  // Strikethrough
                    case 'n' -> currentStyle.withUnderlined(true);      // Underline
                    case 'o' -> currentStyle.withItalic(true);         // Italic
                    case 'r' -> Style.EMPTY;                           // Reset
                    default -> currentStyle;
                };
                i += 2; // Consume the formatting code
                continue;
            }

            // Normal character: append it with the current style.
            finalText.append(Component.literal(String.valueOf(c)).setStyle(currentStyle));
            i++;
        }

        return finalText;
    }
}
