/*
 * Copyright (c) 2025.  Tobias Schuster, Kevloe.de
 *
 *  Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 *  You may download and share this code with attribution, but you may not modify it or use it for commercial purposes.
 *
 *  To view a copy of this license, visit:
 * https://creativecommons.org/licenses/by-nc-nd/4.0/
 */

package de.t0bx.basecore.playerutils.scoreboard;

import lombok.Getter;
import org.bukkit.ChatColor;

@Getter
public enum EntryName {
    ENTRY_0(0, ChatColor.BLACK.toString()),
    ENTRY_1(1, ChatColor.DARK_BLUE.toString()),
    ENTRY_2(2, ChatColor.DARK_GREEN.toString()),
    ENTRY_3(3, ChatColor.DARK_AQUA.toString()),
    ENTRY_4(4, ChatColor.DARK_RED.toString()),
    ENTRY_5(5, ChatColor.DARK_PURPLE.toString()),
    ENTRY_6(6, ChatColor.GOLD.toString()),
    ENTRY_7(7, ChatColor.GRAY.toString()),
    ENTRY_8(8, ChatColor.DARK_GRAY.toString()),
    ENTRY_9(9, ChatColor.BLUE.toString()),
    ENTRY_10(10, ChatColor.GREEN.toString()),
    ENTRY_11(11, ChatColor.AQUA.toString()),
    ENTRY_12(12, ChatColor.RED.toString()),
    ENTRY_13(13, ChatColor.LIGHT_PURPLE.toString()),
    ENTRY_14(14, ChatColor.YELLOW.toString()),
    ENTRY_15(15, ChatColor.WHITE.toString()),
    ENTRY_16(16, ChatColor.MAGIC.toString()),
    ENTRY_17(17, ChatColor.BOLD.toString()),
    ENTRY_18(18, ChatColor.STRIKETHROUGH.toString()),
    ENTRY_19(19, ChatColor.UNDERLINE.toString()),
    ENTRY_20(20, ChatColor.ITALIC.toString()),
    ENTRY_21(21, ChatColor.RESET.toString());

    private final int entry;

    private final String entryName;

    EntryName(int entry, String entryName){
        this.entry = entry;
        this.entryName = entryName;
    }
    public static EntryName getEntryNameByScore(int score){
        for (EntryName name : values()) {
            if(name.entry == score){
                return name;
            }
        }
        return null;
    }

}
