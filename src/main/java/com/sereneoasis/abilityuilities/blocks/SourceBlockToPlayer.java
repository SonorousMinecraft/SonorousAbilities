package com.sereneoasis.abilityuilities.blocks;


import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
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
public class SourceBlockToPlayer extends CoreAbility {

    

    private Location loc;

    private String user;

    private double distanceToStop;

    private DisplayBlock type;

    public SourceBlockToPlayer(Player player, String user, DisplayBlock type, double distanceToStop) {
        super(player,user);

        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Blocks.getFacingBlockOrLiquid(player, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sPlayer).contains(source.getType()))
        {
            this.user = user;
            this.type = type;
            this.distanceToStop = distanceToStop;
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
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

        if (abilityStatus == AbilityStatus.SOURCE_SELECTED)
        {
            Particles.spawnColoredParticle(loc.getBlock().getLocation().add(0,1,0),
                    5, 0.2, 1, ArchetypeDataManager.getArchetypeData(sPlayer.getArchetype()).getColor());
        }
        //new TempBlock(loc.getBlock(), type.createBlockData(), 500);
        //loc.getBlock().setBlockData(Material.DIRT.createBlockData());
        //loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 5);

        if (abilityStatus == AbilityStatus.SOURCING)
        {
            if (!player.isSneaking() )
            {
                this.remove();
            }

            Vector dir = Vectors.getDirectionBetweenLocations(loc, player.getEyeLocation()).normalize();

            loc.add(dir.clone().multiply(speed));

            List<Location> locs = Locations.getShotLocations(loc, 20, dir, speed);

            for (Location point : locs)
            {
                new TempDisplayBlock(point, type, 1000, Math.random() * hitbox);
            }

            if (loc.distance(player.getLocation()) <= distanceToStop)
            {
                abilityStatus = AbilityStatus.SOURCED;
            }
        }
    }

    public void setAbilityStatus(AbilityStatus abilityStatus) {
        this.abilityStatus = abilityStatus;
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
