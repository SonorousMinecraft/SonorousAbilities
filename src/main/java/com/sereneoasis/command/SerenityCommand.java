package com.sereneoasis.command;

import com.sereneoasis.CoreAbility;
import com.sereneoasis.Element;
import com.sereneoasis.SerenityBoard;
import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.airbending.AirAbility;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SerenityCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player)
        {
            SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayerMap().get(player.getUniqueId());
            if (sPlayer != null) {
                    switch (strings[0])
                    {
                        case "bind":
                            if (strings.length == 1)
                            {
                                player.sendMessage("What ability do you want to bind?");
                                return false;
                            }
                            if (strings.length > 2)
                            {
                                player.sendMessage("Too many arguments");
                                return false;
                            }
                            else {
                                String ability = strings[1];
                                if (!CoreAbility.isAbility(ability)) {
                                    player.sendMessage("This isn't an ability.");
                                    return false;
                                }
                                int slot = player.getInventory().getHeldItemSlot()+1;
                                sPlayer.setAbility(slot, ability);
                                SerenityBoard.getByPlayer(player).setSlot(slot, ability);
                                player.sendMessage("Successfully bound " + ability + " to slot " + slot + "." );
                                return true;
                            }

                        case "display":
                            if (strings.length == 1)
                            {
                                player.sendMessage("What element do you want to show abilities for?");
                                return false;
                            }
                            if (strings.length > 2)
                            {
                                player.sendMessage("Too many arguments");
                                return false;
                            }
                            else {
                                String elementString = strings[1].toUpperCase();

                                if (! Arrays.stream(Element.values()).anyMatch(element -> element.toString().equalsIgnoreCase(elementString)))
                                {
                                    player.sendMessage("This isn't an element.");
                                    return false;
                                }
                                Element element = Element.valueOf(elementString);
                                for(CoreAbility abil : CoreAbility.getElementAbilities(element))
                                {
                                    player.sendMessage(abil.getName());
                                }
                                return true;
                            }

                        case "help":
                            if (strings.length == 1)
                            {
                                player.sendMessage("To do help section");
                                return false;
                            }
                            if (strings.length > 2)
                            {
                                player.sendMessage("Too many arguments");
                                return false;
                            }
                            else {
                                String ability = strings[1];
                                if (!CoreAbility.isAbility(ability)) {
                                    player.sendMessage("This isn't an ability.");
                                    return false;
                                }
                                CoreAbility abil = CoreAbility.getAbilityFromString(ability);
                                player.sendMessage(abil.getName() + "\n" + abil.getDescription() + "\n" + abil.getInstructions());
                                return true;
                            }
                    }
            }
        }
        return false;
    }
}
