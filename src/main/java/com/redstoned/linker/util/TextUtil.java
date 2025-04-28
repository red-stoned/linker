package com.redstoned.linker.util;

import java.util.regex.Pattern;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Util class for all things text
 */
public abstract class TextUtil {
    
    public static MutableText hyperlinkText(String url, MutableText text, String hoverText) {
        return genericTextByAction(text, url, hoverText, ClickEvent.Action.OPEN_URL);
    }

    public static MutableText hyperlinkText(String url, String text, String hoverText) {
        return genericTextByAction(text, url, hoverText, ClickEvent.Action.OPEN_URL);
    }

    public static MutableText hyperlinkText(String url, String text) {
        return hyperlinkText(url, text, "Go to: " + url);
    }

    public static MutableText hyperlinkText(String url, MutableText text) {
        return hyperlinkText(url, text, "Go to: " + url);
    }

    public static MutableText hyperlinkInsideText(String url, String fullText, String subText, String hoverText) {
        String[] splitMessage = fullText.split(Pattern.quote(subText), 2);

        if (splitMessage.length != 2) {
            return Text.literal(fullText);
        }

        return Text.literal(splitMessage[0]).append(hyperlinkText(url, subText, hoverText)).append(splitMessage[1]);
    }
    
    public static MutableText textToCopy(MutableText text, String hoverText) {
        return textToCopy(text.getString(), text, hoverText);
    }

    public static MutableText textToCopy(String ctext, MutableText dtext, String hoverText) {
        return genericTextByAction(dtext, ctext, hoverText, ClickEvent.Action.COPY_TO_CLIPBOARD);
    }

    public static MutableText textToCopy(String text, String hoverText) {
        return textToCopy(text, text, hoverText);
    }

    public static MutableText textToCopy(String ctext, String dtext, String hoverText) {
        return genericTextByAction(dtext, ctext, hoverText, ClickEvent.Action.COPY_TO_CLIPBOARD);
    }

    public static MutableText runCommandText(String text, String command) {
        return genericTextByAction(text, command, "Run command: " + command, ClickEvent.Action.RUN_COMMAND);
    }

    public static MutableText suggestCommandText(String text, String command, String hoverText) {
        return genericTextByAction(text, command, hoverText, ClickEvent.Action.SUGGEST_COMMAND);
    }

    public static MutableText suggestCommandText(String text, String command) {
        return suggestCommandText(text, command, "Put in chat: " + command);
    }

    private static MutableText genericTextByAction(String text, String actionText, String hoverText, ClickEvent.Action action) {
        ClickEvent ce = new ClickEvent(action, actionText);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(hoverText));
        Style style = Style.EMPTY.withClickEvent(ce).withHoverEvent(hoverEvent).withFormatting(Formatting.UNDERLINE);

        return Text.literal(text).setStyle(style);
    }

    private static MutableText genericTextByAction(MutableText text, String actionText, String hoverText, ClickEvent.Action action) {
        ClickEvent ce = new ClickEvent(action, actionText);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(hoverText));
        Style style = text.getStyle().withClickEvent(ce).withHoverEvent(hoverEvent).withFormatting(Formatting.UNDERLINE);
        text.setStyle(style);

        return text.setStyle(style);
    }

    public static MutableText setwithColour(MutableText text, String colour) {
        return text.setStyle(text.getStyle().withColor(Formatting.byName(colour)));
    }
}
