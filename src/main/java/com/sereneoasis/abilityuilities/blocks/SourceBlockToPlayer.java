package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.util.Methods;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.SourceStatus;
import com.sereneoasis.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SourceBlockToPlayer extends CoreAbility {

    private SourceStatus sourceStatus;

    private Location loc;

    private CoreAbility user;

    private double distanceToStop;

    private Material type;

    public SourceBlockToPlayer(Player player, CoreAbility user, Material type, double distanceToStop) {
        super(player);

        sourceStatus = SourceStatus.NO_SOURCE;
        Block source = Methods.getFacingBlockOrLiquid(player, sourceRange);
        if (source.getType().equals(type))
        {
            this.user = user;
            this.type = type;
            this.range = range;
            this.distanceToStop = distanceToStop;
            sourceStatus = SourceStatus.SOURCING;
            loc = source.getLocation();
            start();
        }
    }

    public SourceStatus getSourceStatus() {
        return sourceStatus;
    }

    @Override
    public void progress() {
        if (!player.isSneaking() )
        {
            this.remove();
        }

        new TempBlock(loc.getBlock(), type.createBlockData(), 1000);
        //loc.getBlock().setBlockData(Material.DIRT.createBlockData());
        //loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 5);

        if (sourceStatus == SourceStatus.SOURCING)
        {
            Vector dir = Methods.getDirectionBetweenLocations(loc, player.getEyeLocation());
            loc.add(dir.clone().multiply(speed));
            if (loc.distance(player.getLocation()) <= distanceToStop)
            {
                sourceStatus = SourceStatus.SOURCED;
            }
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return user.getName();
    }
}
