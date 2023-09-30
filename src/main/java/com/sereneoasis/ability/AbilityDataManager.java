package com.sereneoasis.ability;

import com.sereneoasis.Element;
import com.sereneoasis.config.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AbilityDataManager {

    private static final Map<String, AbilityData> abilityDataMap = new ConcurrentHashMap<>();
    private FileConfiguration config;


    public AbilityDataManager()
    {
        config = ConfigManager.getAbilityConfig();
        /*for (Element element : Element.values() )
        {
            for (String ability : config.getConfigurationSection(element.name()).getKeys(false))
            {
                ConfigurationSection abil = config.getConfigurationSection(element.name() + "." + ability);
                AbilityData abilityData = new AbilityData(abil.getString("description") , abil.getString("instructions"),
                        abil.getLong("chargetime"), abil.getLong("cooldown"),  abil.getLong("duration") , abil.getDouble("damage") ,
                        abil.getDouble("radius") , abil.getDouble("range") , abil.getDouble("speed") );
                abilityDataMap.put(ability, abilityData);
            }
        }*/


        for (String ability : config.getConfigurationSection(Element.AIR.name()).getKeys(false))
        {
            ConfigurationSection abil = config.getConfigurationSection(Element.AIR.name() + "." + ability);
            AbilityData abilityData = new AbilityData(abil.getString("description") , abil.getString("instructions"),
                    abil.getLong("chargetime"), abil.getLong("cooldown"),  abil.getLong("duration") , abil.getDouble("damage") ,
                    abil.getDouble("radius") , abil.getDouble("range") , abil.getDouble("speed") );
            abilityDataMap.put(ability, abilityData);
        }
    }

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


    public static List<String> getElementAbilities(Element element)
    {
        if (element != null) {
            List<String> elementAbilities = new ArrayList<>();
            for (String ability : abilityDataMap.keySet())
            {
                if (abilityDataMap.get(ability).getElement().equals(element.name()))
                {
                    elementAbilities.add(ability);
                }
            }
            return elementAbilities;
        }
        return null;
    }
}
