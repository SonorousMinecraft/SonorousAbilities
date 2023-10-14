package com.sereneoasis.util.methods;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Entities {
    public static void applyPotion(Entity e, PotionEffectType effect, int durationinms) {
        ((LivingEntity) e).addPotionEffect(effect.createEffect(durationinms/1000*20, 1));
    }

    public static void applyPotionPlayer(Player player, PotionEffectType effect, int durationinms) {
        PotionEffect ef = new PotionEffect(effect, durationinms/1000*20, 1, false, false, false);
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
    public static Vector setVelocity(LivingEntity target, float speed, double height) {
        Location location = target.getLocation();
        Vector direction = location.getDirection().normalize().multiply(speed);
        if (height != 0) {
            direction.setY(height);
        }
        return direction;
    }

    public static Entity getAffected(Location loc, double radius )
    {
        Entity target = null;
        for (Entity potential : loc.getWorld().getNearbyEntities(loc,radius,radius,radius))
        {
            if (target == null)
            {
                target = potential;
            }
            else{
                if (target.getLocation().distanceSquared(loc) > potential.getLocation().distanceSquared(loc))
                {
                    target = potential;
                }
            }
        }
        return target;
    }

    public static Entity getAffected(Location loc, double radius, Player player )
    {
        Entity target = null;
        for (Entity potential : loc.getWorld().getNearbyEntities(loc,radius,radius,radius))
        {
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

    public static List<Entity> getEntitiesAroundPoint(Location loc, double rad)
    {
        return new ArrayList<>(loc.getWorld().getNearbyEntities(loc, rad, rad, rad,
                entity -> !(entity.isDead() || (entity instanceof Player && ((Player)entity).getGameMode().equals(GameMode.SPECTATOR))
                        || entity instanceof ArmorStand && ((ArmorStand)entity).isMarker())));
    }

}
