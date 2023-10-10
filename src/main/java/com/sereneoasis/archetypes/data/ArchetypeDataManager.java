package com.sereneoasis.archetypes.data;

import com.sereneoasis.archetypes.Archetypes;
import com.sereneoasis.config.ConfigFile;
import com.sereneoasis.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.sereneoasis.util.Methods.addTags;

public class ArchetypeDataManager {

    private static final Map<Archetypes, ArchetypeData> ARCHETYPE_DATA_MAP = new ConcurrentHashMap<>();


    public ArchetypeDataManager() {
        for (Archetypes archetype : Archetypes.values()) {
            FileConfiguration config = ConfigManager.getConfig(archetype).getConfig();

            ConfigurationSection section = config.getConfigurationSection(archetype.toString() + ".attribute");
            HashMap<Attribute, Double> attributeValues = new HashMap<>();
            for (String att : section.getKeys(false) )
            {
                Attribute attribute = Attribute.valueOf(att);
                attributeValues.put(attribute, section.getDouble(attribute.toString()));
            }
            ConfigurationSection section2 = config.getConfigurationSection(archetype.toString());

            Set<String>archetypeBlocks = new HashSet<>();
            addTags(archetypeBlocks, section2.getStringList("blocks"));

            ConfigurationSection section3 = config.getConfigurationSection(archetype.toString() + ".cosmetics.");
            String color = section3.getString("color");

            ArchetypeData archetypeData = new ArchetypeData(attributeValues, archetypeBlocks, color);
            ARCHETYPE_DATA_MAP.put(archetype, archetypeData);

        }
    }
}
