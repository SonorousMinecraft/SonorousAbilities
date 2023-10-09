package com.sereneoasis.ability.data;

import com.sereneoasis.archetypes.Archetypes;
import com.sereneoasis.config.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AbilityDataManager {

    private static final Map<String, AbilityData> abilityDataMap = new ConcurrentHashMap<>();

    private static final Map<String, ComboData> comboDataMap = new ConcurrentHashMap<>();

    public static Map<String, ComboData> getComboDataMap()
    {
        return comboDataMap;
    }
    private FileConfiguration config;


    public AbilityDataManager()
    {

        for (Archetypes archetype : Archetypes.values())
        {
            config = ConfigManager.getConfig(archetype).getConfig();

            for (String ability : config.getConfigurationSection(archetype.toString() + ".ability").getKeys(false))
            {
                ConfigurationSection abil = config.getConfigurationSection(archetype.toString() + ".ability" + "." + ability);
                AbilityData abilityData = new AbilityData(abil.getString("description") , abil.getString("instructions"),
                        abil.getLong("chargetime"), abil.getLong("cooldown"),  abil.getLong("duration") , abil.getDouble("damage") ,
                        abil.getDouble("radius") , abil.getDouble("range") , abil.getDouble("speed"), abil.getDouble("sourcerange"));
                abilityDataMap.put(ability, abilityData);
            }
            //        for (String combo : config.getConfigurationSection(Element.OCEAN.toString() + ".combo").getKeys(false))
//        {
//            ConfigurationSection abil = config.getConfigurationSection(Element.OCEAN.toString() + ".combo" + "." + combo);
//
//            ArrayList<ComboManager.AbilityInformation> abilities = new ArrayList<>();
//            for (String ability : config.getConfigurationSection(Element.OCEAN.toString() + ".combo" + "." + combo + ".usage").getKeys(false))
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


    public static List<String> getElementAbilities(Archetypes archetypes)
    {
        if (archetypes != null) {
            List<String> elementAbilities = new ArrayList<>();
            for (String ability : abilityDataMap.keySet())
            {
                if (abilityDataMap.get(ability).getElement().equals(archetypes.toString()))
                {
                    elementAbilities.add(ability);
                }
            }
            return elementAbilities;
        }
        return null;
    }


}
