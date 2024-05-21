package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.sun.SunUtils;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.*;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Beam extends CoreAbility {

    private final String name;

    private Set<Location> locs = new HashSet<>();

    private Random random = new Random();
    private ArchetypeVisuals.ArchetypeVisual archetypeVisual;



    private Location beamOrigin;
    public Beam(Player player, String name, ArchetypeVisuals.ArchetypeVisual archetypeVisual, Location beamOrigin) {
        super(player, name);
        this.name = name;
        this.archetypeVisual = archetypeVisual;
        if (shouldStart()) {
            this.beamOrigin = beamOrigin.clone();
            abilityStatus = AbilityStatus.SHOOTING;
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (System.currentTimeMillis() > startTime + duration || !player.isSneaking()) {
            abilityStatus = AbilityStatus.COMPLETE;
        }


        Location beamTarget = Locations.getFacingLocationObstructed(player.getEyeLocation(), player.getEyeLocation().getDirection(), range);

        if (Entities.getFacingEntity(player, range, hitbox) != null){
            beamTarget = Entities.getFacingEntity(player, range, hitbox).getLocation();
        }

        if (!beamTarget.getBlock().isPassable()){
            SunUtils.blockExplode(player, name, beamTarget, 2, 0.25);
        }


        Vector dir = Vectors.getDirectionBetweenLocations(beamOrigin,beamTarget);


        Location startLoc = beamOrigin.clone();


        if (locs.size() < 1000) {
            for (int i = 0; i < 100; i++) {
                locs.add(startLoc.clone());
//                locs.add(startLoc.clone().add(getRandomOffset()));
            }
        }

        locs.forEach(location -> {
            location.add(dir.clone().multiply(random.nextDouble() * speed).add(getRandomOffset().multiply( Math.log(location.distance(startLoc)/2 + 2))));
//            Particles.spawnParticle(particle, location, 1, 0, 0);
            archetypeVisual.playVisual(location, 0.5, radius/2, 0 , 10,5 );
            AbilityDamage.damageOne(location, this, player, true, dir);

//            SunUtils.blockExplode(player, name, location, 2, 0.25);

        });

        locs.removeIf(location -> location.distance(player.getEyeLocation()) > range * range);

//        if (player.getLocation().getPitch() > 50 && Blocks.getFacingBlock(player, range) != null) {
//            player.setVelocity(dir.clone().multiply(-speed));
//        }
    }

    public Location getBeamOrigin() {
        return beamOrigin;
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
