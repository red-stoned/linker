package com.redstoned.linker;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.redstoned.linker.util.TextUtil;

import java.util.Iterator;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public abstract class MessageEvents {
    public static void register() {

        ClientReceiveMessageEvents.ALLOW_CHAT.register((text, signedMessage, gameProfile, parameters, instant) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            player.sendMessage(restructureText(text), false);

            return false;
        });

        ClientReceiveMessageEvents.MODIFY_GAME.register((message, sender) -> {
            return restructureText(message);
        });
    }

    private static MutableText restructureText(Text message) {
        MutableText text = null;
        Style style = message.getStyle();
        List<Text> noStyle = message.getWithStyle(style);
        if (noStyle.size() == 1 && noStyle.get(0).equals(message)) {
            Style ts = message.getStyle();
            if (message.getLiteralString() == null) {
                return (MutableText) matchMarkdownSyntax((MutableText)message, ts);
            }
            return (MutableText) matchMarkdownSyntax((MutableText)message, ts);
        }
        for (Text t : noStyle) {
            if (text == null) {
                text = restructureText(t);
                continue;
            }
            text.append(restructureText(t));
        }
        return text.setStyle(style);
    }

     /**
     * Looks for occurence of []c() where c is some character that indicates 
     * what to do with the text in both sections
     * @see MessageInterpreter#handleMarkdownInstance(String, String, String, String, Matcher)
     */
    private static MutableText matchMarkdownSyntax(MutableText msg, Style style) {
        String msgString = msg.getString();
        String patternString = "\\[(.*?)\\]([^(]?)\\((.+?)\\)|(https?://\\S+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(msgString);
        List<MutableText> textArray = new ArrayList<>();
        
        int lastMatchEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastMatchEnd) {
                textArray.add(Text.literal(msgString.substring(lastMatchEnd, matcher.start())));
            }
            lastMatchEnd = matcher.end();

            String fullMatch = matcher.group(0);
            String bracketText = matcher.group(1);
            String actionChar = matcher.group(2);
            String parenText = matcher.group(3);
            textArray.add(handleMarkdownInstance(fullMatch, bracketText, actionChar, parenText, matcher, style.withParent(style)));
        }

        if (lastMatchEnd == 0) {
            return msg;
        }

        if (lastMatchEnd < msgString.length()) {
            textArray.add(Text.literal(msgString.substring(lastMatchEnd)));
        }

        return ListToText(textArray);
    }

    /**
     * Handles a single instance of markdown syntax:
     * [bracketText]actionChar(parenText)
     * @param btext - Text inside brackets
     * @param c - Action character
     * @param ptext - Text inside parentheses
     * @param matcher - Matcher object (to replace the markdown syntax with a key)
     */
    private static MutableText handleMarkdownInstance(String fmatch, String btext, String c, String ptext, Matcher matcher, Style style) {
        if (ptext == null) {
            btext = fmatch;
            c = "";
            ptext = fmatch;
        }
        if (btext.equals("")) {
            btext = ptext;
        }
        // default
        if (c.equals("")) { 
            if (ptext.startsWith("/")) {
                c = "s";
            } else if (ptext.startsWith("http")) {
                c = "l";
            } else {
                c = "c";
            }
        }

        if (c.equals("l")) { 
            return TextUtil.hyperlinkText(ptext, Text.literal(btext).setStyle(style.withUnderline(true)));
        } 
        // Clickable link
        else if (c.equals("c")) {
            return TextUtil.textToCopy(ptext, Text.literal(btext).setStyle(style.withUnderline(true)), "Copy: " + ptext);
        } 
        // Run command 
        else if (c.equals("r")) {
            return TextUtil.runCommandText(btext, ptext);
        } 
        // Put command in textbox
        else if (c.equals("s")) {
            return TextUtil.suggestCommandText(btext, ptext);
        }
        
        return Text.literal(fmatch);
    }

    /**
     * Converts a list of MutableText objects into a single MutableText object
     */
    private static MutableText ListToText(List<MutableText> list) {
        Iterator<MutableText> textIterator = list.iterator();

        MutableText text = textIterator.next();
        while (textIterator.hasNext()) {
            text.append(textIterator.next());
        }

        return text;
    }
}
