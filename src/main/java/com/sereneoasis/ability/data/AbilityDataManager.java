package com.sereneoasis.ability.data;

import com.sereneoasis.archetypes.Archetype;
import com.sereneoasis.config.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sakrajin
 * Initialises all configuration values for abilities
 */
public class AbilityDataManager {

    private static final Map<String, AbilityData> abilityDataMap = new ConcurrentHashMap<>();

    private static final Map<String, ComboData> comboDataMap = new ConcurrentHashMap<>();

    public static Map<String, ComboData> getComboDataMap()
    {
        return comboDataMap;
    }
    private FileConfiguration config;


    public AbilityDataManager() {

        for (Archetype archetype : Archetype.values()) {

            config = ConfigManager.getConfig(archetype).getConfig();
            if (config.getConfigurationSection(archetype.toString()+ ".ability") != null) {
                for (String ability : config.getConfigurationSection(archetype.toString() + ".ability").getKeys(false)) {
                    ConfigurationSection abil = config.getConfigurationSection(archetype.toString() + ".ability" + "." + ability);
                    AbilityData abilityData = new AbilityData(abil.getString("description"), abil.getString("instructions"),
                            abil.getLong("chargetime"), abil.getLong("cooldown"), abil.getLong("duration"), abil.getDouble("damage"),
                            abil.getDouble("radius"), abil.getDouble("range"), abil.getDouble("speed"), abil.getDouble("sourcerange"));
                    abilityDataMap.put(ability, abilityData);
                }
            }
        }
    }
            //        for (String combo : config.getConfigurationSection(Archetype.OCEAN.toString() + ".combo").getKeys(false))
//        {
//            ConfigurationSection abil = config.getConfigurationSection(Archetype.OCEAN.toString() + ".combo" + "." + combo);
//
//            ArrayList<ComboManager.AbilityInformation> abilities = new ArrayList<>();
//            for (String ability : config.getConfigurationSection(Archetype.OCEAN.toString() + ".combo" + "." + combo + ".usage").getKeys(false))
//            {
//                abilities.add(new ComboManager.AbilityInformation(ability.split(":")[0], ClickType.valueOf(ability.split(":")[1])));
//            }
//
//            ComboData comboData = new ComboData(abil.getString("description") , abil.getString("instructions"),
//                    abil.getLong("chargetime"), abil.getLong("cooldown"),  abil.getLong("duration") , abil.getDouble("damage") ,
//                    abil.getDouble("radius") , abil.getDouble("range") , abil.getDouble("speed"), abilities);
//
//            comboDataMap.put(combo, comboData);
//            abilityDataMap.put(combo, comboData);
//        }
//        }

    public static AbilityData getAbilityData(String ability)
    {
        return abilityDataMap.get(ability);
    }

    public static boolean isAbility(String ability) {
        if (abilityDataMap.containsKey(ability)) {
            return true;
        }
        return false;
    }


    public static List<String> getArchetypeAbilities(Archetype archetype)
    {
        if (archetype != null) {
            List<String> archetypeAbilities = new ArrayList<>();
            for (String ability : abilityDataMap.keySet())
            {
                if (abilityDataMap.get(ability).getArchetype().equals(archetype.toString()))
                {
                    archetypeAbilities.add(ability);
                }
            }
            return archetypeAbilities;
        }
        return null;
    }


}
