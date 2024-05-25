package com.sereneoasis.config;


import com.sereneoasis.ability.ComboManager;
import com.sereneoasis.archetypes.Archetype;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.ClickType;

import java.util.*;

import static com.sereneoasis.archetypes.Archetype.*;

/**
 * @author Sakrajin
 * Used to create the configs for Archetypes and initialise defaults
 */
public class ConfigManager {

    private static final Map<Archetype, ConfigFile> configs = new HashMap<>();

    public ConfigManager() {
        for (Archetype archetype : Archetype.values()) {
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
                                         double damage, double hitbox, double radius, double range, double speed, double sourceRange, double size) {
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
        if (size != 0) {
            config.addDefault(directory + ".size", size);
        }
    }

    private void saveConfigValuesCombo(FileConfiguration config, String name, String archetype, String description, String instructions,
                                       long chargetime, long cooldown, long duration,
                                       double damage, double hitbox, double radius, double range, double speed, double sourceRange, double size,
                                       ArrayList<ComboManager.AbilityInformation> abilities) {
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
        if (size != 0) {
            config.addDefault(directory + ".size", size);
        }
        List<String> storableAbilities = new ArrayList<>();
        for (ComboManager.AbilityInformation abilityInformation : abilities) {
            storableAbilities.add(abilityInformation.getName() + ":" + abilityInformation.getClickType().toString());
        }
        config.addDefault(directory + ".usage", storableAbilities);
    }

    private void saveAttributeValuesArchetype(FileConfiguration config, Archetype archetype, double armor, double toughness, double damage,
                                              double attackspeed, double kbres,
                                              double health, double speed) {
        String dir = archetype.toString() + ".attribute.";
        config.addDefault(dir + Attribute.GENERIC_ARMOR, armor);
        config.addDefault(dir + Attribute.GENERIC_ARMOR_TOUGHNESS, toughness);
        config.addDefault(dir + Attribute.GENERIC_ATTACK_DAMAGE, damage);
        config.addDefault(dir + Attribute.GENERIC_ATTACK_SPEED, attackspeed);
        config.addDefault(dir + Attribute.GENERIC_KNOCKBACK_RESISTANCE, kbres);
        config.addDefault(dir + Attribute.GENERIC_MAX_HEALTH, health);
        config.addDefault(dir + Attribute.GENERIC_MOVEMENT_SPEED, speed);
    }

    private void saveArchetypeBlocks(FileConfiguration config, Archetype archetype, Set<Tag<Material>> tags, Set<Material> blocks) {
        List<String> tagList = new ArrayList<>();
        tags.forEach(tag -> tagList.add(String.valueOf(tag.getKey())));
        List<String> blockList = new ArrayList<>();
        blocks.forEach(block -> blockList.add(block.toString()));
        config.addDefault(archetype.toString() + ".blocks", blockList);
        config.addDefault(archetype.toString() + ".tags", tagList);
    }

    private void saveArchetypeCosmetics(FileConfiguration config, Archetype archetype, String color) {
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


        FileConfiguration earth = getConfig(EARTH).getConfig();

        Set<Tag<Material>> earthTags = new HashSet<>();
        earthTags.add(Tag.DIRT);
        earthTags.add(Tag.STONE_BRICKS);
        earthTags.add(Tag.SAND);
        earthTags.add(Tag.TERRACOTTA);
        earthTags.add(Tag.CONCRETE_POWDER);
        earthTags.add(Tag.BASE_STONE_OVERWORLD);
        earthTags.add(Tag.BASE_STONE_NETHER);
        Set<Material> earthBlocks = new HashSet<>();
        earthBlocks.add(Material.GRASS_BLOCK);
        earthBlocks.add(Material.GRAVEL);

        saveArchetypeBlocks(earth, EARTH, earthTags, earthBlocks);

        saveConfigValuesAbility(earth, "RockKick", EARTH.toString(), "", "Sneak at a earth block to raise it, left click whilst facing to fire it.",
                0, 2000, 0,
                2, 1.0, 0, 30, 1.0, 10, 0.8);

        saveConfigValuesAbility(earth, "TerraLine", EARTH.toString(), "", "Sneak at an earth block and left click to fire it",
                0, 5000, 20000,
                2, 1.0, 0, 35, 1, 10, 1.0);

        saveConfigValuesAbility(earth, "EarthWall", EARTH.toString(), "description", "Left click an earth block to raise a wall",
                0, 2000, 20000,
                0, 1.5, 0, 6, 5, 10, 1);
        
        ArrayList<ComboManager.AbilityInformation> bulwarkAbilities = new ArrayList<>();
        bulwarkAbilities.add(0, new ComboManager.AbilityInformation("EarthWall", ClickType.SHIFT_LEFT));


        saveConfigValuesCombo(earth, "Bulwark", EARTH.toString(), "description", "Tap sneak with EarthWall to raise walls around you",
                0, 2000, 20000,
                0, 1.5, 0, 6, 5, 10, 1, bulwarkAbilities);

        saveConfigValuesAbility(earth, "StoneShred", Archetype.EARTH.toString(), "description", "Hold sneak whilst facing earth blocks and left click to fire",
                0, 15000, 20000,
                4, 0, 3, 50, 1, 10, 0);

        saveConfigValuesAbility(earth, "TerraSurf", EARTH.toString(), "description", "Left click whilst over earth blocks to surf over, tap sneak to disable",
                0, 5000, 10000,
                0, 0, 3, 0, 1.0, 0, 0.5);

        saveConfigValuesAbility(earth, "Catapult", EARTH.toString(), "description", "Hold sneak until charged while over earth blocks and release to launch yourself",
                0, 1000, 0,
                0, 0, 8, 0, 1, 0, 0.5);

        saveConfigValuesAbility(earth, "EarthQuake", EARTH.toString(), "description", "Hold sneak until charged while over earth blocks to cause an EarthQuake or fall from a great height",
                3000, 10000, 0,
                2, 1.0, 12, 0, 1, 0, 1.0);

        saveConfigValuesAbility(earth, "TectonicWave", EARTH.toString(), "description", "Tap sneak on an earth block and left click to send a wave",
                0, 3000, 0,
                2, 1.0, 8, 30, 1, 10, 1.0);

        saveConfigValuesAbility(earth, "RockRing", EARTH.toString(), "description", "Left click while facing an earth block to cause a ring of rocks to circle you, left click to fire",
                0, 5000, 0,
                2, 0.9, 3, 40, 1.3, 10, 0.9);

        saveAttributeValuesArchetype(earth, EARTH, 0, 0, 0, 0,
                0.0, 0, 0);

        saveArchetypeCosmetics(earth, EARTH, "#50C878");
        earth.options().copyDefaults(true);
        getConfig(EARTH).saveConfig();




        FileConfiguration chaos = getConfig(CHAOS).getConfig();
        saveArchetypeCosmetics(chaos, CHAOS, "#C5B4E3");
        chaos.options().copyDefaults(true);

        saveAttributeValuesArchetype(chaos, CHAOS, 0, 0, 0, 0,
                0.0, 0, 0);

        saveConfigValuesAbility(chaos, "ShadowStep", CHAOS.toString(), "description", "Left click to teleport after a brief delay",
                2000, 10000, 0,
                0, 0, 8, 30, 0, 0, 0);

        saveConfigValuesAbility(chaos, "Singularity", CHAOS.toString(), "description", "Hold sneak to forcefully pull in blocks towards your Singularity \n and left click to fire",
                3000, 15000, 20000,
                4, 4, 3, 20, 1, 5, 0.3);

        saveConfigValuesAbility(chaos, "AbyssalFall", CHAOS.toString(), "description", "Tap sneak to jump a great height and float momentarily, \n left click to dive at your target and raise blocks from the ground",
                0, 5000, 20000,
                0, 0, 30, 0, 2, 0, 1.0);

        saveConfigValuesAbility(chaos, "Limbo", CHAOS.toString(), "description", "Left click to toggle, hold sneak to charge a jump and release to fire",
                5000, 30000, 60000,
                0, 0, 0, 0, 1, 0, 0);

        saveConfigValuesAbility(chaos, "VoidChasm", CHAOS.toString(), "description", "Left click to create a sphere of darkness \n which you can rip the walls out from to launch at entities",
                0, 30000, 30000,
                2, 0.5, 40, 40, 1, 0, 1.0);

        saveConfigValuesAbility(chaos, "SoulSlash", CHAOS.toString(), "description", "Left click to create a blade which cuts through terrain \n and causes blocks to circle you, hold sneak to fire the blocks",
                0, 3000, 0,
                1, 1.0, 4, 30, 1.5, 0, 0.5);

        saveConfigValuesAbility(chaos, "ChaoticVoid", CHAOS.toString(), "description", "Left click to enable and create portals to enter a pocket dimension in which you can create rifts",
                0, 3000, 60000,
                0, 0, 3, 0, 0, 30, 0);

        saveConfigValuesAbility(chaos, "Supernova", CHAOS.toString(), "description", "Left click to create a Singularity, hold sneak to bring it towards you \n use crystals to explode blocks by left clicking while sneaking or fire by left clicking",
                0, 10000, 30000,
                2, 0.5, 5, 100, 1.5, 0, 0.3);

        saveConfigValuesAbility(chaos, "PhantomBreath", CHAOS.toString(), "description", "Hold sneak to breathe, aim at the floor to launch yourself up and \n left click to pull all hit blocks towards you and use as a projectile",
                0, 10000, 30000,
                2, 0.5, 0, 10, 1.5, 0, 0.5);

        getConfig(CHAOS).saveConfig();


        FileConfiguration sun = getConfig(Archetype.SUN).getConfig();

        //Ability configuration

        saveConfigValuesAbility(sun, "CruelSun", Archetype.SUN.toString(), "description", "Hold sneak to charge a sun and bring it towards you, \n left click to fire and melt blocks.",
                3000, 5000, 30000,
                2, 0.5, 3, 60, 2.0, 0, 0.5);

        saveConfigValuesAbility(sun, "SolarFlare", Archetype.SUN.toString(), "description", "Hold sneak while facing a block to cause \n a ring of death to fall on opponents",
                2000, 5000, 0,
                2, 1.0, 20, 0, 1.2, 10, 0.5);

        saveConfigValuesAbility(sun, "FlamingRays", Archetype.SUN.toString(), "description", "Hold sneak to charge rays of fire, \nleft click to shoot these",
                3000, 5000, 0,
                2, 2.0, 1.2, 40, 1.5, 0, 0.5);

        saveConfigValuesAbility(sun, "SolarBeam", Archetype.SUN.toString(), "description", "Hold sneak to charge a ray above you \n which will shine wherever you look, melting both terrain and opponents",
                4000, 5000, 20000,
                1, 1.0, 2.0, 40, 0.5, 0, 0.2);

        saveConfigValuesAbility(sun, "SunBurst", Archetype.SUN.toString(), "description", "Hold sneak to materialise a huge sun which will fire wherever you are looking",
                4000, 5000, 0,
                2, 10.0, 10.0, 100, 1.0, 0, 1.2);

        saveConfigValuesAbility(sun, "MeltingGlare", Archetype.SUN.toString(), "description", "Hold sneak to shoot eye lasers",
                0, 5000, 10000,
                1, 0.5, 0, 20, 0, 0, 0.15);

        saveConfigValuesAbility(sun, "SolarBarrage", Archetype.SUN.toString(), "description", "Tap sneak to create miniature suns orbiting you, \n left click once they are close enough to fire them off in close succession",
                0, 5000, 0,
                2, 2, 1, 40, 1, 0, 0.4);

        saveConfigValuesAbility(sun, "Daybreak", Archetype.SUN.toString(), "description", "Hold sneak and left click to fly forward and melt terrain and burn opponents",
                0, 5000, 10000,
                1, 2, 4, 0, 1.2, 0, 0.2);

        saveConfigValuesAbility(sun, "Sunrise", Archetype.SUN.toString(), "description", "Left click to launch yourself forward, and start \n floating, alternatively tap sneak to disable",
                2000, 10000, 30000,
                0, 0, 1.2, 0, 4, 0, 0);


        sun.addDefault(Archetype.SUN.toString() + ".blocks", "FIRE");
        saveAttributeValuesArchetype(sun, Archetype.SUN, 0, 0, 0, 0,
                0.0, 0, 0);

        saveArchetypeCosmetics(sun, Archetype.SUN, "#f9d71c");
        sun.options().copyDefaults(true);
        getConfig(Archetype.SUN).saveConfig();


        FileConfiguration sky = getConfig(SKY).getConfig();

        //Ability configuration
        saveConfigValuesAbility(sky, "SkyBlast", SKY.toString(), "description", "Tap sneak to select a source, left click to redirect",
                0, 5000, 0,
                0, 1.0, 0.5, 40, 1, 1, 0.2);

        saveConfigValuesAbility(sky, "Nimbus", SKY.toString(), "description", "Left click to fly in the direction you are looking \n sneak and left click to cancel",
                0, 5000, 60000,
                0, 0, 1, 10, 1, 0, 0.2);

        saveConfigValuesAbility(sky, "SkyRipper", SKY.toString(), "description", "Hold sneak and click twice to select \n 2 points, release to rip the sky",
                0, 5000, 0,
                2, 0, 0, 30, 1.5, 5, 0.2);

        saveConfigValuesAbility(sky, "Cyclone", SKY.toString(), "description", "Left click to ride a Cyclone, \n tap sneak to disable",
                0, 500, 5000,
                0, 0, 2.5, 0, 1.0, 0, 0.2);

        saveConfigValuesAbility(sky, "CloudStep", SKY.toString(), "description", "Left click to enable or toggle between floating \n  or walking on clouds and hold sneak to jump",
                1000, 5000, 60000,
                0, 0, 2, 0, 1, 0, 0.2);

        saveConfigValuesAbility(sky, "HeavenSlash", SKY.toString(), "description", "Left click to shoot a Slash of air",
                0, 2000, 0,
                2, 1.0, 0, 20, 1, 0, 0.1);

        saveConfigValuesAbility(sky, "Shocker", SKY.toString(), "description", "Hold sneak to shock your opponents",
                0, 2000, 20000,
                2, 1.0, 0, 10, 1, 0, 0.2);

        saveConfigValuesAbility(sky, "ThunderStrike", Archetype.SKY.toString(), "description", "Tap sneak to launch yourself in the air \n and left click to fire a strike of thunder off.",
                2000, 5000, 0,
                2, 2.0, 2.0, 100, 1.0, 0, 1.2);

        saveConfigValuesAbility(sky, "LightningBolts", Archetype.SKY.toString(), "description", "Tap sneak to create a bolt of lightning, \n left click to fire this at opponents",
                3000, 5000, 0,
                2, 0.5, 1.2, 40, 1.5, 0, 0.5);

        saveAttributeValuesArchetype(sky, SKY, 0, 0, 0, 0,
                0.0, 0, 0.2);

        saveArchetypeCosmetics(sky, SKY, "#BCC8C6");
        sky.options().copyDefaults(true);
        getConfig(SKY).saveConfig();


        FileConfiguration ocean = getConfig(Archetype.OCEAN).getConfig();


        //Ability configuration
        saveConfigValuesAbility(ocean, "Torrent", Archetype.OCEAN.toString(), "description", "Hold sneak whilst facing a block to source it towards you \n and create a ring, left click to fire",
                0, 2000, 0,
                2, 0.9, 3, 40, 1.0, 10, 0.8);

        saveConfigValuesAbility(ocean, "Gimbal", Archetype.OCEAN.toString(), "description", "Hold sneak whilst facing a block to source it towards you \n and create 2 rings, left click to fire",
                0, 5000, 0,
                2, 0.9, 2.0, 40, 1.2, 10, 0.8);

        saveConfigValuesAbility(ocean, "WaterSpout", Archetype.OCEAN.toString(), "description", "Left click to enable a spout of water beneath \n you whilst above water, left click again to disable",
                0, 500, 10000,
                0, 0, 2.0, 10, 1.2, 0, 0.8);

        saveConfigValuesAbility(ocean, "FrostTsunami", OCEAN.toString(), "description", "Tap sneak on an Ocean block and left click to send a wave of ice towards opponents",
                0, 3000, 0,
                2, 1.0, 8, 100, 1, 10, 1.0);

        saveConfigValuesAbility(ocean, "SeaStream", Archetype.OCEAN.toString(), "description", "Hold sneak whilst facing Ocean blocks amd create a \n powerful sphere of water. " +
                        "\n Left click to shoot off ice projectiles, or hold sneak and left click to fire the entire projectile. \n Hold sneak to return it to you  ",
                1000, 2000, 60000,
                2, 0.9, 3, 40, 1.0, 10, 0.8);

        saveConfigValuesAbility(ocean, "SnowShuriken", Archetype.OCEAN.toString(), "description", "Hold sneak to charge Shurikens of Snow, \nleft click to shoot these",
                3000, 5000, 0,
                2, 0.5, 1.2, 40, 1.5, 0, 0.5);

        saveConfigValuesAbility(ocean, "Geyser", Archetype.OCEAN.toString(), "description", "Hold sneak while facing an ocean block or while above one to create an ice formation, \n " +
                        "left click or release sneak to create a geyser",
                1000, 5000, 0,
                0, 0, 4.0, 20, 1.2, 10, 0.8);

        saveConfigValuesAbility(ocean, "OctopusForm", Archetype.OCEAN.toString(), "description", "Tap sneak to enable, hold sneak to switch between modes. Left click to use.",
                1000, 2000, 60000,
                2, 0.9, 3, 40, 1.0, 12, 0.3);

        saveConfigValuesAbility(ocean, "SeaSurf", OCEAN.toString(), "description", "Left click whilst above Ocean blocks to surf over, tap sneak to cancel",
                0, 500, 5000,
                0, 0, 2.5, 0, 1.0, 0, 0.2);

//        saveConfigValuesAbility(ocean, "Iceberg", Archetype.OCEAN.toString(), "description", "instructions",
//                0, 5000, 10000,
//                4, 0.5, 2, 20, 1, 10, 0.4);

//
//        saveConfigValuesAbility(ocean, "WaterWhip", Archetype.OCEAN.toString(), "description", "instructions",
//                0, 5000, 0,
//                2, 1.2, 0, 30, 1.7, 10, 1.2);

//        saveConfigValuesAbility(ocean, "GlacierBreath", Archetype.OCEAN.toString(), "description", "instructions",
//                2000, 5000, 10000,
//                2, 0.5, 2, 10, 1, 0, 0.5);

//        saveConfigValuesAbility(ocean, "Blizzard", Archetype.OCEAN.toString(), "description", "instructions",
//                0, 5000, 0,
//                1, 0.8, 6, 15, 1.5, 0, 0.2);
//
//        saveConfigValuesAbility(ocean, "Tsunami", Archetype.OCEAN.toString(), "description", "instructions",
//                0, 5000, 5000,
//                0, 0, 5, 0, 1.0, 0, 0.4);
//
//        ArrayList<ComboManager.AbilityInformation> snowStormAbilities = new ArrayList<>();
//        snowStormAbilities.add(0, new ComboManager.AbilityInformation("Blizzard", ClickType.SHIFT_LEFT));
//        snowStormAbilities.add(1, new ComboManager.AbilityInformation("Torrent", ClickType.LEFT));
//
//        saveConfigValuesCombo(ocean, "SnowStorm", Archetype.OCEAN.toString(), "description", "instructions",
//                0, 5000, 5000,
//                0, 0, 15, 0, 0.5, 0, 0, snowStormAbilities);
//
//        ArrayList<ComboManager.AbilityInformation> blackIceAbilities = new ArrayList<>();
//        blackIceAbilities.add(0, new ComboManager.AbilityInformation("GlacierBreath", ClickType.RIGHT));
//
//        saveConfigValuesCombo(ocean, "BlackIce", Archetype.OCEAN.toString(), "description", "instructions",
//                0, 5000, 15000,
//                0, 0, 20, 0, 0.5, 5, 0, blackIceAbilities);

        Set<Tag<Material>> oceanTags = new HashSet<>();
        oceanTags.add(Tag.ICE);
        oceanTags.add(Tag.SNOW);
        oceanTags.add(Tag.FLOWERS);
        oceanTags.add(Tag.CROPS);
        oceanTags.add(Tag.LEAVES);
        Set<Material> oceanBlocks = new HashSet<>();
        oceanBlocks.add(Material.WATER);
        oceanBlocks.add(Material.SHORT_GRASS);
        oceanBlocks.add(Material.TALL_GRASS);

        saveArchetypeBlocks(ocean, Archetype.OCEAN, oceanTags, oceanBlocks);

        saveAttributeValuesArchetype(ocean, Archetype.OCEAN, 0, 0, 0, 0,
                0.0, 0, 0.1);

        saveArchetypeCosmetics(ocean, Archetype.OCEAN, "#005EB8");

        ocean.options().copyDefaults(true);
        getConfig(Archetype.OCEAN).saveConfig();



    }


}



