package com.redstoned.linker.util;

import java.net.URI;
import java.net.URISyntaxException;

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

    public static MutableText hyperlinkText(URI url, MutableText text) {
        return genericTextByAction(text, "Go to: " + url, new ClickEvent.OpenUrl(url));
    }

    public static MutableText textToCopy(String copyData, MutableText text) {
        return genericTextByAction(text, "Copy: " + copyData, new ClickEvent.CopyToClipboard(copyData));
    }

    public static MutableText runCommandText(String commandString, MutableText text) {
        return genericTextByAction(text, "Run command: " + commandString, new ClickEvent.RunCommand(commandString));
    }

    public static MutableText suggestCommandText(String suggestionString, MutableText text) {
        return genericTextByAction(text, "Autofill with: " + suggestionString, new ClickEvent.SuggestCommand(suggestionString));
    }

    private static MutableText genericTextByAction(MutableText text, String hoverText, ClickEvent ce) {
        HoverEvent hoverEvent = new HoverEvent.ShowText(Text.literal(hoverText));
        Style style = Style.EMPTY.withClickEvent(ce).withHoverEvent(hoverEvent).withFormatting(Formatting.UNDERLINE);

        return text.setStyle(style);
    }
}
