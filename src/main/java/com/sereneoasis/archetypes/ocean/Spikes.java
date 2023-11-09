package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * @author Sakrajin
 *
 */
public class Spikes extends CoreAbility {

    private final String name = "Spikes";

    private Location loc;

    private HashMap<Integer, TempDisplayBlock>spike;

    private long starttime;

    private boolean hasShot = false;

    public Spikes(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name))
        {
            return;
        }

        Block source = Blocks.getSourceBlock(player, sPlayer, sourceRange);
        if (source != null)
        {
            loc = source.getLocation();
            starttime = System.currentTimeMillis();
            spike = new HashMap<>();
            spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(loc, radius, 0.5), DisplayBlock.ICE, 0.5);
            start();
        }

    }

    @Override
    public void progress() {
        if (player.isSneaking() && !hasShot)
        {
            Location targetLoc = player.getEyeLocation().
                    add(player.getEyeLocation().getDirection().multiply(Math.max(radius+1,loc.distance(player.getEyeLocation()))));
            if (loc.distance(targetLoc) > 1)
            {
                Vector dir = Vectors.getDirectionBetweenLocations(loc, targetLoc).normalize();
                loc.add(dir.clone().multiply(speed));
                spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(loc, radius, 0.5), DisplayBlock.ICE, 0.5);
            }

        }
        else if (hasShot)
        {
            if (loc.distance(player.getLocation()) > range)
            {
                this.remove();
            }
            loc.add(player.getEyeLocation().getDirection().multiply(speed));
            spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(loc, radius, 0.5), DisplayBlock.ICE, 0.5);
            DamageHandler.damageEntity(Entities.getAffected(loc, radius, player), player, this, damage);
        }


        if (!hasShot && System.currentTimeMillis() > starttime+duration)
        {
            this.remove();
        }

    }

    @Override
    public void remove()
    {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        for (TempDisplayBlock tb : spike.values())
        {
            tb.revert();
        }
    }

    public void setHasClicked()
    {
        if (!hasShot)
        {
            hasShot = true;
        }
    }


    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return name;
    }
}
