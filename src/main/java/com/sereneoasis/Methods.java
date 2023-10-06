package com.sereneoasis;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Methods {

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

    public static Block getFacingBlock(Player player, double distance)
    {
        Location loc = player.getLocation();
        Block block = Objects.requireNonNull(Objects.requireNonNull(loc.getWorld()).rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.NEVER)).getHitBlock();
        return block;
    }

    public static Block getFacingBlockOrLiquid(Player player, double distance)
    {
        Location loc = player.getEyeLocation();
        Block block = Objects.requireNonNull(Objects.requireNonNull(loc.getWorld()).rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.ALWAYS)).getHitBlock();
        Bukkit.broadcastMessage("block is " + block.getType().toString());
        return block;
    }

    public static Vector getDirectionBetweenLocations(Location start, Location end)
    {
        return end.subtract(start).toVector().normalize();
    }

    public static List<Block>getBlocksAroundPoint(Location loc, double radius)
    {
        List<Block>blocks = new ArrayList<Block>();
        radius -= radius/2;
        for (double y = -radius ; y < radius ; y++)
        {
            for (double x = -radius ; x < radius ; x++)
            {
                for (double z = -radius ; z < radius ; z++)
                {
                    Block block = loc.clone().add(x,y,z).getBlock();
                    if (block.getLocation().distanceSquared(loc) <  ( (radius*2) * (radius*2)))
                    {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public static List<Block>getBlocksAroundPoint(Location loc, double radius, Material type)
    {
        List<Block>blocks = new ArrayList<Block>();
        radius -= radius/2;
        for (double y = -radius ; y < radius ; y++)
        {
            for (double x = -radius ; x < radius ; x++)
            {
                for (double z = -radius ; z < radius ; z++)
                {
                    Block block = loc.clone().add(x,y,z).getBlock();
                    if (block.getType() == type && block.getLocation().distanceSquared(loc) <  ( (radius*2) * (radius*2)))
                    {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }
}
