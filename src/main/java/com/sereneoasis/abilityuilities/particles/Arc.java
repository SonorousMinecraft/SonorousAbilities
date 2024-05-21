package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Arc extends CoreAbility {

    private final String name;

    private Set<Location> locs = new HashSet<>();

    private Random random = new Random();
    private Particle particle;

    private Vector offset = null;

    private long sinceLastDirChange = System.currentTimeMillis();

    public Arc(Player player, String name, Particle particle) {
        super(player, name);
        this.name = name;
        this.particle = particle;
        if (shouldStartCanHaveMultiple()) {
            abilityStatus = AbilityStatus.SHOOTING;
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (System.currentTimeMillis() > startTime + duration || !player.isSneaking()) {
            abilityStatus = AbilityStatus.COMPLETE;
        }

        Vector dir = player.getEyeLocation().getDirection();

        Location startLoc = player.getEyeLocation().add(dir.clone().multiply(speed));
        Location endLoc = player.getEyeLocation().add(dir.clone().multiply(range));

        if (locs.size() < 100) {
            for (int i = 0; i < 5; i++) {
                Location location = startLoc.clone();
                Vector newDir = Vectors.getDirectionBetweenLocations(location, randomMidwayVertex(endLoc,location)).normalize();
                location.setDirection(newDir);
                locs.add(location);

//                locs.add(startLoc.clone().add(getRandomOffset()));
            }
        }

        if ((System.currentTimeMillis() - sinceLastDirChange) > 200 ) {
            locs.forEach(location -> {
                Vector newDir = Vectors.getDirectionBetweenLocations(location, randomMidwayVertex(endLoc, location)).normalize();
                location.setDirection(location.getDirection().add(newDir.clone().multiply(1.2)));
            });

            sinceLastDirChange = System.currentTimeMillis();

        }


            locs.forEach(location -> {
//            location.setDirection(dir.clone());

                playParticlesBetweenPoints(location, location.clone().add(location.getDirection()));
            location.add(location.getDirection().clone());
//            Particles.spawnParticle(particle, location, 1, 0, 0);

            AbilityDamage.damageOne(location, this, player, true, dir);



        });

        locs.removeIf(location -> location.distanceSquared(player.getEyeLocation()) > range * range);

    }

    public void playParticlesBetweenPoints(Location start, Location end) {
        Vector difference = Vectors.getDirectionBetweenLocations(start,end);
        double distance = difference.length();
        Vector normalised = difference.clone().normalize();

        for (double d = 0; d < distance; d += size) {
            Location temploc = start.clone().add(normalised.clone().multiply(d));
            //Particles.spawnColoredParticle(temploc, 1, 0.05, 1, Color.fromRGB(1, 225, 255));
            TDBs.playTDBs(temploc, DisplayBlock.LIGHTNING, 1, size, 0);
            Particles.spawnParticle(particle, temploc, 1, 0, 0);

        }
            //Particles.spawnColoredParticle(temploc, 11, 1, 1, Color.fromRGB(1, 225, 255));

//            Vector random = Vector.getRandom().normalize().add(new Vector(-0.5,-0.5,-0.5)).normalize().add(dir.clone().multiply(0.2)).normalize().multiply(0.4);
//
//            Particles.spawnParticleOffset(Particle.END_ROD, temploc, 0, random.getX(), random.getY(), random.getZ(), 0.15);
//            Particles.spawnColoredParticle(temploc, 1, Math.log(d+1), size*3, Color.fromRGB(1, 225, 255));
//            Particles.spawnParticle(Particle.ELECTRIC_SPARK, temploc, 1,Math.log(d+1),0);
        }



    public Location randomMidwayVertex(Location start, Location end) {
        Vector midpoint = end.clone().subtract(start.clone()).toVector().multiply(0.5);
        Vector random = Vectors.getRandom();
        if (start.distanceSquared(end) > 1){
            random.multiply(Math.log(start.distance(end)));
        }
        return (start.clone().add(midpoint).add(random));
    }



    public Set<Location> getLocs() {
        return locs;
    }

    private Vector getRandomOffset(){
        Vector randomiser = Vectors.getRightSide(player, random.nextDouble()-0.5).add(new Vector(0, random.nextDouble() - 0.5, 0).rotateAroundAxis(Vectors.getRightSideNormalisedVector(player), Math.toRadians(-player.getEyeLocation().getPitch())));
        return randomiser;
    }

    @Override
    public String getName() {
        return name;
    }
}
