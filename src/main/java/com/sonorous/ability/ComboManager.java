package com.sonorous.ability;

import com.google.common.collect.ArrayListMultimap;
import com.sonorous.SonorousAbilitiesPlayer;
import com.sonorous.ability.data.AbilityDataManager;
import com.sonorous.ability.data.ComboData;
import com.sonorous.archetypes.earth.Bulwark;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sakrajin
 * Handles all combo instantiation by keeping track of recently used abilities using {@link ComboData combo data}
 */
public class ComboManager {

    private static Map<Player, ArrayList<AbilityInformation>> RECENTLY_USED = new ConcurrentHashMap<>();

    private static Map<String, ComboData> COMBO_ABILITIES = new HashMap<>();

    public ComboManager() {
        RECENTLY_USED.clear();
        COMBO_ABILITIES.clear();
        COMBO_ABILITIES = AbilityDataManager.getComboDataMap();
    }


    public void removePlayer(Player player) {
        RECENTLY_USED.remove(player);
    }

    public void addRecentlyUsed(Player player, String name, ClickType clickType) {
        AbilityInformation abilityInformation = new AbilityInformation(name, clickType);
        ArrayList<AbilityInformation> recentAbilities = RECENTLY_USED.get(player);
        if (recentAbilities == null) {
            recentAbilities = new ArrayList<>();
        }
        recentAbilities.add(abilityInformation);
        if (recentAbilities.size() > 8) {
            recentAbilities.remove(0);
        }
        RECENTLY_USED.put(player, recentAbilities);

        checkForCombo(player);
    }

    private void checkForCombo(Player player) {
        SonorousAbilitiesPlayer sPlayer = SonorousAbilitiesPlayer.getSonorousAbilitiesPlayer(player);
        for (Map.Entry<String, ComboData> ability : COMBO_ABILITIES.entrySet()) {

            ArrayListMultimap<String, ClickType> recentlyUsedPairList = ArrayListMultimap.create();
            RECENTLY_USED.get(player).stream().forEach(abilityInformation -> recentlyUsedPairList.put(abilityInformation.getName(), abilityInformation.getClickType()));
            ArrayListMultimap<String, ClickType> abilityPairList = ArrayListMultimap.create();
            ability.getValue().getAbilities().stream().forEach(abilityInformation -> abilityPairList.put(abilityInformation.getName(), abilityInformation.getClickType()));


            if (recentlyUsedPairList.entries().containsAll(abilityPairList.entries())) {
                boolean hasDoneAbility = true;
                switch (ability.getKey()) {
                    case "Bulwark":
                        new Bulwark(player);
                        break;
                    default:
                        hasDoneAbility = false;
                        break;
                }
                if (hasDoneAbility) {
                    RECENTLY_USED.replace(player, new ArrayList<>());
                }
            }
        }
    }


    public static class AbilityInformation {

        private String name;
        private ClickType clickType;

        public AbilityInformation(final String name, ClickType clickType) {
            this.name = name;
            this.clickType = clickType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ClickType getClickType() {
            return clickType;
        }

        public void setClickType(ClickType clickType) {
            this.clickType = clickType;
        }
    }


}








