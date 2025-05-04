package com.sonorous.ability.superclasses;

import com.sonorous.SonorousAbilities;
import com.sonorous.SonorousAbilitiesPlayer;
import com.sonorous.ability.data.AbilityData;
import com.sonorous.ability.data.AbilityDataManager;
import com.sonorous.archetypes.Archetype;
import com.sonorous.util.AbilityStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author Sakrajin
 * Serves as a blueprint for abilities.
 * Handles config values, so they are automatically available in subclasses.
 * Used to handle all ability progression, removal and cooldowns.
 */
public abstract class CoreAbility implements Ability {

    private static final Set<CoreAbility> INSTANCES = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static final Set<CoreAbility> REDIRECT_INSTANCES = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static final Set<CoreAbility> COLLISION_INSTANCES = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static final Map<Class<? extends CoreAbility>, Map<UUID, Map<Integer, CoreAbility>>> INSTANCES_BY_PLAYER = new ConcurrentHashMap<>();
    private static final Map<Class<? extends CoreAbility>, Set<CoreAbility>> INSTANCES_BY_CLASS = new ConcurrentHashMap<>();
    private static int idCounter = Integer.MIN_VALUE;
    protected Player player;
    protected SonorousAbilitiesPlayer sPlayer;
    protected Archetype archetype;
    protected long startTime, chargeTime, cooldown, duration;
    protected AbilityStatus abilityStatus;
    protected double damage, hitbox, radius, range, speed, sourceRange, size;
    private int id;

    public CoreAbility(final Player player, String name) {

        this.player = player;
        this.sPlayer = SonorousAbilitiesPlayer.getSereneAbilitiesPlayerMap().get(player.getUniqueId().toString());


        initialiseConfigVariables(AbilityDataManager.getAbilityData(name));

    }

    public static Stream<Pair<CoreAbility, Stream<BoundingBox>>> getAllRedirectInstances() {
        Stream<CoreAbility> redirectAbilities = REDIRECT_INSTANCES.stream()
                .filter(coreAbility -> coreAbility instanceof RedirectAbility);


        Stream<Pair<CoreAbility, Stream<BoundingBox>>> abilityToBoundingBoxes = redirectAbilities.map(coreAbility -> new Pair<CoreAbility, RedirectAbility>(coreAbility, (RedirectAbility) coreAbility))
                .map(pair -> new Pair<CoreAbility, Set<Map.Entry<Location, Double>>>(pair.getA(), pair.getB().getLocs().entrySet()))
                .map(pair -> {
                    return new Pair<CoreAbility, Stream<BoundingBox>>(pair.getA(), pair.getB().stream().map(locationDoubleEntry -> {
                        Location center = locationDoubleEntry.getKey();
                        double radius = locationDoubleEntry.getValue();
                        Location bottom = center.clone().subtract(radius, radius, radius);
                        Location top = center.clone().add(radius, radius, radius);
                        return BoundingBox.of(bottom, top);
                    }));
                });


        return abilityToBoundingBoxes;
    }

    public static void progressAll() throws ReflectiveOperationException {


        for (Player player : Bukkit.getOnlinePlayers()) {

        }
        for (CoreAbility abil : INSTANCES) {
            if (abil.player.isOnline()) {
                abil.progress();
            } else {
                abil.remove();
            }
        }
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

    public static <T extends CoreAbility> T getLatestAbility(final Player player, final Class<T> clazz) {
        final Collection<T> abils = getAbilities(player, clazz);

        T abil = null;
        while (abils.iterator().hasNext()) {

            abil = abils.iterator().next();
        }
        return abil;
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
     * @param clazz  the class for the type of CoreAbilities
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
     * @param clazz  the class for the type of CoreAbility
     */
    public static <T extends CoreAbility> boolean hasAbility(final Player player, final Class<T> clazz) {
        return getAbility(player, clazz) != null;
    }

    public void setRedirectable() {
        REDIRECT_INSTANCES.add(this);
    }

    public void setCollideable() {
        COLLISION_INSTANCES.add(this);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public SonorousAbilitiesPlayer getsPlayer() {
        return sPlayer;
    }

    public AbilityStatus getAbilityStatus() {
        return abilityStatus;
    }

    public void setAbilityStatus(AbilityStatus abilityStatus) {
        this.abilityStatus = abilityStatus;
    }

    public Archetype getArchetype() {
        return archetype;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getChargeTime() {
        return chargeTime;
    }

//    public CoreAbility(final Player player) {
//        this.player = player;
//        this.sPlayer = SereneAbilitiesPlayer.getSereneAbilitiesPlayerMap().get(player.getUniqueId());
//
//        initialiseConfigVariables(AbilityDataManager.getAbilityData(this.getName()));
//    }

    public long getCooldown() {
        return cooldown;
    }

    public long getDuration() {
        return duration;
    }

    public double getDamage() {
        return damage;
    }

    public double getHitbox() {
        return hitbox;
    }

    public double getRadius() {
        return radius;
    }

    public double getRange() {
        return range;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSourceRange() {
        return sourceRange;
    }

    public double getSize() {
        return size;
    }

    protected boolean shouldStart() {
        return SonorousAbilities.getWorldGuardManager().canBend(player) && !CoreAbility.hasAbility(player, this.getClass()) && !sPlayer.isOnCooldown(this.getName());
    }

    protected boolean shouldStartCanHaveMultiple() {
        return SonorousAbilities.getWorldGuardManager().canBend(player) && !sPlayer.isOnCooldown(this.getName());
    }

    private void initialiseConfigVariables(AbilityData abilityData) {

        this.archetype = abilityData.getArchetype();
        this.startTime = System.currentTimeMillis();

        this.chargeTime = abilityData.getChargetime();
        this.cooldown = abilityData.getCooldown();
        this.duration = abilityData.getDuration();

        this.damage = abilityData.getDamage();
        this.hitbox = abilityData.getHitbox();
        this.radius = abilityData.getRadius();
        this.range = abilityData.getRange();
        this.speed = abilityData.getSpeed();
        this.sourceRange = abilityData.getSourceRange();
        this.size = abilityData.getSize();
    }

    public void start() {

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
    }


}