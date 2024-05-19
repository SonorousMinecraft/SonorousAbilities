package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
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

public class Breath extends CoreAbility {

    private final String name;

    private Set<Location> locs = new HashSet<>();

    private Random random = new Random();
    private Particle particle;


    public Breath(Player player, String name, Particle particle) {
        super(player, name);
        this.name = name;
        this.particle = particle;
        if (shouldStart()) {
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
        if (locs.size() < 1000) {
            for (int i = 0; i < 100; i++) {
                locs.add(startLoc.clone());
//                locs.add(startLoc.clone().add(getRandomOffset()));
            }
        }

        locs.forEach(location -> {
            location.add(dir.clone().multiply(random.nextDouble() * speed).add(getRandomOffset().multiply( Math.log(location.distance(startLoc) + 2))));
            Particles.spawnParticle(particle, location, 1, 0, 0);

            AbilityDamage.damageOne(location, this, player, true, dir);
        });

        locs.removeIf(location -> location.distance(player.getEyeLocation()) > range * range);

        if (player.getLocation().getPitch() > 50 && Blocks.getFacingBlock(player, range) != null) {
            player.setVelocity(dir.clone().multiply(-speed));
        }
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
