package com.blunix.blunixsuicide.util;

import org.bukkit.ChatColor;

public class ColorUtil {

    public static String formatColor(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
