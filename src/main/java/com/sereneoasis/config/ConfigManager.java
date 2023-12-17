package com.sereneoasis.config;


import com.sereneoasis.archetypes.Archetype;
import com.sereneoasis.ability.ComboManager;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.ClickType;

import java.util.*;

import static com.sereneoasis.archetypes.Archetype.SKY;
import static com.sereneoasis.archetypes.Archetype.WAR;

/**
 * @author Sakrajin
 * Used to create the configs for Archetypes and initialise defaults
 */
public class ConfigManager {

    private static final Map<Archetype, ConfigFile> configs = new HashMap<>();

    public ConfigManager() {
        for (Archetype archetype : Archetype.values())
        {
            ConfigFile config = new ConfigFile(archetype.toString());
            configs.put(archetype, config);
        }

        loadConfig();
    }

    public static ConfigFile getConfig(Archetype archetype) {
        return configs.get(archetype);
    }

    private void saveConfigValuesAbility(FileConfiguration config, String name, String archetype, String description, String instructions,
                                  long chargetime, long cooldown, long duration,
                                         double damage, double hitbox, double radius, double range, double speed, double sourceRange)
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

        if (hitbox != 0) {
            config.addDefault(directory + ".hitbox", hitbox);
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

    private void saveConfigValuesCombo(FileConfiguration config, String name, String archetype, String description, String instructions,
                                       long chargetime, long cooldown, long duration,
                                       double damage, double hitbox, double radius, double range, double speed, double sourceRange,
                                       ArrayList<ComboManager.AbilityInformation> abilities)
    {
        String directory = archetype + ".combo." + name;
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

        if (hitbox != 0) {
            config.addDefault(directory + ".hitbox", hitbox);
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
        List<String>storableAbilities = new ArrayList<>();
        for (ComboManager.AbilityInformation abilityInformation : abilities)
        {
            storableAbilities.add(abilityInformation.getName() + ":" + abilityInformation.getClickType().toString());
        }
        config.addDefault(directory + ".usage", storableAbilities);
    }

    private void saveAttributeValuesArchetype(FileConfiguration config, Archetype archetype, double armor, double toughness, double damage,
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

    private void saveArchetypeBlocks(FileConfiguration config, Archetype archetype, Set<Tag<Material>> tags, Set<Material>blocks)
    {
        List<String>tagList = new ArrayList<>();
        tags.forEach(tag -> tagList.add(String.valueOf(tag.getKey())));
        List<String>blockList = new ArrayList<>();
        blocks.forEach(block -> blockList.add(block.toString()));
        config.addDefault(archetype.toString() + ".blocks", blockList);
        config.addDefault(archetype.toString() + ".tags", tagList);
    }

    private void saveArchetypeCosmetics(FileConfiguration config, Archetype archetype, String color)
    {
        String dir = archetype.toString() + ".cosmetics.";
        config.addDefault(dir + ".color", color);
    }

    public void loadConfig() {
        //Archetype config values are added on top of base values (which are below)
        /*
        saveConfigValuesArchetype(ocean, Archetype.NONE, 0, 0, 2, 0, 4, 0.4,0.0, 20, 0.13);

         */



        FileConfiguration none = getConfig(Archetype.NONE).getConfig();

        //Ability configuration

        none.addDefault(Archetype.NONE.toString() + ".blocks", "DIRT");
        saveAttributeValuesArchetype(none, Archetype.NONE, 0, 0, 0, 0,
                0.0, 0, 0);

        saveArchetypeCosmetics(none, Archetype.NONE, "#ffffff");
        none.options().copyDefaults(true);
        getConfig(Archetype.NONE).saveConfig();


        FileConfiguration ocean = getConfig(Archetype.OCEAN).getConfig();


        //Ability configuration
        saveConfigValuesAbility(ocean, "Torrent", Archetype.OCEAN.toString(), "description", "instructions",
                0, 5000, 0,
                2, 0.8, 3, 30, 1.5, 10);

        saveConfigValuesAbility(ocean, "Gimbal", Archetype.OCEAN.toString(), "description", "instructions",
                0, 5000, 0,
                2,0.6, 2.0, 20, 1.7, 10);

        saveConfigValuesAbility(ocean, "Spikes", Archetype.OCEAN.toString(), "description", "instructions",
                0, 5000, 10000,
                4, 0.5,5, 20, 1, 10);

        saveConfigValuesAbility(ocean, "FrostBite", Archetype.OCEAN.toString(), "description", "instructions",
                5000, 5000, 0,
                2, 1.0, 8, 20, 2.0, 10);

        saveConfigValuesAbility(ocean, "GlacierBreath", Archetype.OCEAN.toString(), "description", "instructions",
                2000, 5000, 10000,
                2, 0.5, 2, 10, 1, 0);

        saveConfigValuesAbility(ocean, "Blizzard", Archetype.OCEAN.toString(), "description", "instructions",
                0, 5000, 0,
                1, 0.8, 6, 15, 1.5, 0);

        saveConfigValuesAbility(ocean, "Tsunami", Archetype.OCEAN.toString(), "description", "instructions",
                0, 5000, 5000,
                0, 0, 5, 0, 1.0, 0);

        ArrayList<ComboManager.AbilityInformation> snowStormAbilities = new ArrayList<>();
        snowStormAbilities.add(0, new ComboManager.AbilityInformation("Blizzard", ClickType.SHIFT_LEFT));
        snowStormAbilities.add(1, new ComboManager.AbilityInformation("Torrent", ClickType.LEFT));

        saveConfigValuesCombo(ocean, "SnowStorm", Archetype.OCEAN.toString(), "description", "instructions",
                0, 5000, 5000,
                0, 0, 15, 0, 0.5, 0, snowStormAbilities);

        ArrayList<ComboManager.AbilityInformation> blackIceAbilities = new ArrayList<>();
        blackIceAbilities.add(0, new ComboManager.AbilityInformation("GlacierBreath", ClickType.RIGHT));

        saveConfigValuesCombo(ocean, "BlackIce", Archetype.OCEAN.toString(), "description", "instructions",
                0, 5000, 15000,
                0, 0, 20, 0, 0.5, 5, blackIceAbilities);

        Set<Tag<Material>>oceanTags = new HashSet<>();
        oceanTags.add(Tag.ICE);
        oceanTags.add(Tag.SNOW);
        Set<Material>oceanBlocks = new HashSet<>();
        oceanBlocks.add(Material.WATER);

        saveArchetypeBlocks(ocean, Archetype.OCEAN, oceanTags, oceanBlocks);

        saveAttributeValuesArchetype(ocean, Archetype.OCEAN, 0, 0, 2, 4,
                0.0, 20, 0.13);

        saveArchetypeCosmetics(ocean, Archetype.OCEAN, "#005EB8");

        ocean.options().copyDefaults(true);
        getConfig(Archetype.OCEAN).saveConfig();



        FileConfiguration sun = getConfig(Archetype.SUN).getConfig();

        //Ability configuration
        saveConfigValuesAbility(sun, "CruelSun", Archetype.SUN.toString(), "description", "instructions",
                10000, 5000, 0,
                2, 0.5,2, 20, 1, 0);

        saveConfigValuesAbility(sun, "SolarFlare", Archetype.SUN.toString(), "description", "instructions",
                5000, 5000, 0,
                2, 0.5,5, 0, 1, 10);

        saveConfigValuesAbility(sun, "FlamingRays", Archetype.SUN.toString(), "description", "instructions",
                3000, 5000, 0,
                2, 0.5,0.5, 15, 1, 0);

        saveConfigValuesAbility(sun, "SolarBeam", Archetype.SUN.toString(), "description", "instructions",
                4000, 5000, 0,
                2, 0.5,2.0, 30, 0.5, 0);

        saveConfigValuesAbility(sun, "SunBurst", Archetype.SUN.toString(), "description", "instructions",
                4000, 5000, 0,
                2, 0.5,20.0, 30, 1.0, 0);

        saveConfigValuesAbility(sun, "MeltingGlare", Archetype.SUN.toString(), "description", "instructions",
                0, 5000, 10000,
                2, 0.5,0, 20, 0, 0);

        saveConfigValuesAbility(sun, "SolarBarrage", Archetype.SUN.toString(), "description", "instructions",
                0, 5000, 0,
                2, 2,2, 20, 1, 0);

        saveConfigValuesAbility(sun, "Daybreak", Archetype.SUN.toString(), "description", "instructions",
                0, 5000, 5000,
                2, 2,0, 0, 1, 0);

        saveConfigValuesAbility(sun, "Sunrise", Archetype.SUN.toString(), "description", "instructions",
                0, 5000, 5000,
                2, 2,0, 0, 1, 0);

        sun.addDefault(Archetype.SUN.toString() + ".blocks", "FIRE");
        saveAttributeValuesArchetype(sun, Archetype.SUN, 0, 0, 0, 0,
                0.0, 0, 0);

        saveArchetypeCosmetics(sun, Archetype.SUN, "#FFCC33");
        sun.options().copyDefaults(true);
        getConfig(Archetype.SUN).saveConfig();

        FileConfiguration sky = getConfig(SKY).getConfig();

        //Ability configuration
        saveConfigValuesAbility(sky, "SkyBlast", SKY.toString(), "description", "instructions",
                0, 5000, 0,
                0, 1.0,0.5, 20, 1, 1);

        saveConfigValuesAbility(sky, "Nimbus", SKY.toString(), "description", "instructions",
                0, 5000, 3000,
                0, 0,0, 0, 1, 0);

        saveConfigValuesAbility(sky, "SkyRipper", SKY.toString(), "description", "instructions",
                0, 5000, 0,
                2, 0,0, 30, 1.5, 5);

        saveConfigValuesAbility(sky, "Cyclone", SKY.toString(), "description", "instructions",
                0, 500, 5000,
                0, 0, 2, 0, 1.0, 0);

        saveConfigValuesAbility(sky, "CloudStep", SKY.toString(), "description", "instructions",
                1000, 5000, 8000,
                0, 0,0, 0, 1, 0);

        saveConfigValuesAbility(sky, "HeavenSlash", SKY.toString(), "description", "instructions",
                0, 2000, 0,
                2, 1.0,0, 20, 1, 0);

        saveConfigValuesAbility(sky, "Shocker", SKY.toString(), "description", "instructions",
                0, 2000, 5000,
                2, 1.0,0, 20, 1, 0);

        saveConfigValuesAbility(sky, "ThunderStrike", Archetype.SKY.toString(), "description", "instructions",
                1000, 5000, 0,
                2, 0.5,2.0, 30, 0.5, 0);

        saveAttributeValuesArchetype(sky, SKY, 0, 0, 0, 0,
                0.0, 0, 0.2);

        saveArchetypeCosmetics(sky, SKY, "#BCC8C6");
        sky.options().copyDefaults(true);
        getConfig(SKY).saveConfig();

        FileConfiguration war = getConfig(WAR).getConfig();

        //Ability configuration
        saveConfigValuesAbility(war, "Tether", WAR.toString(), "description", "instructions",
                0, 5000, 0,
                0, 1.0,0.5, 20, 2, 0);

        saveConfigValuesAbility(war, "Jab", WAR.toString(), "description", "instructions",
                0, 2000, 0,
                2, 0,0, 10, 1.5, 0);

        saveConfigValuesAbility(war, "Rocket", WAR.toString(), "description", "instructions",
                0, 5000, 0,
                4, 3.0,0, 30, 0.8, 0);

        saveConfigValuesAbility(war, "Formless", Archetype.WAR.toString(), "description", "instructions",
                2000, 5000, 30000,
                0, 0,0, 0, 1, 0);

        saveConfigValuesAbility(war, "Katana", Archetype.WAR.toString(), "description", "instructions",
                2000, 5000, 30000,
                0, 0,0, 0, 1, 0);

        saveConfigValuesAbility(war, "Spear", WAR.toString(), "description", "instructions",
                0, 5000, 0,
                4, 1.0,0, 30, 1.5, 0);

        saveConfigValuesAbility(war, "Grenades", WAR.toString(), "description", "instructions",
                2000, 1000, 0,
                4, 2.0,0, 30, 1.5, 0);

        saveAttributeValuesArchetype(war, WAR, 0.5, 0.5, 0.5, 0.5,
                1.0, 0.5, 0.1);

        saveArchetypeCosmetics(war, WAR, "#FF8000");
        war.options().copyDefaults(true);
        getConfig(WAR).saveConfig();

    }


}



