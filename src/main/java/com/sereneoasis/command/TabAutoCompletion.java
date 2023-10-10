package com.sereneoasis.command;

import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.ability.data.AbilityDataManager;
import com.sereneoasis.archetypes.Archetypes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TabAutoCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete( CommandSender commandSender,  Command command,  String s,  String[] strings) {
        if (commandSender instanceof Player player) {
            SerenityPlayer serenityPlayer = SerenityPlayer.getSerenityPlayer(player);

            return switch (strings[0]) {
                case "choose" ->
                        Arrays.stream(Archetypes.values()).map(Archetypes::toString).collect(Collectors.toList());
                case "bind" -> AbilityDataManager.getArchetypeAbilities(serenityPlayer.getArchetype());
                case "display" ->
                        Arrays.stream(Archetypes.values()).map(Archetypes::toString).collect(Collectors.toList());
                default -> null;
            };
        }
        return null;
    }
}
