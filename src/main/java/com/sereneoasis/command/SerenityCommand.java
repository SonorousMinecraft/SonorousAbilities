package com.sereneoasis.command;

import com.sereneoasis.ability.data.AbilityData;
import com.sereneoasis.ability.data.AbilityDataManager;
import com.sereneoasis.archetypes.Archetype;
import com.sereneoasis.displays.SerenityBoard;
import com.sereneoasis.SerenityPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static com.sereneoasis.listeners.SerenityListener.initialiseAttributePlayer;

/**
 * @author Sakrajin
 * Handles all serenity commands
 */
public class SerenityCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player)
        {
            SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(player);
            if (sPlayer != null) {
                    switch (strings[0])
                    {
                        case "choose":
                            if (strings.length == 1)
                            {
                                player.sendMessage("What archetype do you want to choose?");
                                return false;
                            }
                            if (strings.length > 2)
                            {
                                player.sendMessage("Too many arguments");
                                return false;
                            }
                            else{
                                Archetype archetype = null;
                                for (Archetype archetypes : Archetype.values())
                                {
                                    if (strings[1].equalsIgnoreCase(archetypes.toString()))
                                    {
                                        archetype = archetypes;
                                    }
                                }
                                if (archetype == null)
                                {
                                    player.sendMessage("This is not an archetype!");
                                    return false;
                                }
                                else{
                                    sPlayer.setArchetype(archetype);
                                    SerenityBoard.getByPlayer(player).setAboveSlot(1, archetype.toString());
                                    initialiseAttributePlayer(player, sPlayer);
                                    return true;
                                }
                            }

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
                                if (!AbilityDataManager.isAbility(ability)) {
                                    player.sendMessage("This isn't an ability.");
                                    return false;
                                }
                                int slot = player.getInventory().getHeldItemSlot()+1;
                                sPlayer.setAbility(slot, ability);
                                SerenityBoard.getByPlayer(player).setAbilitySlot(slot, ability);
                                player.sendMessage("Successfully bound " + ability + " to slot " + slot + "." );
                                return true;
                            }

                        case "display":
                            if (strings.length == 1)
                            {
                                player.sendMessage("What archetype do you want to show abilities for?");
                                return false;
                            }
                            if (strings.length > 2)
                            {
                                player.sendMessage("Too many arguments");
                                return false;
                            }
                            else {
                                String archetypeString = strings[1].toUpperCase();

                                if (! Arrays.stream(Archetype.values()).anyMatch(archetype -> archetype.toString().equalsIgnoreCase(archetypeString)))
                                {
                                    player.sendMessage("This isn't an archetype.");
                                    return false;
                                }
                                Archetype archetype = Archetype.valueOf(archetypeString);
                                for(String abil : AbilityDataManager.getArchetypeAbilities(archetype))
                                {
                                    player.sendMessage(abil);
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
                                if (!AbilityDataManager.isAbility(ability)) {
                                    player.sendMessage("This isn't an ability.");
                                    return false;
                                }
                                AbilityData abilityData = AbilityDataManager.getAbilityData(ability);
                                player.sendMessage(ability + "\n" + abilityData.getDescription() + "\n" + abilityData.getInstructions());
                                return true;
                            }
                    }
            }
        }
        return false;
    }
}
