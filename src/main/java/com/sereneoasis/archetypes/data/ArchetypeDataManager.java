package com.sereneoasis.archetypes.data;

import com.sereneoasis.archetypes.Archetype;
import com.sereneoasis.config.ConfigManager;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.sereneoasis.util.Methods.addTags;

public class ArchetypeDataManager {

    private static final Map<Archetype, ArchetypeData> ARCHETYPE_DATA_MAP = new ConcurrentHashMap<>();

    public static ArchetypeData getArchetypeData(Archetype archetype)
    {
        return ARCHETYPE_DATA_MAP.get(archetype);
    }

    public ArchetypeDataManager() {
        for (Archetype archetype : Archetype.values()) {
            FileConfiguration config = ConfigManager.getConfig(archetype).getConfig();

            ConfigurationSection section = config.getConfigurationSection(archetype.toString() + ".attribute");
            HashMap<Attribute, Double> attributeValues = new HashMap<>();
            for (String att : section.getKeys(false) )
            {
                Attribute attribute = Attribute.valueOf(att);
                attributeValues.put(attribute, section.getDouble(attribute.toString()));
            }
            ConfigurationSection section2 = config.getConfigurationSection(archetype.toString());

            Set<String>archetypeBlocksString = new HashSet<>(section2.getStringList("blocks"));
            addTags(archetypeBlocksString, section2.getStringList("blocks.tags"));
            Set<Material>archetypeBlocks = archetypeBlocksString.stream().map(s -> Material.valueOf(s)).collect(Collectors.toSet());

            ConfigurationSection section3 = config.getConfigurationSection(archetype.toString() + ".cosmetics");
            String color = section3.getString("color");

            ArchetypeData archetypeData = new ArchetypeData(attributeValues, archetypeBlocks, color);
            ARCHETYPE_DATA_MAP.put(archetype, archetypeData);

        }
    }
}
