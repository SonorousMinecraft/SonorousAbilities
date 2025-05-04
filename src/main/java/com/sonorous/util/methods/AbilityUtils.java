package com.sonorous.util.methods;

import com.sonorous.SonorousAbilitiesPlayer;
import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class AbilityUtils {

    public static void sendActionBar(Player player, String message, ChatColor chatColor) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message, chatColor));
    }

    public static void showCharged(CoreAbility coreAbility) {
        AbilityStatus abilityStatus = coreAbility.getAbilityStatus();
        Player player = coreAbility.getPlayer();
        String name = coreAbility.getName();
        SonorousAbilitiesPlayer sPlayer = SonorousAbilitiesPlayer.getSonorousAbilitiesPlayer(player);
        String heldAbilityName = sPlayer.getHeldAbility();
        if (abilityStatus == AbilityStatus.CHARGED && name.equals(heldAbilityName)) {
            sendActionBar(player, "READY", ChatColor.GREEN);
        }
    }

    public static void showShots(CoreAbility coreAbility, int current, int total) {
        AbilityStatus abilityStatus = coreAbility.getAbilityStatus();
        Player player = coreAbility.getPlayer();
        String name = coreAbility.getName();
        SonorousAbilitiesPlayer sPlayer = SonorousAbilitiesPlayer.getSonorousAbilitiesPlayer(player);
        String heldAbilityName = sPlayer.getHeldAbility();
        if (abilityStatus == AbilityStatus.SHOOTING && name.equals(heldAbilityName)) {
            sendActionBar(player, current + "/" + total, ChatColor.WHITE);
        }
    }
}
