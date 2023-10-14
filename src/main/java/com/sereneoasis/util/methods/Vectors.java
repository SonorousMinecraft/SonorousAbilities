package com.sereneoasis.util.methods;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Methods which are related to vectors
 */
public class Vectors {

    public static Vector getDirectionBetweenLocations(Location start, Location end)
    {
        return end.subtract(start).toVector();
    }

    public static double getAngleBetweenVectors(Vector vec1, Vector vec2)
    {
        double num = vec1.dot(vec2);
        double den = vec1.length() * vec2.length();
        double d = Math.acos(num/den);
        return d;
    }
}
