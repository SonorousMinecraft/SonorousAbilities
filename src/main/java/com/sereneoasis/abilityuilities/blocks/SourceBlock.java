package com.sereneoasis.abilityuilities.blocks;


import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author Sakrajin
 * Allows a player to source a block and have it travel towards them
 */
public class SourceBlock extends CoreAbility {

    private AbilityStatus abilityStatus;

    private Location loc;

    private String user;

    private double distanceToStop;

    private Material type;

    public SourceBlock(Player player, String user, Material type, double distanceToStop) {
        super(player,user);

        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Blocks.getFacingBlockOrLiquid(player, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sPlayer).contains(source.getType()))
        {
            this.user = user;
            this.type = type;
            this.distanceToStop = distanceToStop;
            abilityStatus = AbilityStatus.SOURCING;
            loc = source.getLocation();
            start();
        }
    }

    public AbilityStatus getSourceStatus() {
        return abilityStatus;
    }

    public Location getLocation() {
        return loc;
    }

    @Override
    public void progress() {
        if (!player.isSneaking() )
        {
            this.remove();
        }


        //new TempBlock(loc.getBlock(), type.createBlockData(), 500);
        //loc.getBlock().setBlockData(Material.DIRT.createBlockData());
        //loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 5);

        if (abilityStatus == AbilityStatus.SOURCING)
        {
            Vector dir = Vectors.getDirectionBetweenLocations(loc, player.getEyeLocation()).normalize();

            loc.add(dir.clone().multiply(speed));

            List<Location> locs = Locations.getShotLocations(loc, 20, dir, speed);

            for (Location point : locs)
            {
                new TempDisplayBlock(point, type.createBlockData(), 1000, Math.random());
            }

            if (loc.distance(player.getLocation()) <= distanceToStop)
            {
                abilityStatus = AbilityStatus.SOURCED;
            }
        }
    }



    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return user;
    }
}
