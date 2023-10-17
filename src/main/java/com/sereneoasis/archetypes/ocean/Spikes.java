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
import java.util.Map;

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
        Block source = Blocks.getSourceBlock(player, sPlayer, sourceRange);
        if (source != null)
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
        for (Map.Entry<Block,TempBlock> entry : spike.entrySet()) {
            if (!Blocks.getBlocksAroundPoint(loc, radius, Material.WATER).contains(entry.getKey())) {
                entry.getValue().revertBlock();
            }
        }

        for (Block b: Blocks.getBlocksAroundPoint(loc, radius, Material.WATER))
        {
            if (!TempBlock.isTempBlock(b))
            {
                TempBlock tb = new TempBlock(b, Material.ICE.createBlockData(), duration);
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
