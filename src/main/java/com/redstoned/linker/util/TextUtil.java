package com.redstoned.linker.util;

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

    public static MutableText hyperlinkText(String url, MutableText text) {
        return genericTextByAction(text, url, "Go to: " + url, ClickEvent.Action.OPEN_URL);
    }

    public static MutableText textToCopy(String copyData, MutableText text) {
        return genericTextByAction(text, copyData, "Copy: " + copyData, ClickEvent.Action.COPY_TO_CLIPBOARD);
    }

    public static MutableText runCommandText(String commandString, MutableText text) {
        return genericTextByAction(text, commandString, "Run command: " + commandString, ClickEvent.Action.RUN_COMMAND);
    }

    public static MutableText suggestCommandText(String suggestionString, MutableText text) {
        return genericTextByAction(text, suggestionString, "Autofill with: " + suggestionString, ClickEvent.Action.SUGGEST_COMMAND);
    }

    private static MutableText genericTextByAction(MutableText text, String actionText, String hoverText, ClickEvent.Action action) {
        ClickEvent ce = new ClickEvent(action, actionText);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(hoverText));
        Style style = text.getStyle().withClickEvent(ce).withHoverEvent(hoverEvent).withFormatting(Formatting.UNDERLINE);
        text.setStyle(style);

        return text.setStyle(style);
    }
}
