package com.sereneoasis.displays;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

/**
 * @author Sakrajin
 * Handles scoreboards for serenity.
 * Used to store, create, and modify them.
 *
 */
public class SerenityBoard {

    private static HashMap<UUID, SerenityBoard> players = new HashMap<>();

    public static SerenityBoard createScore(Player player) {
        return new SerenityBoard(player);
    }

    public static SerenityBoard getByPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    public static SerenityBoard removeScore(Player player) {
        return players.remove(player.getUniqueId());
    }

    private Scoreboard scoreboard;
    private Objective sidebar;

    private SerenityBoard(Player player) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        sidebar = scoreboard.registerNewObjective("Serenity", Criteria.DUMMY, "Serenity");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName(hex("#d99856 Serenity"));
        // Create Teams

        //Abilities
        for (int i = 1; i <= 9; i++) {
            Team team = scoreboard.registerNewTeam("SLOT_" + -i);
            team.addEntry(genEntry(i));
        }
        // Above text
        for (int i = 10; i <= 14; i++)
        {
            Team team = scoreboard.registerNewTeam("SLOT_" + i);
            team.addEntry(genEntry(i));
        }

        // Combos and text
        for (int i = 15; i <= 19; i++)
        {
            Team team = scoreboard.registerNewTeam("SLOT_" + -i);
            team.addEntry(genEntry(i));
        }
        player.setScoreboard(scoreboard);
        players.put(player.getUniqueId(), this);
    }

//    public void setTitle(String title) {
//        title = ChatColor.translateAlternateColorCodes('&', title);
//        sidebar.setDisplayName(title.length()>32 ? title.substring(0, 32) : title);
//    }

    public void setAbilitySlot(int slot, String text) {
        Team team = scoreboard.getTeam("SLOT_" +- slot);
        String entry = genEntry(slot);
        if(!scoreboard.getEntries().contains(entry)) {
            sidebar.getScore(entry).setScore(-slot);
        }
        text = hex(text);
        String pre = getFirstSplit(text);
        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));

        team.setPrefix(pre);
        team.setSuffix(suf);
    }

    public void setAboveSlot(int slot, String text)
    {
        slot += 9;
        Team team = scoreboard.getTeam("SLOT_" + slot);
        String entry = genEntry(slot);
        if(!scoreboard.getEntries().contains(entry)) {
            sidebar.getScore(entry).setScore(slot-9);
        }

        text = hex(text);
        String pre = getFirstSplit(text);
        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));
        team.setPrefix(pre);
        team.setSuffix(suf);
    }

    public void setBelowSlot(int slot, String text)
    {
        slot += 14;
        Team team = scoreboard.getTeam("SLOT_" + -slot);
        String entry = genEntry(slot);
        if(!scoreboard.getEntries().contains(entry)) {
            sidebar.getScore(entry).setScore( -9 - (slot-14));
        }

        text = hex(text);
        String pre = getFirstSplit(text);
        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));

        team.setPrefix(pre);
        team.setSuffix(suf);
    }

    public String getBelowComboSlot(int slot)
    {
        slot += 14;
        Team team = scoreboard.getTeam("SLOT_" + -slot);
        if (team.getPrefix() != null)
        {
            return team.getPrefix();
        }
        return "";
    }

    public void removeSlot(int slot) {
        String entry = genEntry(slot);
        if(scoreboard.getEntries().contains(entry)) {
            scoreboard.resetScores(entry);
        }
    }


    private String genEntry(int slot) {
        return ChatColor.values()[slot].toString();
    }

    private String getFirstSplit(String s) {
        return s.length()>16 ? s.substring(0, 16) : s;
    }

    private String getSecondSplit(String s) {
        if(s.length()>32) {
            s = s.substring(0, 32);
        }
        return s.length()>16 ? s.substring(16) : "";
    }


    public static String hex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}