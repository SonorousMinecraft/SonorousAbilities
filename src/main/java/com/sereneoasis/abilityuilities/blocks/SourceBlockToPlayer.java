package com.sereneoasis.abilityuilities.blocks;


import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.Methods;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SourceBlockToPlayer extends CoreAbility {

    private AbilityStatus abilityStatus;

    private Location loc;

    private String user;

    private double distanceToStop;

    private Material type;

    public SourceBlockToPlayer(Player player, String user, Material type, double distanceToStop) {
        super(player,user);

        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Methods.getFacingBlockOrLiquid(player, sourceRange);
        if (source != null && source.getType().equals(type))
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

        new TempBlock(loc.getBlock(), type.createBlockData(), 500);
        //loc.getBlock().setBlockData(Material.DIRT.createBlockData());
        //loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 5);

        if (abilityStatus == AbilityStatus.SOURCING)
        {
            Vector dir = Methods.getDirectionBetweenLocations(loc, player.getEyeLocation());
            loc.add(dir.clone().multiply(speed));
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
