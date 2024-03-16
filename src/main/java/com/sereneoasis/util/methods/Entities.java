package com.sereneoasis.util.methods;

import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sakrajin
 * Methods which are related to entities
 */
public class Entities {
    public static void applyPotion(Entity e, PotionEffectType effect, int durationinms) {
        PotionEffect ef = new PotionEffect(effect, durationinms / 1000 * 20, 1, false, false, false);
        ((LivingEntity) e).addPotionEffect(ef);
    }

    public static void applyPotionPlayer(Player player, PotionEffectType effect, int durationinms) {
        PotionEffect ef = new PotionEffect(effect, durationinms / 1000 * 20, 1, false, false, false);
        player.addPotionEffect(ef);
    }

    public static void applyPotionPlayerAmplifier(Player player, PotionEffectType effect, int amplifier, int durationinms) {
        PotionEffect ef = new PotionEffect(effect, durationinms / 1000 * 20, amplifier, false, false, false);
        player.addPotionEffect(ef);
    }

    /*
     * Used to set the velocity of a player.
     *
     * player = The player which is being manipulated.
     * isForward = Whether the velocity is going forward or backwards.
     * speed = The speed at which the player is being sent.
     * height = The height from their original location the player is shot.
     */
    public static void setVelocity(LivingEntity target, float speed, double height) {
        Location location = target.getLocation().clone();
        Vector direction = location.getDirection().clone().normalize().multiply(speed);
        if (height != 0) {
            direction.setY(height);
        }
        target.setVelocity(direction);
    }

//    public static Collection<Entity>getNearbyEntities(Location loc, double radius){
//        return loc.getWorld().getNearbyEntities(loc, radius, radius, radius).stream().filter(entity -> !(entity instanceof ArmorStand)).collect(Collectors.toSet());
//    }

    public static Entity getAffected(Location loc, double radius) {
        Entity target = null;
        for (Entity potential : getEntitiesAroundPoint(loc, radius)) {
            if (target == null) {
                target = potential;
            } else {
                if (target.getLocation().distanceSquared(loc) > potential.getLocation().distanceSquared(loc)) {
                    target = potential;
                }
            }
        }
        return target;
    }

    public static Entity getAffected(Location loc, double radius, Player player) {
        Entity target = null;
        for (Entity potential : getEntitiesAroundPoint(loc, radius)) {
            if (potential.getUniqueId() != player.getUniqueId()) {
                if (target == null) {
                    target = potential;
                } else {
                    if (target.getLocation().distanceSquared(loc) > potential.getLocation().distanceSquared(loc)) {
                        target = potential;
                    }
                }
            }
        }
        return target;
    }

    public static List<LivingEntity> getAffectedList(Location loc, double radius, Player player) {
        return getEntitiesAroundPoint(loc,radius).stream().filter(entity -> entity != player).filter(entity -> entity instanceof LivingEntity).map(entity -> (LivingEntity)entity).toList();
    }

    public static List<Entity> getEntitiesAroundPoint(Location loc, double rad) {
        return new ArrayList<>(loc.getWorld().getNearbyEntities(loc, rad, rad, rad,
                entity -> !(entity.isDead() || (entity instanceof Player && ((Player) entity).getGameMode().equals(GameMode.SPECTATOR))
                        || entity instanceof ArmorStand || entity instanceof ItemDisplay || entity instanceof BlockDisplay)));
    }


    public static HashMap<Integer, TempDisplayBlock> handleDisplayBlockEntities(HashMap<Integer, TempDisplayBlock> spike, Set<Location> locs, DisplayBlock type, double size) {

        int i = 0;
        for (Location l : locs) {
            l.setDirection(l.getBlock().getLocation().getDirection().clone());

            if (!spike.containsKey(i)) {
                TempDisplayBlock tempDisplayBlock = new TempDisplayBlock(l, type, 50000, size);
                spike.put(i, tempDisplayBlock);
            } else {
                spike.get(i).teleport(l);
            }
            i++;
        }
        if (locs.size() < spike.size()) {
            for (int n = locs.size(); n <= spike.size(); n++) {
                TempDisplayBlock tb = spike.get(n);
                if (tb != null) {
                    spike.get(n).revert();
                }
                spike.remove(n);
            }
        }
        return spike;
    }

    public static HashMap<Integer, TempDisplayBlock> handleDisplayBlockEntities(HashMap<Integer, TempDisplayBlock> spike, Set<Location> locs, Material type, double size) {

        int i = 0;
        for (Location l : locs) {
            if (!spike.containsKey(i)) {
                TempDisplayBlock tempDisplayBlock = new TempDisplayBlock(l, type, 50000, size);
                spike.put(i, tempDisplayBlock);
            } else {
                spike.get(i).teleport(l);
            }
            i++;
        }
        if (locs.size() < spike.size()) {
            for (int n = locs.size(); n < spike.size(); n++) {
                TempDisplayBlock tb = spike.get(n);
                if (tb != null) {
                    spike.get(n).revert();
                }
                spike.remove(n);
            }
        }
        return spike;
    }

    public static HashMap<Integer, TempDisplayBlock> handleDisplayBlockEntities(HashMap<Integer, TempDisplayBlock> spike, Set<Location> locs, double size) {

        int i = 0;
        for (Location l : locs) {
            if (!spike.containsKey(i)) {
                TempDisplayBlock tempDisplayBlock = new TempDisplayBlock(l, Blocks.getBelowBlock(l.getBlock(), 10).getType(), 50000, size);
                spike.put(i, tempDisplayBlock);
            } else {
                spike.get(i).teleport(l);
            }
            i++;
        }
        if (locs.size() < spike.size()) {
            for (int n = locs.size(); n < spike.size(); n++) {
                TempDisplayBlock tb = spike.get(n);
                if (tb != null) {
                    spike.get(n).revert();
                }
                spike.remove(n);
            }
        }
        return spike;
    }

    public static LivingEntity getFacingEntity(Player player, double distance, double hitbox) {
        Location loc = player.getEyeLocation().clone();
        Vector dir = player.getEyeLocation().getDirection().clone().normalize();
        RayTraceResult rayTraceResult = (loc.getWorld().rayTraceEntities(loc, dir, distance, hitbox, entity -> {
            if (entity instanceof LivingEntity && entity != player && ! (entity instanceof ArmorStand)) {
                return true;
            }
            return false;
        }));
        if (rayTraceResult != null) {
            return (LivingEntity) rayTraceResult.getHitEntity();
        }
        return null;
    }

    public static boolean playerLookingAt(Player player, Entity target, double maxDistance) {
        BoundingBox boundingBox = target.getBoundingBox();
        Location loc = player.getEyeLocation().clone();
        Vector dir = player.getEyeLocation().getDirection().clone().normalize();
        RayTraceResult rayTraceResult = boundingBox.rayTrace(loc.toVector(), dir, maxDistance);
        if (rayTraceResult != null) {
            return true;
        }
        return false;
    }

    public static LivingEntity getFacingEntity(Location loc, Vector dir, double distance) {
        if (loc.getWorld().rayTraceEntities(loc, dir, distance) != null) {
            if (loc.getWorld().rayTraceEntities(loc, dir, distance).getHitEntity() instanceof LivingEntity entity  && ! (entity instanceof ArmorStand)) {
                return entity;
            }
        }
        return null;
    }

    public static LivingEntity getEntityBetweenPoints(Location loc1, Location loc2) {
        Vector dir = Vectors.getDirectionBetweenLocations(loc1, loc2);
        return getFacingEntity(loc1, dir.clone().normalize(), dir.length());
    }

    public static Block getCollidedBlock(Entity entity) {
        Location loc = entity.getLocation();
        Vector dir = entity.getVelocity().clone().normalize();
        double distance = 1.0;
        Block block = null;
        if (loc.getWorld().rayTraceBlocks(loc, dir, distance, FluidCollisionMode.NEVER) != null) {
            block = loc.getWorld().rayTraceBlocks(loc, dir, distance, FluidCollisionMode.NEVER).getHitBlock();
        }
        return block;
    }
}



























