package com.sonorous.abilityuilities.particles;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.AbilityDamage;
import com.sonorous.util.methods.Particles;
import com.sonorous.util.methods.RandomUtils;
import com.sonorous.util.methods.Vectors;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class Stream extends CoreAbility {

    private final String name;

    protected Set<Location> locs = new HashSet<>();
    protected Particle particle;

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
        return Vectors.getRightSide(player, RandomUtils.getRandomDouble(0,1) - 0.5).add(new Vector(0, RandomUtils.getRandomDouble(0,1) - 0.5, 0).rotateAroundAxis(Vectors.getRightSideNormalisedVector(player), Math.toRadians(-player.getEyeLocation().getPitch())));
    }

    @Override
    public String getName() {
        return name;
    }
}
