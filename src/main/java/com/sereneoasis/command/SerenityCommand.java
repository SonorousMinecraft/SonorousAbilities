package com.sereneoasis.command;

import com.sereneoasis.SerenityBoard;
import com.sereneoasis.SerenityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SerenityCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player)
        {
            SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayerMap().get(player.getUniqueId());
            Bukkit.broadcastMessage(player.getUniqueId().toString());
            if (sPlayer != null) {
                    switch (strings[0])
                    {
                        case "bind":
                            if (strings.length != 2) {
                                return false;
                            }
                            String potentialAbility = strings[1];

                            Bukkit.broadcastMessage("bound");
                            int slot = player.getInventory().getHeldItemSlot();
                            sPlayer.setAbility(slot, strings[1]);
                            SerenityBoard.getByPlayer(player).setSlot(slot, strings[1]);

                            break;

                        case "display":
                            player.sendMessage();
                            break;

                        case "help":

                    }

            }
        }
        return false;
    }
}
