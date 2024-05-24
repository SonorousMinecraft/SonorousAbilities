package com.sereneoasis.command;

import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.ability.data.AbilityDataManager;
import com.sereneoasis.archetypes.Archetype;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sakrajin
 * Tab auto completion for {@link SerenityCommand commands}
 * Does not work yet (change this if you fix it)
 */
public class TabAutoCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] strings) {
        if (commandSender instanceof Player player) {
            SerenityPlayer serenityPlayer = SerenityPlayer.getSerenityPlayer(player);

            return switch (strings[0]) {
                case "choose", "ch" ->
                    Arrays.stream(Archetype.values()).map(Archetype::toString).collect(Collectors.toList());

                case "bind", "b" ->
                    AbilityDataManager.getArchetypeAbilities(serenityPlayer.getArchetype()).stream().filter(s -> !AbilityDataManager.isCombo(s)).collect(Collectors.toList());
                case "display", "d" ->
                        Arrays.stream(Archetype.values()).map(Archetype::toString).collect(Collectors.toList());
                case "preset", "p" -> switch (strings[1]) {
                    case "create", "c" -> null;
                    case "delete", "d", "list", "l", "bind", "b" ->  serenityPlayer.getPresets().keySet().stream().toList();

                    default -> List.of("create, delete, bind");
                };
                default -> List.of("choose", "display", "help", "preset");
            };
        }
        return null;
    }
}
