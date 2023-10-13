package com.sereneoasis.util;

import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.displays.SerenityBoard;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

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
        Location loc = player.getEyeLocation();
        Block block = null;
        if (loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.NEVER) != null)
        {
            block = loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.NEVER).getHitBlock();
        }
        return block;
    }

    public static Block getFacingBlockOrLiquid(Player player, double distance)
    {
        Location loc = player.getEyeLocation();
        Block block = null;
        if (loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.ALWAYS) != null)
        {
            block = loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.ALWAYS).getHitBlock();
        }
        return block;
    }

    public static Vector getDirectionBetweenLocations(Location start, Location end)
    {
        return end.subtract(start).toVector();
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

    public static List<Location>getDisplayEntityLocs(Location loc, double size, double increment)
    {
        List<Location>locs = new ArrayList<>();
        for (double x = -size/2; x <size/2 ; x += increment)
        {
            for (double y = -size/2; y <size/2 ; y += increment)
            {
                for (double z = -size/2; z <size/2 ; z += increment)
                {
                    locs.add(loc.clone().add(x,y,z));
                }
            }
        }
        return locs;
    }

    public static double getAngleBetweenVectors(Vector vec1, Vector vec2)
    {
        double num = vec1.dot(vec2);
        double den = vec1.length() * vec2.length();
        double d = Math.acos(num/den);
        return d;
    }

    public static List<Location> getPivotedLocations(List<Location>locs, Location pivot, Vector dir)
    {
        List<Location>newLocs = new ArrayList<>();
        for (Location loc : locs)
        {
            double angle = getAngleBetweenVectors(Methods.getDirectionBetweenLocations(pivot, loc), dir);
            newLocs.add(pivot.clone().add(dir.clone().rotateAroundY(angle)));
        }
        return newLocs;
    }


    public static Set<Material>getArchetypeBlocks(SerenityPlayer serenityPlayer)
    {
        return ArchetypeDataManager.getArchetypeData(serenityPlayer.getArchetype()).getBlocks();
    }


}
