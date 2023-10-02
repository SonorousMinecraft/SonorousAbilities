package com.sereneoasis.ability;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.sereneoasis.Element;
import com.sereneoasis.SerenityPlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public abstract class CoreAbility implements Ability {

    private static final Set<CoreAbility> INSTANCES = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static final Map<Class<? extends CoreAbility>, Map<UUID, Map<Integer, CoreAbility>>> INSTANCES_BY_PLAYER = new ConcurrentHashMap<>();

    private static final Map<Class<? extends CoreAbility>, Set<CoreAbility>> INSTANCES_BY_CLASS = new ConcurrentHashMap<>();

    protected Player player;

    protected SerenityPlayer sPlayer;

    protected Element element;

    protected long chargetime, cooldown, duration;

    protected double damage, radius, range, speed, sourceRange;

    private int id;

    private static int idCounter = Integer.MIN_VALUE;

    public CoreAbility(final Player player) {
        this.player = player;
        this.sPlayer = SerenityPlayer.getSerenityPlayerMap().get(player.getUniqueId());

        AbilityData abilityData = AbilityDataManager.getAbilityData(this.getName());

        this.chargetime = abilityData.getChargetime();
        this.cooldown = abilityData.getCooldown();
        this.duration = abilityData.getDuration();

        this.damage = abilityData.getDamage();
        this.radius = abilityData.getRadius();
        this.range = abilityData.getRange();
        this.speed = abilityData.getSpeed();
        this.sourceRange = abilityData.getSourceRange();
    }




    public void start()
    {
        INSTANCES.add(this);

        final Class<? extends CoreAbility> clazz = this.getClass();
        final UUID uuid = this.player.getUniqueId();
        if (!INSTANCES_BY_PLAYER.containsKey(clazz)) {
            INSTANCES_BY_PLAYER.put(clazz, new ConcurrentHashMap<>());
        }
        if (!INSTANCES_BY_PLAYER.get(clazz).containsKey(uuid)) {
            INSTANCES_BY_PLAYER.get(clazz).put(uuid, new ConcurrentHashMap<>());
        }
        if (!INSTANCES_BY_CLASS.containsKey(clazz)) {
            INSTANCES_BY_CLASS.put(clazz, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        }

        this.id = CoreAbility.idCounter;

        if (idCounter == Integer.MAX_VALUE) {
            idCounter = Integer.MIN_VALUE;
        } else {
            idCounter++;
        }

        INSTANCES_BY_PLAYER.get(clazz).get(uuid).put(this.id, this);
        INSTANCES_BY_CLASS.get(clazz).add(this);

    }

    public static void progressAll()
    {
        for (CoreAbility abil : INSTANCES)
        {
            abil.progress();
        }
    }

    @Override
    public void remove() {
        final Map<UUID, Map<Integer, CoreAbility>> classMap = INSTANCES_BY_PLAYER.get(this.getClass());
        if (classMap != null) {
            final Map<Integer, CoreAbility> playerMap = classMap.get(this.player.getUniqueId());
            if (playerMap != null) {
                playerMap.remove(this.id);
                if (playerMap.isEmpty()) {
                    classMap.remove(this.player.getUniqueId());
                }
            }

            if (classMap.isEmpty()) {
                INSTANCES_BY_PLAYER.remove(this.getClass());
            }
        }
        if (INSTANCES_BY_CLASS.containsKey(this.getClass())) {
            INSTANCES_BY_CLASS.get(this.getClass()).remove(this);
        }

        INSTANCES.remove(this);
        sPlayer.addCooldown(this.getName(), this.cooldown);
    }

    public static void removeAll() {
        for (final Set<CoreAbility> setAbils : INSTANCES_BY_CLASS.values()) {
            for (final CoreAbility abil : setAbils) {
                try {
                    abil.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T extends CoreAbility> T getAbility(final Player player, final Class<T> clazz) {
        final Collection<T> abils = getAbilities(player, clazz);
        if (abils.iterator().hasNext()) {
            return abils.iterator().next();
        }
        return null;
    }

    public static <T extends CoreAbility> Collection<T> getAbilities(final Class<T> clazz) {
        if (clazz == null || INSTANCES_BY_CLASS.get(clazz) == null || INSTANCES_BY_CLASS.get(clazz).size() == 0) {
            return Collections.emptySet();
        }
        return (Collection<T>) CoreAbility.INSTANCES_BY_CLASS.get(clazz);
    }

    /**
     * Returns a Collection of specific CoreAbility instances that were created
     * by the specified player.
     *
     * @param player the player that created the instances
     * @param clazz the class for the type of CoreAbilities
     * @return a Collection of real instances
     */
    public static <T extends CoreAbility> Collection<T> getAbilities(final Player player, final Class<T> clazz) {
        if (player == null || clazz == null || INSTANCES_BY_PLAYER.get(clazz) == null || INSTANCES_BY_PLAYER.get(clazz).get(player.getUniqueId()) == null) {
            return Collections.emptySet();
        }
        return (Collection<T>) INSTANCES_BY_PLAYER.get(clazz).get(player.getUniqueId()).values();
    }

    /**
     * Returns true if the player has an active CoreAbility instance of type T.
     *
     * @param player the player that created the T instance
     * @param clazz the class for the type of CoreAbility
     */
    public static <T extends CoreAbility> boolean hasAbility(final Player player, final Class<T> clazz) {
        return getAbility(player, clazz) != null;
    }


}
