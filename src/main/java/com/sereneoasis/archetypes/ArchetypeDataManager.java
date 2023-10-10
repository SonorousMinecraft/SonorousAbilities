package com.sereneoasis.archetypes;

import com.sereneoasis.archetypes.Archetypes;
import com.sereneoasis.config.ConfigFile;
import com.sereneoasis.config.ConfigManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ArchetypeDataManager {

    private static final Map<Archetypes, Map<Attribute, Double>> ARCHETYPE_DATA_MAP = new ConcurrentHashMap<>();

    public static Map<Attribute, Double> getArchetypeAttributes(Archetypes archetypes)
    {
        return ARCHETYPE_DATA_MAP.get(archetypes);
    }

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
            ARCHETYPE_DATA_MAP.put(archetype, attributeValues);

        }
    }
}
