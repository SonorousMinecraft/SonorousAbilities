package com.sereneoasis;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.Archetype;
import com.sereneoasis.displays.SerenityBoard;
import com.sereneoasis.storage.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sakrajin
 * Represents a player within the context of serenity.
 * Used to create/retrieve and update a players data from the database.
 * Handles everything related to abilities and a player.
 */
public class SerenityPlayer {

    private static final Map<UUID,SerenityPlayer> SERENITY_PLAYER_MAP = new ConcurrentHashMap<>();

    public static Map<UUID, SerenityPlayer> getSerenityPlayerMap() {
        return SERENITY_PLAYER_MAP;
    }

    public static SerenityPlayer getSerenityPlayer(Player player)
    {
        return SERENITY_PLAYER_MAP.get(player.getUniqueId());
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
        return getAbilities().get(player.getInventory().getHeldItemSlot() + 1);
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Archetype archetype;

    public Archetype getArchetype() {
        return archetype;
    }

    public void setArchetype(Archetype archetype) {
        this.archetype = archetype;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private Player player;

    public SerenityPlayer(String name, HashMap<Integer,String>abilities, Archetype archetype, Player player)
    {
        this.name = name;
        this.abilities = abilities;
        this.archetype = archetype;
        this.player = player;
    }

    public static void loadPlayer(UUID uuid, Player player)
    {
        if (SERENITY_PLAYER_MAP.containsKey(uuid)) {
            return;
        }
        HashMap<Integer, String> abilities = new HashMap<>();

        if (Serenity.getRepository().get(uuid) == null) {
            for (int i = 1; i <= 9; i++) {
                abilities.put(i, ChatColor.DARK_GRAY + "=-=-Slot" + i + "-=-=");
            }

            SerenityPlayer serenityPlayer = new SerenityPlayer(player.getName(), abilities, Archetype.NONE, player);
            getSerenityPlayerMap().put(uuid, serenityPlayer);
            upsertPlayer(serenityPlayer);

        }
        else{
            Serenity.getRepository().getAsync(uuid).thenAsync( (PlayerData) -> {
                SerenityPlayer serenityPlayer = new SerenityPlayer(PlayerData.getName(), PlayerData.getAbilities(), Archetype.valueOf(PlayerData.getArchetype()), player);
                getSerenityPlayerMap().put(uuid, serenityPlayer);
            });
        }
    }


    public static void upsertPlayer(SerenityPlayer serenityPlayer)
    {

        PlayerData playerData = new PlayerData();
        playerData.setKey(serenityPlayer.getPlayer().getUniqueId());
        playerData.setName(serenityPlayer.getName());

        playerData.setAbilities(serenityPlayer.getAbilities());

        playerData.setArchetype(serenityPlayer.getArchetype().toString());
        Serenity.getRepository().upsertAsync(playerData);
    }


    protected final Map<String, Long> cooldowns = new HashMap<>();

    public void removeOldCooldowns() {
        Iterator<Map.Entry<String, Long>> iterator = this.cooldowns.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if (System.currentTimeMillis() >= entry.getValue()) {
                SerenityBoard board = SerenityBoard.getByPlayer(player);
                if (board == null)
                {
                    return;
                }
                for (int i = 1; i<=9; i++)
                {
                    if (abilities.get(i).equals(entry.getKey()))
                    {
                        board.setAbilitySlot(i, entry.getKey());
                    }
                }
                    iterator.remove();
            }
        }
    }

    public void addCooldown(final String ability, final long cooldown) {
        if (cooldown <= 0) {
            return;
        }
        SerenityBoard board = SerenityBoard.getByPlayer(player);
        if (board == null)
        {
            return;
        }
        for (int i = 1; i<=9; i++)
        {
            if (abilities.get(i).equals(ability))
            {
                board.setAbilitySlot(i, ChatColor.STRIKETHROUGH + ability);
            }
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
