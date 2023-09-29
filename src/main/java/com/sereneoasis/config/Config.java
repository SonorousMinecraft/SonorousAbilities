package com.sereneoasis.config;


import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    private static ConfigFile abilityConfig;

    public Config() {
        abilityConfig = new ConfigFile("abilities");
        loadConfig();
    }

    public static FileConfiguration getConfig() {
        return abilityConfig.getConfig();
    }

    private void saveConfigValues(FileConfiguration config, String name, String instructions, String description,
                                  long cooldown, double damage, long duration, double range, long chargetime)
    {
        config.addDefault(name + ".instructions", instructions);
        config.addDefault(name + ".description", description);
        config.addDefault(name + ".cooldown", cooldown);
        config.addDefault(name + ".damage", damage);
        config.addDefault(name + ".duration", duration);
        config.addDefault(name + ".range", range);
        config.addDefault(name + ".chargetime", chargetime);
    }

    public void loadConfig() {
        FileConfiguration config = abilityConfig.getConfig();

        //Ability configuration
       saveConfigValues(config, "AirBlast", "instructions", "descriptions", 5000, 2, 0, 20, 0 );
        config.options().copyDefaults(true);
        abilityConfig.saveConfig();
    }


}



