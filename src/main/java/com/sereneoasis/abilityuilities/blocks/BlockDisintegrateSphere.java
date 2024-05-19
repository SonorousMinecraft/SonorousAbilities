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



    public BlockDisintegrateSphere(Player player, String name, Location startLoc, double currentRadius, double increment) {
        super(player, name);

        this.name = name;

        this.loc = startLoc.clone();
        this.currentRadius = currentRadius;
        this.increment = increment;
        start();
    }


    @Override
    public void progress() {
        currentRadius += increment;
        Particles.spawnParticle(Particle.SONIC_BOOM, loc, 20, currentRadius , 0);

        Blocks.getBlocksAroundPoint(loc, currentRadius).stream().forEach(block -> {
            if (!TempBlock.isTempBlock(block)) {
                TempBlock tb = new TempBlock(block, Material.LIGHT, duration, true);
            }
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
