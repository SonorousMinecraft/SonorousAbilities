package com.sereneoasis;

import com.sereneoasis.ability.data.AbilityDataManager;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.Archetype;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.displays.SerenityBoard;
import com.sereneoasis.storage.PlayerData;
import com.sereneoasis.util.methods.Colors;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sakrajin
 * Represents a player within the context of serenity.
 * Used to create/retrieve and update a players data from the database.
 * Handles everything related to abilities and a player.
 */
public class SerenityPlayer {

    private static final Map<UUID, SerenityPlayer> SERENITY_PLAYER_MAP = new ConcurrentHashMap<>();

    public static Map<UUID, SerenityPlayer> getSerenityPlayerMap() {
        return SERENITY_PLAYER_MAP;
    }

    public static SerenityPlayer getSerenityPlayer(Player player) {
        return SERENITY_PLAYER_MAP.get(player.getUniqueId());
    }


    private HashMap<String, HashMap<Integer, String>> presets;

    public void setPreset(String name, HashMap<Integer, String> abilities) {
        HashMap<Integer, String> clonedAbilities = new HashMap<>();
        abilities.forEach((slot, abil) ->
        {
            String tempString = abil;
            clonedAbilities.put(slot, tempString);
        });
        presets.put(name, clonedAbilities);
    }

    public boolean existsPreset(String name) {

        if (presets.containsKey(name)) {
            return true;
        }
        return false;
    }

    public HashMap<Integer, String> getPreset(String name) {
        return presets.get(name);
    }

    public Set<String> getPresetNames() {
        return presets.keySet();
    }

    public HashMap<String, HashMap<Integer, String>> getPresets() {
        return presets;
    }

    public void deletePreset(String name) {
        presets.remove(name);
    }

    private HashMap<Integer, String> abilities;

    public HashMap<Integer, String> getAbilities() {
        return abilities;
    }

    public void setAbilities(HashMap<Integer, String> abilities) {
        this.abilities = abilities;
        SerenityBoard.getByPlayer(player).setAllAbilitySlots(abilities);
    }

    public void setAbility(int slot, String ability) {
        SerenityBoard.getByPlayer(player).setAbilitySlot(slot, ability);
        this.getAbilities().put(slot, ability);
    }

    public String getHeldAbility() {
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

    public SerenityPlayer(String name, HashMap<Integer, String> abilities, Archetype archetype, Player player, HashMap<String, HashMap<Integer, String>> presets) {
        this.name = name;
        this.abilities = abilities;
        this.archetype = archetype;
        this.player = player;
        this.presets = presets;
    }

    public static void initialisePlayer(Player player) {
        SerenityPlayer serenityPlayer = SerenityPlayer.getSerenityPlayer(player);
        SerenityBoard board = SerenityBoard.createScore(player, serenityPlayer);
        board.setAboveSlot(1, serenityPlayer.getArchetype().toString());
        board.setAboveSlot(2, "Abilities:");
        board.setBelowSlot(1, "Combos:");
        int slot = 2;

        for (String abil : AbilityDataManager.getArchetypeAbilities(serenityPlayer.getArchetype())) {
            if (AbilityDataManager.getComboDataMap().containsKey(abil)) {
                board.setBelowSlot(slot, abil);
                slot++;
            }
        }
        for (int i : serenityPlayer.getAbilities().keySet()) {
            board.setAbilitySlot(i, serenityPlayer.getAbilities().get(i));
        }
        initialiseAttributePlayer(player, serenityPlayer);
    }

    public static void initialiseAttributePlayer(Player player, SerenityPlayer serenityPlayer) {
        removeAttributePlayer(player, serenityPlayer);
        ArchetypeDataManager.getArchetypeData(serenityPlayer.getArchetype()).getArchetypeAttributes().forEach((attribute, value) ->
        {
            AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID(), "Serenity." + attribute.toString(), value,
                    AttributeModifier.Operation.ADD_NUMBER);

            player.getAttribute(attribute).addModifier(attributeModifier);
        });
    }

    public static void removeAttributePlayer(Player player, SerenityPlayer serenityPlayer) {
        ArchetypeDataManager.getArchetypeData(serenityPlayer.getArchetype()).getArchetypeAttributes().forEach((attribute, value) ->
        {
            player.getAttribute(attribute).getModifiers().forEach(attributeModifier -> {
                player.getAttribute(attribute).removeModifier(attributeModifier);
            });
        });
    }

    public static void loadPlayer(UUID uuid, Player player) {
        if (SERENITY_PLAYER_MAP.containsKey(uuid)) {
            return;
        }
        HashMap<Integer, String> abilities = new HashMap<>();

        if (Serenity.getRepository().get(uuid) == null) {
            for (int i = 1; i <= 9; i++) {
                abilities.put(i, ChatColor.DARK_GRAY + "=-=-Slot" + "_" + i + "-=-=");
            }

            SerenityPlayer serenityPlayer = new SerenityPlayer(player.getName(), abilities, Archetype.NONE, player, new HashMap<>());
            getSerenityPlayerMap().put(uuid, serenityPlayer);
            upsertPlayer(serenityPlayer);

        } else {
            Serenity.getRepository().getAsync(uuid).thenAsync((PlayerData) -> {
                SerenityPlayer serenityPlayer = new SerenityPlayer(PlayerData.getName(), PlayerData.getAbilities(), Archetype.valueOf(PlayerData.getArchetype().toUpperCase()), player, PlayerData.getPresets());
                getSerenityPlayerMap().put(uuid, serenityPlayer);
            });
        }
    }


    public static void upsertPlayer(SerenityPlayer serenityPlayer) {

        PlayerData playerData = new PlayerData();
        playerData.setKey(serenityPlayer.getPlayer().getUniqueId());
        playerData.setName(serenityPlayer.getName());

        playerData.setAbilities(serenityPlayer.getAbilities());

        playerData.setArchetype(serenityPlayer.getArchetype().toString());

        playerData.setPresets(serenityPlayer.getPresets());
        Serenity.getRepository().upsertAsync(playerData);
    }


    protected final Map<String, Long> cooldowns = new HashMap<>();

    public void removeOldCooldowns() {
        Iterator<Map.Entry<String, Long>> iterator = this.cooldowns.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if (System.currentTimeMillis() >= entry.getValue()) {
                SerenityBoard board = SerenityBoard.getByPlayer(player);
                if (board == null) {
                    return;
                }
                String ability = entry.getKey();

                for (int i = 2; i <= 5; i++) {
                    if (board.getBelowComboSlot(i).equalsIgnoreCase(ChatColor.STRIKETHROUGH + ability)) {
                        board.setBelowSlot(i, ability);
                    }
                }

                for (int i = 1; i <= 9; i++) {
                    if (abilities.get(i).equals(ability)) {
                        board.setAbilitySlot(i, ability);
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
        if (board == null) {
            return;
        }
        if (AbilityDataManager.isCombo(ability)) {
            for (int i = 2; i <= 5; i++) {
                if (board.getBelowComboSlot(i).equalsIgnoreCase(ability)) {
                    board.setBelowSlot(i, ChatColor.STRIKETHROUGH + ability);
                }
            }
        } else {
            for (int i = 1; i <= 9; i++) {
                if (abilities.get(i).equals(ability)) {
                    board.setAbilitySlot(i, ChatColor.STRIKETHROUGH + ability);
                }
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

    public boolean canBend(CoreAbility ability) {
        if (this.isOnCooldown(ability.getName())) {
            return false;
        }
        return true;
    }

    public String getStringColor() {
        return ArchetypeDataManager.getArchetypeData(this.getArchetype()).getColor();
    }

    public Color getColor() {
        return Colors.hexToColor(ArchetypeDataManager.getArchetypeData(this.getArchetype()).getColor());
    }


}
