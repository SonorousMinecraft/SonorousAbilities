package com.sereneoasis.config;


import com.sereneoasis.Element;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private static ConfigFile abilityConfig;

    public ConfigManager() {
        abilityConfig = new ConfigFile("abilities");
        loadConfig();
    }

    public static FileConfiguration getAbilityConfig() {
        return abilityConfig.getConfig();
    }

    private void saveConfigValues(FileConfiguration config, String name, String element, String description, String instructions,
                                  long chargetime, long cooldown, long duration, double damage, double radius, double range, double speed)
    {
        String directory = element + "." + name;
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
    }

    public void loadConfig() {
        FileConfiguration config = abilityConfig.getConfig();

        //Ability configuration
        saveConfigValues(config, "AirBlast", Element.AIR.name(), "description", "instructions",
                0, 5000, 0,
                2, 0.5, 20, 1 );
        config.options().copyDefaults(true);
        abilityConfig.saveConfig();
    }


}



