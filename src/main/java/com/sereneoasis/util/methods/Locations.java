package com.sereneoasis.util.methods;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sakrajin
 * Methods which are related to locations
 */
public class Locations {
    public static Location getFacingLocation(Location loc, Vector dir, double distance)
    {
        return loc.add(dir.normalize().multiply(distance));
    }

    public static List<Location> getDisplayEntityLocs(Location loc, double size, double increment)
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



    public static List<Location> getSphere(Location loc, double radii, int density) {
        final List<Location> sphere = new ArrayList<Location>();
        for (double i = 0; i <= Math.PI; i += Math.PI / density) {
            double radius = Math.sin(i) * radii;
            double y = Math.cos(i) * radii;
            for (double a = 0; a < Math.PI * 2; a+= Math.PI*2 / density) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                sphere.add(loc.clone().add(x,y,z));
            }
        }
        return sphere;
    }
    public static List<Location> getCircle(Location loc, double radii, int points) {
        final List<Location> circle = new ArrayList<>();
        for (double i = 0; i < Math.PI *2; i+= Math.PI*2 / points) {
            double x = Math.sin(i) * radii;
            double z = Math.cos(i) * radii;
            Location location = loc.clone().add(x,0,z);
            circle.add(location);
        }
        return circle;
    }

    public static List<Location>getCirclePointsBetweenPoints(Location loc, double radii, int points, Vector dir, int startAngle, int endAngle)
    {
        int increment = Math.floorDiv(endAngle-startAngle, points);
        List<Location>locs = new ArrayList<>();
        for (int i = startAngle; i < endAngle; i+=increment)
        {
            double radian = Math.toRadians(i);
            double x = Math.sin(radian) * radii;
            double z = Math.cos(radian) * radii;
            Vector v = new Vector(x, 0, z).multiply(radii);
            locs.add(loc.clone().add(v));
        }
        return locs;
    }

    public static List<Location> getPolygon(Location loc, double radii, int points){
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

    public static List<Location> getHollowPolygon(Location loc, double radii, int points){
        final List<Location> hpolygon = new ArrayList<Location>();
        for (int i = 0; i < points; i++) {
            double angle = 360.0 / points * i;
            angle = Math.toRadians(angle);
            double x = Math.cos(angle);
            double z = Math.sin(angle);
        }
        return hpolygon;
    }

}
