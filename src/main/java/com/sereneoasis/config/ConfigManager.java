package com.sereneoasis.config;


import com.sereneoasis.Element;
import com.sereneoasis.ability.ComboManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class ConfigManager {

    private static ConfigFile ocean, nature, sun, war, moon, aether, underworld, time, earth;

    public ConfigManager() {
        ocean = new ConfigFile("Ocean");

        loadConfig();
    }

    public static FileConfiguration getOceanConfig() {
        return ocean.getConfig();
    }

    private void saveConfigValuesAbility(FileConfiguration config, String name, String element, String description, String instructions,
                                  long chargetime, long cooldown, long duration, double damage, double radius, double range, double speed, double sourceRange)
    {
        String directory = element + ".ability." + name;
        config.addDefault(directory + ".description", description);
        config.addDefault(directory + ".instructions", instructions);
        if (chargetime != 0) {
            config.addDefault(directory + ".chargetime", chargetime);
        }
        if (cooldown != 0) {
            config.addDefault(directory + ".cooldown", cooldown);
        }
        if (duration != 0) {
            config.addDefault(directory + ".duration", duration);
        }
        
        if (damage != 0) {
            config.addDefault(directory + ".damage", damage);
        }
        if (radius != 0) {
            config.addDefault(directory + ".radius", radius);
        }
        if (range != 0) {
            config.addDefault(directory + ".range", range);
        }
        if (speed != 0) {
            config.addDefault(directory + ".speed", speed);
        }
        if (sourceRange != 0) {
            config.addDefault(directory + ".sourcerange", speed);
        }
    }

    private void saveConfigValuesCombo(FileConfiguration config, String name, String element, String description, String instructions,
                                       long chargetime, long cooldown, long duration, double damage, double radius, double range, double speed, double sourceRange,
                                       ArrayList<ComboManager.AbilityInformation> abilities)
    {
        String directory = element + ".combo." + name;
        config.addDefault(directory + ".description", description);
        config.addDefault(directory + ".instructions", instructions);
        if (chargetime != 0) {
            config.addDefault(directory + ".chargetime", chargetime);
        }
        if (cooldown != 0) {
            config.addDefault(directory + ".cooldown", cooldown);
        }
        if (duration != 0) {
            config.addDefault(directory + ".duration", duration);
        }

        if (damage != 0) {
            config.addDefault(directory + ".damage", damage);
        }
        if (radius != 0) {
            config.addDefault(directory + ".radius", radius);
        }
        if (range != 0) {
            config.addDefault(directory + ".range", range);
        }
        if (speed != 0) {
            config.addDefault(directory + ".speed", speed);
        }
        if (sourceRange != 0) {
            config.addDefault(directory + ".sourcerange", speed);
        }
        for (ComboManager.AbilityInformation abilityInformation : abilities)
        {
            config.addDefault(directory + ".usage",abilityInformation.getName() + ":" + abilityInformation.getClickType().name());
        }
    }

    public void loadConfig() {
        FileConfiguration config = ocean.getConfig();

        //Ability configuration
        saveConfigValuesAbility(config, "Torrent", Element.OCEAN.name(), "description", "instructions",
                0, 5000, 0,
                2, 0.5, 20, 1, 10 );

        config.options().copyDefaults(true);
        ocean.saveConfig();
    }


}



