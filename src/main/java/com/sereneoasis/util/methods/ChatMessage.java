package com.sereneoasis.util.methods;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatMessage {

    public static void sendPlayerMessage(Player player, String string) {
        player.sendMessage(ChatColor.MAGIC.toString() + ChatColor.LIGHT_PURPLE + "========================================" + "\n");
        player.sendMessage(ChatColor.ITALIC.toString() + ChatColor.BOLD.toString() +  string);
//        player.sendMessage(ChatColor.MAGIC.toString() + "========================================" + "\n");
    }

    public static void sendArchetypeMessage(Player player, String string) {
        player.sendMessage(ChatColor.MAGIC.toString() + "========================================" + "\n");
        player.sendMessage(ChatColor.ITALIC.toString() + ChatColor.BOLD.toString() +  string);
        player.sendMessage(ChatColor.MAGIC.toString() + "========================================" + "\n");
    }
}
