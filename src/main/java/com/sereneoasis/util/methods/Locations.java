package com.sereneoasis.util.methods;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MainHand;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * @author Sakrajin
 * Methods which are related to locations
 */
public class Locations {

    public static List<Location> getArc(Location loc1, Location loc2, Location origin, double distance) {
        List<Location> locs = new ArrayList<>();
        double radius = loc1.distance(origin);
        Vector orth = Vectors.getOrthFrom2Vectors(Vectors.getDirectionBetweenLocations(loc1, origin), Vectors.getDirectionBetweenLocations(loc2, origin));
        double angle = Vectors.getAngleBetweenVectors(Vectors.getDirectionBetweenLocations(loc1, origin), Vectors.getDirectionBetweenLocations(loc2, origin));
        double arcLength = radius * angle;
        int increments = (int) (arcLength / distance);
        double angleStep = angle / increments;
        Vector startRadius = Vectors.getDirectionBetweenLocations(origin, loc1);
        for (int r = 0; r < increments; r++) {
            locs.add(origin.clone().add(startRadius.rotateAroundAxis(orth, angleStep)));
        }
        return locs;
    }

    public static Location getMidpoint(Location loc1, Location loc2) {
        return loc1.clone().add(loc2.clone()).multiply(0.5);
    }

    public static List<Location> getPointsAlongLine(Location loc1, Location loc2, double distance) {
        Location startLoc = loc1.clone();
        Vector differenceVec = Vectors.getDirectionBetweenLocations(loc1, loc2);
        List<Location> locs = new ArrayList<>();
        for (double d = 0; d < differenceVec.length(); d += distance) {
            locs.add(startLoc.clone().add(differenceVec.clone().normalize().multiply(d)));
        }
        return locs;
    }

    public static Location getFacingLocation(Location loc, Vector dir, double distance) {
        return loc.clone().add(dir.clone().normalize().multiply(distance)).clone();
    }

    public static Location getFacingLocationObstructed(Location loc, Vector dir, double distance) {

//        if (Blocks.getFacingBlock(loc, dir, distance) != null) {
//            facingLoc = Blocks.getFacingBlockLoc(loc, dir, distance);
//        }

        Block target = Blocks.getFacingBlockOrLiquid(loc, dir, distance);
        if (target != null && target.getType().isSolid()) {
            return target.getLocation();
        }

//        RayTraceResult rayTraceResult = loc.getWorld().rayTraceBlocks(loc, dir, distance);
//        if (rayTraceResult != null) {
//            Bukkit.broadcastMessage("the player is looking at smth");
//            if (rayTraceResult.getHitBlock() != null){
//                facingLoc = rayTraceResult.getHitPosition().toLocation(facingLoc.getWorld());
//
//            }
//        }

        Location facingLoc = getFacingLocation(loc, dir, distance);

        return facingLoc;
    }

    public static List<Location> getDisplayEntityLocs(Location loc, double size, double increment) {
        List<Location> locs = new ArrayList<>();
        for (double x = -size / 2; x < size / 2; x += increment) {
            for (double y = -size / 2; y < size / 2; y += increment) {
                for (double z = -size / 2; z < size / 2; z += increment) {
                    locs.add(loc.clone().add(x, y, z));
                }
            }
        }
        return locs;
    }


    public static List<Location> getSphere(Location loc, double radii, int density) {
        final List<Location> sphere = new ArrayList<Location>();
        for (double i = 0; i <= Math.PI; i += Math.PI / density) {
            double radius = Math.sin(i) * radii;
            double y = Math.cos(i) * radii;
            for (double a = 0; a < Math.PI * 2; a += Math.PI * 2 / density) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                sphere.add(loc.clone().add(x, y, z));
            }
        }
        return sphere;
    }

    public static List<Location> getCircle(Location loc, double radii, int points, Vector dir, double orientation) {
        final List<Location> circle = new ArrayList<>();
        for (double i = 0; i < Math.PI * 2; i += Math.PI * 2 / points) {
            double x = Math.sin(i) * radii;
            double z = Math.cos(i) * radii;
            Vector vec = new Vector(x, 0, z);
            vec.rotateAroundAxis(dir, orientation);
            Location location = loc.clone().add(vec);
            circle.add(location);
        }
        return circle;
    }

    public static List<Location> getArcFromTrig(Location loc, double radius, int points, Vector dir, int orientation, int startAngle, int endAngle, boolean clockwise) {
        int increment = Math.floorDiv(endAngle - startAngle, points);
        List<Location> locs = new ArrayList<>();
        for (int i = startAngle; i < endAngle; i += increment) {
            double radian = Math.toRadians(i);
            double x, z;
            if (!clockwise) {
                x = Math.sin(radian);
                z = Math.cos(radian);
            } else {
                z = Math.sin(radian);
                x = Math.cos(radian);
            }
            Vector v = new Vector(x, 0, z).multiply(radius).rotateAroundAxis(dir, Math.toRadians(orientation));
            locs.add(loc.clone().add(v));
        }
        return locs;
    }

    public static Set<Location> getPerpArcFromVector(Location loc, Vector dir, double radius, int startAngle, int endAngle, int points) {
        int increment = Math.floorDiv(endAngle - startAngle, points);
        Set<Location> locs = new HashSet<>();
        for (int i = startAngle; i < endAngle; i += increment) {
            locs.add(loc.clone().add(dir.clone().rotateAroundY(Math.toRadians(i)).multiply(radius)));
        }
        return locs;
    }


    public static List<Location> getShotLocations(Location loc, int points, Vector dir, double speed) {
        double increment = speed / points;
        List<Location> locs = new ArrayList<>();
        for (double d = 0; d < speed; d += increment) {
            locs.add(loc.clone().add(dir.clone().multiply(d)));
        }
        return locs;
    }

    public static List<Location> getBezierCurveLocations(Location loc, int points, LinkedHashMap<Vector, Double> directions, double speed) {
        double distance = directions.values().stream().reduce(0.0, Double::sum);
        double increment = (distance) / points;
        List<Location> locs = new ArrayList<>();
        Line line = new Line(directions);
        Location previousVec = loc.clone();
        for (double d = 0; d < distance; d += increment) {
            locs.add(previousVec.add(line.getVector(increment / distance).clone().multiply(speed)));
        }

        return locs;
    }

    private static class Line {

        private Vector dir1;
        private Vector dir2;

        private Line previous;

        Line(Line previous, Vector dir1, Vector dir2) {
            this.previous = previous;
            this.dir1 = dir1;
            this.dir2 = dir2;
        }

        Line(LinkedHashMap<Vector, Double> directions) {
            Vector oldVector = new Vector(0, 0, 0);
            Line line = null;
            int i = 0;
            for (Map.Entry<Vector, Double> entry : directions.entrySet()) {
                i++;
                if (i == directions.size()) {
                    this.previous = line;
                    this.dir1 = oldVector;
                    this.dir2 = entry.getKey().multiply(entry.getValue()).add(oldVector);
                    return;
                }

                Line newLine = new Line(line, oldVector, entry.getKey().multiply(entry.getValue()).add(oldVector));
                oldVector = oldVector.add(entry.getKey());
                line = newLine;

            }

        }

        private Vector getVector(double time) {
            if (previous == null) {
                return dir1.multiply(1 - time).add(dir2.multiply(time));
            } else {
                return previous.getVector(1 - time).add(dir2.multiply(time));
            }
        }

    }

    public static List<Location> getPolygon(Location loc, double radii, int points) {
        final List<Location> polygon = new ArrayList<Location>();
        for (int i = 0; i < points; i++) {
            double angle = 360.0 / points * i;
            double nextAngle = 360.0 / points * (i + 1); // the angle for the next point.
            angle = Math.toRadians(angle);
            nextAngle = Math.toRadians(nextAngle); // convert to radians.
            double x = Math.cos(angle);
            double z = Math.sin(angle);
            double x2 = Math.cos(nextAngle);
            double z2 = Math.sin(nextAngle);
            double deltaX = x2 - x; // get the x-difference between the points.
            double deltaZ = z2 - z; // get the z-difference between the points.
            double distance = Math.sqrt((deltaX - x) * (deltaX - x) + (deltaZ - z) * (deltaZ - z));
            for (double d = 0; d < distance - .1; d += .1) { // we subtract .1 from the distance because otherwise it would make 1 step too many.
                loc.add(x + deltaX * d, 0, z + deltaZ * d);
                polygon.add(loc);
                loc.subtract(x + deltaX * d, 0, z + deltaZ * d);
            }
        }
        return polygon;
    }

    public static List<Location> getHollowPolygon(Location loc, double radii, int points) {
        final List<Location> hpolygon = new ArrayList<Location>();
        for (int i = 0; i < points; i++) {
            double angle = 360.0 / points * i;
            angle = Math.toRadians(angle);
            double x = Math.cos(angle);
            double z = Math.sin(angle);
        }
        return hpolygon;
    }


    public static Set<Location> getLocsAroundPoint(Location loc, double radius, double distance) {
        Set<Location> locs = new HashSet<>();
        radius -= radius / 2;
        for (double y = -radius; y < radius; y += distance) {
            for (double x = -radius; x < radius; x += distance) {
                for (double z = -radius; z < radius; z += distance) {
                    Location temploc = loc.clone().add(x, y, z);
                    if (temploc.distanceSquared(loc) <= radius * radius) {
                        locs.add(loc.clone().add(x, y, z));
                    }
                }
            }
        }
        return locs;
    }

    public static Set<Location> getCircleLocsAroundPoint(Location loc, double radius, double distance) {
        Set<Location> locs = new HashSet<>();
        radius -= radius / 2;
            for (double x = -radius; x < radius; x += distance) {
                for (double z = -radius; z < radius; z += distance) {
                    Location temploc = loc.clone().add(x, 0, z);
                    if (temploc.distanceSquared(loc) <= radius * radius) {
                        locs.add(loc.clone().add(x, 0, z));
                    }
                }
            }
        return locs;
    }

    public static Set<Location> getOutsideSphereLocs(Location loc, double radius, double distance) {
        Set<Location> locs = new HashSet<>();
        for (double y = -radius; y < radius; y += distance) {
            for (double x = -radius; x < radius; x += distance) {
                for (double z = -radius; z < radius; z += distance) {
                    Location temploc = loc.clone().add(x, y, z);
                    if (temploc.distanceSquared(loc) < radius * radius && temploc.distance(loc) > radius - distance - 0.1) {
                        locs.add(temploc);
                    }
                }
            }
        }
        return locs;
    }

    public static Location getLeftSide(final Location location, final double distance) {
        final float angle = (float) Math.toRadians(location.getYaw());
        return location.clone().add(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    public static Location getUpLoc(Location loc, double distance){
        Vector up = Vectors.getUp(loc, 1);
        return loc.clone().add(up.multiply(distance));
    }

    public static Location getDownLoc(Location loc, double distance){
        Vector down = Vectors.getDown(loc, 1);
        return loc.clone().add(down.multiply(distance));
    }

    public static Location getRightSide(final Location location, final double distance) {
        final float angle = (float) Math.toRadians(location.getYaw());
        return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    public static Location getLeftSide(final Location location, Vector dir, double distance){
        return location.clone().add(dir.clone().rotateAroundY(Math.toRadians(90)).normalize().multiply(distance));
    }

    public static Location getLeftSideFromPlayerView(final Location location, Player player, double distance){
        return location.clone().add(Vectors.getLeftSideNormalisedVector(player).multiply(distance));
    }

    public static Location getRightSide(final Location location, Vector dir, double distance){
        return location.clone().add(dir.clone().rotateAroundY(Math.toRadians(270)).normalize().multiply(distance));
    }

    public static Location getRightSideFromPlayerView(final Location location, Player player, double distance){
        return location.clone().add(Vectors.getRightSideNormalisedVector(player).multiply(distance));
    }

    public static Location getMainHandLocation(final Player player) {
        double y = 1.2 - (player.isSneaking() ? 0.4 : 0);
        if (player.getMainHand() == MainHand.LEFT) {
            return getLeftSide(player.getLocation(), .55).add(0, y, 0)
                    .add(player.getLocation().getDirection().multiply(0.8));
        } else {
            return getRightSide(player.getLocation(), .55).add(0, y, 0)
                    .add(player.getLocation().getDirection().multiply(0.8));
        }
    }

    public static Location getOffHandLocation(final Player player) {
        double y = 1.2 - (player.isSneaking() ? 0.4 : 0);
        if (player.getMainHand() == MainHand.RIGHT) {
            return getLeftSide(player.getLocation(), .55).add(0, y, 0)
                    .add(player.getLocation().getDirection().multiply(0.8));
        } else {
            return getRightSide(player.getLocation(), .55).add(0, y, 0)
                    .add(player.getLocation().getDirection().multiply(0.8));
        }
    }

    public static double PI = Math.PI;

    public static List<Location> getHelix(Location loc, Vector dir, double distance, int points, int height, int startAngle, boolean anticlockwise) {
        double tempDistance = 0;
        List<Location>locs = new ArrayList<>();
        for (double d = startAngle; d < (2 * PI) + startAngle; d += (2 * PI / points)) {
            Location tempLoc = loc.clone();
            Vector tempDir = dir.clone();
            tempDistance += distance / points;
            Vector orthoDir = Vectors.getOrthogonalVector(tempDir, d, tempDistance);
            if (anticlockwise) {
                tempLoc.add(orthoDir.rotateAroundAxis(tempDir, d));
            } else {
                tempLoc.add(orthoDir.rotateAroundAxis(tempDir, -d));
            }
            loc.add(tempDir.multiply(height / points));

            locs.add(tempLoc);
        }
        return locs;
    }

    public  static List<Location> getSeveralHelixes(Location loc, Vector dir, double distance, int points, int height, int startAngle, boolean anticlockwise, int helixes) {
        int currentAngle = startAngle;
        List<Location>locs = new ArrayList<>();
        for (int i = 0; i < helixes; i++) {
            locs.addAll(getHelix(loc.clone(), dir, distance, points, height, currentAngle, anticlockwise));
            currentAngle += 360 / helixes;
        }
        return locs;
    }

    public static Location getNextLoc(Location inputLoc, Vector dir, double speed) {
        Location loc = inputLoc.clone();
        loc.add(dir.clone().multiply(speed));
        Location middleLoc = loc.clone();
        Location topLoc = middleLoc.clone().add(0, 1, 0);
        Location bottomLoc = middleLoc.clone().subtract(0, 1, 0);
        if (middleLoc.getBlock().isLiquid() || middleLoc.getBlock().getType().isAir()) {
            if (!(topLoc.getBlock().isLiquid() || topLoc.getBlock().getType().isAir())) {
                loc = topLoc;
            } else if (!(bottomLoc.getBlock().isLiquid() || bottomLoc.getBlock().getType().isAir())) {
                loc = bottomLoc;
            } else {
                loc = null;
            }
        } else if (!Blocks.isTopBlock(middleLoc.getBlock())) {
            middleLoc.add(0, 1, 0);
            if (!Blocks.isTopBlock(middleLoc.getBlock())) {
                loc = null;
            } else {
                loc = middleLoc;
            }
        }
        return loc;
    }
}
