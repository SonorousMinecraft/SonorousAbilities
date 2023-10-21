package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempBlock;
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

    private Location loc;

    private HashSet<Block>spike;

    private long starttime;

    private boolean hasShot = false;

    public Spikes(Player player) {
        super(player);

        Block source = Blocks.getSourceBlock(player, sPlayer, sourceRange);
        if (source != null)
        {
            loc = source.getLocation();
            starttime = System.currentTimeMillis();
            spike = new HashSet<>();
            createTempBlocks();
            start();
        }

    }
    private void createTempBlocks()
    {
        Set<Block> currentBlocks = Blocks.getBlocksAroundPoint(loc.getBlock().getLocation(), radius);
        for (Block b: currentBlocks)
        {
            if (!spike.contains(b) && !TempBlock.isTempBlock(b))
            {
                new TempBlock(b, Material.ICE.createBlockData(), duration);
                spike.add(b);
            }
        }
        Iterator<Block>it = spike.iterator();
        while (it.hasNext())
        {
            Block b = it.next();
            if (!currentBlocks.contains(b))
            {
                TempBlock.getTempBlock(b).revertBlock();
                it.remove();
            }
        }
    }

    @Override
    public void progress() {
        if (player.isSneaking() && !hasShot)
        {
            Location targetLoc = player.getEyeLocation().
                    add(player.getEyeLocation().getDirection().multiply(loc.distance(player.getEyeLocation())));
            Vector dir = Vectors.getDirectionBetweenLocations(loc, targetLoc).normalize();
            loc.add(dir.clone().multiply(speed));
            createTempBlocks();
        }
        else if (hasShot)
        {
            if (loc.distance(player.getLocation()) > range)
            {
                this.remove();
            }
            loc.add(player.getEyeLocation().getDirection().multiply(speed));
            createTempBlocks();
            DamageHandler.damageEntity(Entities.getAffected(loc, radius, player), player, this, damage);
        }
        else if (!hasShot && System.currentTimeMillis() > starttime+duration)
        {
            this.remove();
        }

    }

    @Override
    public void remove()
    {
        super.remove();
        sPlayer.addCooldown("Spikes", cooldown);
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
        return "Spikes";
    }
}
