package com.sereneoasis.util.methods;

import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Methods which are related to vectors
 * Velocity is in units of 1/8000 of a block per server tick (50ms); for example, -1343 would move (-1343 / 8000) = −0.167875 blocks per tick (or −3.3575 blocks per second).
 */
public class Vectors {

    public static Vector getDirectionBetweenLocations(Location start, Location end) {
        return end.clone().subtract(start.clone()).toVector();
    }

    public static double getAngleBetweenVectors(Vector vec1, Vector vec2) {
        double num = vec1.dot(vec2);
        double den = vec1.length() * vec2.length();
        double d = Math.acos(num / den);
        return d;
    }

    public static boolean isObstructed(final Location location1, final Location location2) {
        final Vector loc1 = location1.toVector();
        final Vector loc2 = location2.toVector();

        final Vector direction = loc2.subtract(loc1);
        direction.normalize();

        Location loc;

        double max = 0;
        if (location1.getWorld().equals(location2.getWorld())) {
            max = location1.distance(location2);
        }

        for (double i = 0; i <= max; i++) {
            loc = location1.clone().add(direction.clone().multiply(i));
            final Material type = loc.getBlock().getType();
            if (type != Material.AIR) {
                return true;
            }
        }
        return false;
    }

    public static Vector getOrthogonalVector(final Vector axis, final double degrees, final double length) {
        Vector ortho = new Vector(axis.getY(), -axis.getX(), 0);
        ortho = ortho.normalize();
        ortho = ortho.multiply(length);

        return rotateVectorAroundVector(axis, ortho, degrees);
    }

    public static Vector getOrthFrom2Vectors(final Vector vec1, final Vector vec2) {
        return vec1.clone().crossProduct(vec2.clone()).normalize();
    }

    public static Vector rotateVectorAroundVector(final Vector axis, final Vector rotator, final double degrees) {
        final double angle = Math.toRadians(degrees);
        Vector rotation = axis.clone();
        final Vector rotate = rotator.clone();
        rotation = rotation.normalize();

        final Vector thirdaxis = rotation.crossProduct(rotate).normalize().multiply(rotate.length());

        return rotate.multiply(Math.cos(angle)).add(thirdaxis.multiply(Math.sin(angle)));
    }

    public static Vector getVectorToMainHand(Player player){
        double y = 1.2 - (player.isSneaking() ? 0.4 : 0);
        return Vectors.getDirectionBetweenLocations(player.getLocation().add(0,y,0), Locations.getMainHandLocation(player));
    }



    public static Vector getVectorToOffHand(Player player){
        double y = 1.2 - (player.isSneaking() ? 0.4 : 0);
        return Vectors.getDirectionBetweenLocations(player.getLocation().add(0,y,0), Locations.getOffHandLocation(player));
    }

    public static Vector getLeftSideNormalisedVector(Player player){
        return Vectors.getDirectionBetweenLocations(player.getEyeLocation(), Locations.getLeftSide(player.getEyeLocation(), 1));
    }

    public static Vector getRightSideNormalisedVector(Player player){
        return Vectors.getDirectionBetweenLocations(player.getEyeLocation(), Locations.getRightSide(player.getEyeLocation(), 1));
    }

    public static Vec3 getVec3FromVector(Vector vector) {
        return new Vec3(vector.getX(), vector.getY(), vector.getZ());
    }
    
    public static double getPitchDiff(Vector previousVec, Vector newVec, Player player){
        return Math.toRadians(player.getLocation().setDirection(newVec).getPitch() - player.getLocation().setDirection(previousVec).getPitch());
    }
    
    public static double getYawDiff(Vector previousVec, Vector newVec, Player player){
        return Math.toRadians(player.getLocation().setDirection(newVec).getYaw() - player.getLocation().setDirection(previousVec).getYaw());
    }

}

















