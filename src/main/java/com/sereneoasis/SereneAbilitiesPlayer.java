package com.sereneoasis;

import com.sereneoasis.ability.data.AbilityDataManager;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.Archetype;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.displays.SereneAbilitiesBoard;
import com.sereneoasis.storage.PlayerData;
import com.sereneoasis.util.equipment.ItemStackUtils;
import com.sereneoasis.util.methods.Colors;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sakrajin
 * Represents a player within the context of serenity.
 * Used to create/retrieve and update a players data from the database.
 * Handles everything related to abilities and a player.
 */
public class SereneAbilitiesPlayer {

    private static final Map<UUID, SereneAbilitiesPlayer> SERENITY_PLAYER_MAP = new ConcurrentHashMap<>();
    protected final Map<String, Long> cooldowns = new HashMap<>();
    private boolean isOn = false;
    private HashMap<String, HashMap<Integer, String>> presets;
    private Set<CoreAbility> flyAbilities = new HashSet<>();
    private HashMap<Integer, String> abilities;
    private String name;
    private Archetype archetype;
    private Player player;

    public SereneAbilitiesPlayer(String name, HashMap<Integer, String> abilities, Archetype archetype, Player player, HashMap<String, HashMap<Integer, String>> presets) {
        this.name = name;
        this.abilities = abilities;
        this.archetype = archetype;
        this.player = player;
        this.presets = presets;
//        this.serenityPlayerEquipment = new SereneAbilitiesPlayerEquipment(this, player);
    }

    public static Map<UUID, SereneAbilitiesPlayer> getSereneAbilitiesPlayerMap() {
        return SERENITY_PLAYER_MAP;
    }

    public static SereneAbilitiesPlayer getSereneAbilitiesPlayer(Player player) {
        return SERENITY_PLAYER_MAP.get(player.getUniqueId());
    }

    public static void initialisePlayer(Player player) {
        SereneAbilitiesPlayer serenityPlayer = SereneAbilitiesPlayer.getSereneAbilitiesPlayer(player);
        SereneAbilitiesBoard board = SereneAbilitiesBoard.createScore(player, serenityPlayer);

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
//        serenityPlayer.createEquipment();
    }

    public static void initialiseAttributePlayer(Player player, SereneAbilitiesPlayer serenityPlayer) {
        removeAttributePlayer(player, serenityPlayer);
        ArchetypeDataManager.getArchetypeData(serenityPlayer.getArchetype()).getArchetypeAttributes().forEach((attribute, value) ->
        {
            AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID(), "SereneAbilities." + attribute.toString(), value,
                    AttributeModifier.Operation.ADD_NUMBER);

            player.getAttribute(attribute).addModifier(attributeModifier);
        });
    }

    public static void removeAttributePlayer(Player player, SereneAbilitiesPlayer serenityPlayer) {
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

        if (SereneAbilities.getRepository().get(uuid) == null) {
            for (int i = 1; i <= 9; i++) {
                abilities.put(i, ChatColor.DARK_GRAY + "=-=-Slot" + "_" + i + "-=-=");
            }

            SereneAbilitiesPlayer serenityPlayer = new SereneAbilitiesPlayer(player.getName(), abilities, Archetype.NONE, player, new HashMap<>());
            getSereneAbilitiesPlayerMap().put(uuid, serenityPlayer);
            upsertPlayer(serenityPlayer);

        } else {
            SereneAbilities.getRepository().getAsync(uuid).thenAsync((PlayerData) -> {
                SereneAbilitiesPlayer serenityPlayer = new SereneAbilitiesPlayer(PlayerData.getName(), PlayerData.getAbilities(), Archetype.valueOf(PlayerData.getArchetype().toUpperCase()), player, PlayerData.getPresets());
                getSereneAbilitiesPlayerMap().put(uuid, serenityPlayer);
            });
        }
    }

    public static void removePlayerFromMap(Player player) {
        SERENITY_PLAYER_MAP.remove(player.getUniqueId());
    }

    public static void upsertPlayer(SereneAbilitiesPlayer serenityPlayer) {

        PlayerData playerData = new PlayerData();
        playerData.setKey(serenityPlayer.getPlayer().getUniqueId());
        playerData.setName(serenityPlayer.getName());

        playerData.setAbilities(serenityPlayer.getAbilities());

        playerData.setArchetype(serenityPlayer.getArchetype().toString());

        playerData.setPresets(serenityPlayer.getPresets());
        SereneAbilities.getRepository().upsertAsync(playerData);
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
//        if (on){
//            serenityPlayerEquipment.switchToSereneAbilities();
//        } else {
//            serenityPlayerEquipment.switchToNormal();
//        }
        isOn = on;
    }

    public void setFly(CoreAbility coreAbility) {
        player.setFlySpeed(0.1F);
        flyAbilities.add(coreAbility);
        player.setAllowFlight(true);
        player.setFlying(true);
    }

    public void removeFly(CoreAbility coreAbility) {
        if (flyAbilities.contains(coreAbility) && flyAbilities.size() == 1) {
            if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
                player.setAllowFlight(true);
            } else {
                player.setAllowFlight(false);
            }
            player.setFlying(false);
        }
        flyAbilities.remove(coreAbility);

    }

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
        return (HashMap<Integer, String>) presets.get(name).clone();
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

    public HashMap<Integer, String> getAbilities() {
        return abilities;
    }

    public void setAbilities(HashMap<Integer, String> abilities) {
        this.abilities = abilities;
        SereneAbilitiesBoard.getByPlayer(player).setAllAbilitySlots(abilities);
    }

    public void setAbility(int slot, String ability) {
        SereneAbilitiesBoard.getByPlayer(player).setAbilitySlot(slot, ability);
        this.getAbilities().put(slot, ability);
    }

//    private SereneAbilitiesPlayerEquipment serenityPlayerEquipment;

    public String getHeldAbility() {
        return getAbilities().get(player.getInventory().getHeldItemSlot() + 1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Archetype getArchetype() {
        return archetype;
    }

    public void setArchetype(Archetype archetype) {
        if (this.archetype != archetype) {
            for (int i = 1; i <= 9; i++) {
                abilities.put(i, ChatColor.DARK_GRAY + "=-=-Slot" + "_" + i + "-=-=");
                SereneAbilitiesBoard.getByPlayer(player).setAbilitySlot(i, ChatColor.DARK_GRAY + "=-=-Slot" + "_" + i + "-=-=");
            }
        }

        this.archetype = archetype;
        createEquipment();
    }

    public void createEquipment() {
        Set<Pair<EquipmentSlot, Material>> equipment = new HashSet<>();
        equipment.add(new Pair<>(EquipmentSlot.HEAD, Material.IRON_HELMET));
        equipment.add(new Pair<>(EquipmentSlot.CHEST, Material.IRON_CHESTPLATE));
        equipment.add(new Pair<>(EquipmentSlot.LEGS, Material.IRON_LEGGINGS));
        equipment.add(new Pair<>(EquipmentSlot.FEET, Material.IRON_BOOTS));
        equipment.add(new Pair<>(EquipmentSlot.HAND, Material.IRON_SWORD));
//        equipment.add(new Pair<>(EquipmentSlot.OFF_HAND, Material.SHIELD));

        equipment.forEach(equipmentSlotMaterialPair -> {
            ItemStackUtils.createSereneAbilitiesEquipment(player, equipmentSlotMaterialPair.getB(), archetype + " " + equipmentSlotMaterialPair.getB().toString(), List.of("test"), archetype.getValue(), equipmentSlotMaterialPair.getA(), archetype.getTrim());
        });
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void removeOldCooldowns() {
        Iterator<Map.Entry<String, Long>> iterator = this.cooldowns.entrySet().iterator();

        if (isOn) {
            while (iterator.hasNext()) {
                Map.Entry<String, Long> entry = iterator.next();
                if (System.currentTimeMillis() >= entry.getValue()) {
                    SereneAbilitiesBoard board = SereneAbilitiesBoard.getByPlayer(player);
                    if (board == null) {
                        return;
                    }
                    String ability = entry.getKey();

                    for (int i = 2; i <= 5; i++) {
                        if (ChatColor.stripColor(board.getBelowComboSlot(i)).equalsIgnoreCase(ability)) {
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
    }

    public void addCooldown(final String ability, final long cooldown) {
        if (cooldown <= 0) {
            return;
        }
        SereneAbilitiesBoard board = SereneAbilitiesBoard.getByPlayer(player);
        if (board == null) {
            return;
        }
        if (AbilityDataManager.isCombo(ability)) {
            for (int i = 2; i <= 5; i++) {
//                Bukkit.broadcastMessage(i+ "\n" + board.getBelowComboSlot(i) + " != " + ability);
                if (ChatColor.stripColor(board.getBelowComboSlot(i)).equalsIgnoreCase(ability)) {
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
