package com.sereneoasis.util.methods;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class RayTracing {

    public static boolean playerLookingAt(Player player, BoundingBox target, double maxDistance) {
        BoundingBox boundingBox = target;
        Location loc = player.getEyeLocation().clone();
        Vector dir = player.getEyeLocation().getDirection().clone().normalize();
        RayTraceResult rayTraceResult = boundingBox.rayTrace(loc.toVector(), dir, maxDistance);
        if (rayTraceResult != null) {
            return true;
        }
        return false;
    }
}
