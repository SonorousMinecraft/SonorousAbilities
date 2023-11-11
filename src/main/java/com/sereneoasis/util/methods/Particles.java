package com.sereneoasis.util.methods;

import com.sereneoasis.archetypes.Archetype;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

/**
 * @author Sakrajin
 * Methods which are related to particles
 */
public class Particles {

    public static void spawnParticle(Particle particle, Location loc, int count, double offset, double extra)
    {
        loc.getWorld().spawnParticle(particle,loc,count, offset, offset, offset, extra);
    }

    public static void spawnColoredParticle(Location loc, int count, double offset, double size, String hexCode)
    {

        int red = 255;
        int green = 153;
        int blue = 51;

        int resultRed = Integer.valueOf(hexCode.substring(0, 2), 16);
        int resultGreen = Integer.valueOf(hexCode.substring(2, 4), 16);
        int resultBlue = Integer.valueOf(hexCode.substring(4, 6), 16);

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(resultRed, resultGreen, resultBlue), (float) size);
        loc.getWorld().
            spawnParticle(Particle.REDSTONE, loc, count, offset, offset, offset, dustOptions);
    }



    public static void playSphere(Location loc, double radii, int density, Particle particle) {
        for (double i = 0; i <= Math.PI; i += Math.PI / density) {
            double radius = Math.sin(i) * radii;
            double y = Math.cos(i) * radii;
            for (double a = 0; a < Math.PI * 2; a+= Math.PI*2 / density) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                spawnParticle(particle,loc.clone().add(x,y,z), 1, 0, 0);
            }
        }
    }


        public static void playCircle(Location loc, double radii, int points, Archetype type) {
            for (double i = 0; i < Math.PI *2; i+= Math.PI*2 / points) {
                double x = Math.sin(i) * radii;
                double z = Math.cos(i) * radii;
                Location location = loc.clone().add(x,0,z);
            }
        }
        
        public static double getWave(Location loc, Location origin, double wavelength, double amplitude) {
            final double distance = loc.distance(origin) % wavelength;
            double y = Math.cos(distance)*amplitude;
            return y;
        }
        


        public static void playPolygon(Location loc, double radii, int points, Archetype type){
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
                    loc.subtract(x + deltaX * d, 0, z + deltaZ * d);
                }
            }
        }



        public static void playHollowPolygon(Location loc, double radii, int points, Archetype type){
            for (int i = 0; i < points; i++) {
                double angle = 360.0 / points * i;
                angle = Math.toRadians(angle);
                double x = Math.cos(angle);
                double z = Math.sin(angle);
            }
        }



    }

