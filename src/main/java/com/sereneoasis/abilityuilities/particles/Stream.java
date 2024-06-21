package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityDamage;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Stream extends CoreAbility {

    private final String name;

    protected Set<Location> locs = new HashSet<>();
    protected Particle particle;
    private Random random = new Random();
    private Vector offset = null;

    public Stream(Player player, String name, Particle particle) {
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

        if (locs.size() < 10000) {
            for (int i = 0; i < 100; i++) {
                Location location = startLoc.clone();
                Vector newDir = Vectors.getDirectionBetweenLocations(location, randomMidwayVertex(endLoc, location)).normalize();
                location.setDirection(newDir);
                locs.add(location);

//                locs.add(startLoc.clone().add(getRandomOffset()));
            }
        }

        locs.forEach(location -> {
//            location.setDirection(dir.clone());
            Vector newDir = Vectors.getDirectionBetweenLocations(location, randomMidwayVertex(endLoc, location)).normalize();
            location.setDirection(location.getDirection().add(newDir.clone().multiply(0.1)).normalize());
            location.add(location.getDirection().clone());
            Particles.spawnParticle(particle, location, 1, 0, 0);

            AbilityDamage.damageOne(location, this, player, true, dir);


        });

        locs.removeIf(location -> location.distanceSquared(player.getEyeLocation()) > range * range);

    }


    public Location randomMidwayVertex(Location start, Location end) {
        Vector midpoint = end.clone().subtract(start.clone()).toVector().multiply(0.5);
        Vector random = Vectors.getRandom();
        if (start.distanceSquared(end) > 1) {
            random.multiply(Math.log(start.distance(end)) / 4);
        }
        return (start.clone().add(midpoint).add(random));
    }


    public Set<Location> getLocs() {
        return locs;
    }

    private Vector getRandomOffset() {
        Vector randomiser = Vectors.getRightSide(player, random.nextDouble() - 0.5).add(new Vector(0, random.nextDouble() - 0.5, 0).rotateAroundAxis(Vectors.getRightSideNormalisedVector(player), Math.toRadians(-player.getEyeLocation().getPitch())));
        return randomiser;
    }

    @Override
    public String getName() {
        return name;
    }
}
