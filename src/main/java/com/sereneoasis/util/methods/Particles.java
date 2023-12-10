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

    public static void playParticlesBetweenPoints(Particle particle, Location loc1, Location loc2, double difference, int count, double offset, double extra) {
        for (Location loc : Locations.getLocationsBetweenLocs(loc1, loc2, difference))
        {
            spawnParticle(particle, loc, count, offset, extra);
        }
    }


    public static void playLocParticles(List<Location>locs, Particle particle, int count, double offset, double extra)
    {
        for (Location loc : locs)
        {
            spawnParticle(particle, loc, count, offset, extra);
        }
    }

    public static void spawnParticle(Particle particle, Location loc, int count, double offset, double extra)
    {
        loc.getWorld().spawnParticle(particle,loc,count, offset, offset, offset, extra);
    }

    public static void spawnColoredParticle(Location loc, int count, double offset, double size, Color color)
    {


        Particle.DustOptions dustOptions = new Particle.DustOptions(color, (float) size);
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






    }

