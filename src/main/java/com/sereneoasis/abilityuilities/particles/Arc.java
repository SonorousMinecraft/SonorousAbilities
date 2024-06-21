package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityDamage;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import com.sereneoasis.util.methods.Vectors;
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

    private OutputLocation outputLocation;

    public Arc(Player player, String name, Particle particle, OutputLocation outputLocation) {
        super(player, name);
        this.name = name;
        this.particle = particle;
        this.outputLocation = outputLocation;
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
//        if (Blocks.getFacingBlock(startLoc, dir, range) != null && !Blocks.getFacingBlock(startLoc, dir, range).isPassable()){
//            SkyUtils.lightningStrike(this,Blocks.getFacingBlock(startLoc, dir, range).getLocation() );
//        }

        Location endLoc = player.getEyeLocation().add(dir.clone().multiply(range));

        if (outputLocation == OutputLocation.MAINHAND) {
            startLoc.add(Vectors.getVectorToMainHand(player));
            endLoc.add(Vectors.getVectorToMainHand(player));
        } else if (outputLocation == OutputLocation.OFFHAND) {
            startLoc.add(Vectors.getVectorToOffHand(player));
            endLoc.add(Vectors.getVectorToOffHand(player));
        }

        if (locs.size() < 20) {
            for (int i = 0; i < 1; i++) {
                Location location = startLoc.clone();
                Vector newDir = Vectors.getDirectionBetweenLocations(location, randomMidwayVertex(endLoc, location)).normalize();
                location.setDirection(newDir);
                locs.add(location);

//                locs.add(startLoc.clone().add(getRandomOffset()));
            }
        }

        if ((System.currentTimeMillis() - sinceLastDirChange) > 100) {
            locs.forEach(location -> {
                Vector newDir = Vectors.getDirectionBetweenLocations(location, randomMidwayVertex(location, endLoc)).normalize();
                location.setDirection((location.getDirection().add(newDir.clone().multiply(1.2))).normalize());
            });

            sinceLastDirChange = System.currentTimeMillis();

        }

        for (double d = 0; d < range; d += hitbox) {

            AbilityDamage.damageOne(startLoc.clone().add(dir.clone().multiply(d)), this, player, true, dir);
        }


        locs.forEach(location -> {
//            location.setDirection(dir.clone());

            playParticlesBetweenPoints(location, location.clone().add(location.getDirection().clone().multiply(2)));
            location.add(location.getDirection());
//            location.add(location.getDirection().normalize());
//            Particles.spawnParticle(particle, location, 1, 0, 0);

        });

        locs.removeIf(location -> location.distanceSquared(player.getEyeLocation()) > range * range);

    }

    public void playParticlesBetweenPoints(Location start, Location end) {
        Vector difference = Vectors.getDirectionBetweenLocations(start, end);
        double distance = difference.length();
        Vector normalised = difference.clone().normalize();

        for (double d = 0; d <= distance; d += size) {
            Location temploc = start.clone().add(normalised.clone().multiply(d));
            //Particles.spawnColoredParticle(temploc, 1, 0.05, 1, Color.fromRGB(1, 225, 255));
//            TDBs.playTDBs(temploc, DisplayBlock.LIGHTNING, 1, size, 0);
//            Particles.spawnParticle(particle, temploc, 1, 0, 0);
            new ArchetypeVisuals.LightningVisual().playVisual(temploc, size, size / 2, 0, 1, 0);


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

        Vector random = getRandomOffset();
        if (start.distanceSquared(end) > 1) {
            random.multiply(Math.log(start.distance(end)) * Math.log(start.distance(end)));
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

    public enum OutputLocation {
        MAINHAND,
        OFFHAND,
        MIDDLE
    }
}
