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

import java.util.HashMap;

/**
 * @author Sakrajin
 *
 */
public class Spikes extends CoreAbility {

    private Location loc;

    private HashMap<Block, TempBlock>spike;

    private long starttime;

    private boolean hasShot = false;

    public Spikes(Player player) {
        super(player);
        Block source = Blocks.getFacingBlockOrLiquid(player, sourceRange);
        if (source.getType().equals(Material.WATER))
        {
            loc = source.getLocation();
            starttime = System.currentTimeMillis();
            spike = new HashMap<>();
            createTempBlocks();
            start();
        }

    }
    private void createTempBlocks()
    {
        for (Block b: spike.keySet())
        {
            if (!Blocks.getBlocksAroundPoint(loc, radius, Material.WATER).contains(b)) {
                spike.get(b).revertBlock();
            }
        }

        for (Block b: Blocks.getBlocksAroundPoint(loc, radius, Material.WATER))
        {
            if (!TempBlock.isTempBlock(b))
            {
                TempBlock tb = new TempBlock(b, Material.WATER.createBlockData(), duration);
                spike.put(b, tb);
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
