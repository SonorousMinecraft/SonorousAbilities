package com.sereneoasis.config;


import com.sereneoasis.archetypes.Archetypes;
import com.sereneoasis.ability.ComboManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static final Map<Archetypes, ConfigFile> configs = new HashMap<>();

    public ConfigManager() {
        for (Archetypes archetype : Archetypes.values())
        {
            ConfigFile config = new ConfigFile(archetype.toString());
            configs.put(archetype, config);
        }

        loadConfig();
    }

    public static ConfigFile getConfig(Archetypes archetypes) {
        return configs.get(archetypes);
    }

    private void saveConfigValuesAbility(FileConfiguration config, String name, String archetype, String description, String instructions,
                                  long chargetime, long cooldown, long duration, double damage, double radius, double range, double speed, double sourceRange)
    {
        String directory = archetype + ".ability." + name;
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
            config.addDefault(directory + ".sourcerange", sourceRange);
        }
    }

    private void saveConfigValuesCombo(FileConfiguration config, String name, Archetypes archetype, String description, String instructions,
                                       long chargetime, long cooldown, long duration, double damage, double radius, double range, double speed, double sourceRange,
                                       ArrayList<ComboManager.AbilityInformation> abilities)
    {
        String directory = archetype.toString() + ".combo." + name;
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
            config.addDefault(directory + ".sourcerange", sourceRange);
        }
        for (ComboManager.AbilityInformation abilityInformation : abilities)
        {
            config.addDefault(directory + ".usage",abilityInformation.getName() + ":" + abilityInformation.getClickType().toString());
        }
    }

    private void saveConfigValuesArchetype(FileConfiguration config, Archetypes archetype, double armor, double toughness, double damage,
                                           double attackspeed, double kbres,
                                           double health, double speed)
    {
        String dir = archetype.toString() + ".attribute.";
        config.addDefault(dir + Attribute.GENERIC_ARMOR.toString(), armor);
        config.addDefault(dir + Attribute.GENERIC_ARMOR_TOUGHNESS.toString(), toughness);
        config.addDefault(dir + Attribute.GENERIC_ATTACK_DAMAGE.toString(), damage);
        config.addDefault(dir + Attribute.GENERIC_ATTACK_SPEED.toString(), attackspeed);
        config.addDefault(dir + Attribute.GENERIC_KNOCKBACK_RESISTANCE.toString(), kbres);
        config.addDefault(dir + Attribute.GENERIC_MAX_HEALTH.toString(), health);
        config.addDefault(dir + Attribute.GENERIC_MOVEMENT_SPEED.toString(), speed);

    }

    public void loadConfig() {
        FileConfiguration ocean = getConfig(Archetypes.OCEAN).getConfig();

        //Archetype config values are added on top of base values (which are below)
        /*
        saveConfigValuesArchetype(ocean, Archetypes.NONE, 0, 0, 2, 0, 4, 0.4,0.0, 20, 0.13);

         */

        //Ability configuration
        saveConfigValuesAbility(ocean, "Torrent", Archetypes.OCEAN.toString(), "description", "instructions",
                0, 5000, 0,
                2, 0.5, 20, 1, 10);


        //Below are the defaults, if you are going to change then comment it out and copy and paste
        saveConfigValuesArchetype(ocean, Archetypes.OCEAN, 0, 0, 2, 4,
                0.0, 20, 0.13);

        ocean.options().copyDefaults(true);
        getConfig(Archetypes.OCEAN).saveConfig();


        FileConfiguration none = getConfig(Archetypes.NONE).getConfig();

        //Ability configuration


        //Below are the defaults, if you are going to change then comment it out and copy and paste
        saveConfigValuesArchetype(none, Archetypes.NONE, 0, 0, 0, 0,
                0.0, 0, 0);

        ocean.options().copyDefaults(true);
        getConfig(Archetypes.NONE).saveConfig();
    }


}



