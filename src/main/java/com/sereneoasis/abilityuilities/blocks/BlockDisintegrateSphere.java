package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 * Causes a spherical shaped blast to be shot from the player
 */
public class BlockDisintegrateSphere extends CoreAbility {

    private Location loc;

    private String name;

    private double currentRadius, increment;

    private boolean noParticles = false;


    public BlockDisintegrateSphere(Player player, String name, Location startLoc, double currentRadius, double increment) {
        super(player, name);

        this.name = name;

        this.loc = startLoc.clone();
        this.currentRadius = currentRadius;
        this.increment = increment;
        start();
    }

    public BlockDisintegrateSphere(Player player, String name, Location startLoc, double currentRadius, double endRadius, double increment) {
        super(player, name);

        this.name = name;

        this.loc = startLoc.clone();
        this.currentRadius = currentRadius;
        this.radius = endRadius;
        this.increment = increment;
        start();
    }

    public BlockDisintegrateSphere(Player player, String name, Location startLoc, double currentRadius, double endRadius, double increment, boolean noParticles) {
        super(player, name);

        this.name = name;

        this.loc = startLoc.clone();
        this.currentRadius = currentRadius;
        this.radius = endRadius;
        this.increment = increment;
        this.noParticles = noParticles;
        start();
    }


    @Override
    public void progress() {
        currentRadius += increment;
        if (!noParticles) {
            Particles.spawnParticle(Particle.SONIC_BOOM, loc, 20, currentRadius, 0);
        }

        Blocks.getBlocksAroundPoint(loc, currentRadius).stream().forEach(block -> {
            TempBlock tb = new TempBlock(block, Material.AIR, duration);

        });

        if (currentRadius > radius) {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();

    }

    @Override
    public String getName() {
        return name;
    }
}
