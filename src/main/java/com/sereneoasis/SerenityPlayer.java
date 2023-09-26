package com.sereneoasis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SerenityPlayer {

    private static final Map<UUID,SerenityPlayer> SERENITY_PLAYER_MAP = new ConcurrentHashMap<>();

    public static Map<UUID, SerenityPlayer> getSerenityPlayerMap() {
        return SERENITY_PLAYER_MAP;
    }

    private HashMap<Integer, String> abilities;

    public HashMap<Integer, String> getAbilities() {
        return abilities;
    }

    public void setAbilities(HashMap<Integer, String> abilities) {
        this.abilities = abilities;
    }

    public void setAbility(int slot, String ability)
    {
        this.getAbilities().put(slot, ability);
    }

    public String getHeldAbility()
    {
        return abilities.get(player.getInventory().getHeldItemSlot());
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Element element;

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private Player player;

    public static void loadAsync(UUID uuid, Player player)
    {
        if (SERENITY_PLAYER_MAP.containsKey(uuid))
        {
            return;
        }

        Serenity.getRepository().getAsync(uuid).thenAsync( (PlayerData,exception) -> {
            if (exception != null)
            {
                HashMap<Integer, String>emptyabilities = new HashMap<>();
                for (int i = 1; i<10; i++)
                {
                    emptyabilities.put(i,"unbound");
                }
                insertPlayer(uuid, player.getName(), emptyabilities, Element.NONBENDER);
                loadAsync(uuid, player);
            }
            else {

                SerenityPlayer serenityPlayer = new SerenityPlayer();
                SERENITY_PLAYER_MAP.put(uuid, serenityPlayer);
                serenityPlayer.setName(PlayerData.getName());

                HashMap<Integer, String> abilities = PlayerData.getAbilities().getAbilities();

                serenityPlayer.setAbilities(abilities);

                serenityPlayer.setElement(Element.valueOf(PlayerData.getElement()));
                serenityPlayer.setPlayer(player);


            }
        });
    }

    public static void insertPlayer(UUID uuid, String name, HashMap<Integer,String> abilities, Element element)
    {
        PlayerData playerData = new PlayerData();
        playerData.setKey(uuid);
        playerData.setName(name);

        PlayerDataAbilities playerDataAbilities = new PlayerDataAbilities();
        playerDataAbilities.setAbilities(abilities);
        playerData.setAbilities(playerDataAbilities);

        playerData.setElement(String.valueOf(element));

        Serenity.getRepository().insert(playerData);
    }

    protected final Map<String, Long> cooldowns = new HashMap<>();

    protected void removeOldCooldowns() {
        Iterator<Map.Entry<String, Long>> iterator = this.cooldowns.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if (System.currentTimeMillis() >= entry.getValue()) {
                    iterator.remove();
            }
        }
    }

    public void addCooldown(final String ability, final long cooldown) {
        if (cooldown <= 0) {
            return;
        }
        this.cooldowns.put(ability, cooldown + System.currentTimeMillis());
    }

    public boolean isOnCooldown(final String ability) {
        if (this.cooldowns.containsKey(ability)) {
            return System.currentTimeMillis() < this.cooldowns.get(ability);
        }

        return false;
    }

    public boolean canBend(CoreAbility ability)
    {
        if (this.isOnCooldown(ability.getName()))
        {
            return false;
        }
        return true;
    }

}
