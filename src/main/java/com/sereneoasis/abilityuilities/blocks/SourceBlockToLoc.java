package com.sereneoasis.abilityuilities.blocks;


import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sakrajin
 * Allows a player to source a block and have it travel towards them
 */
public class SourceBlockToLoc extends CoreAbility {


    private Location loc, targetLoc;

    private String user;

    private double distanceToStop;

    private Material type;

    private TempDisplayBlock glowingSource;

    private int amount;

    private List<TempDisplayBlock>blocks = new ArrayList<>();

    private boolean shouldSneak = true;



    public SourceBlockToLoc(Player player, String user, double distanceToStop, int amount, Block source, Location targetLoc) {
        super(player, user);

        abilityStatus = AbilityStatus.NO_SOURCE;
        if (source != null ) {
            this.user = user;
            this.type = source.getType();
            this.amount = amount;
            this.distanceToStop = distanceToStop;
            abilityStatus = AbilityStatus.SOURCE_SELECTED;

            this.targetLoc = targetLoc.clone();
            Location origin = source.getLocation().add(size/2,size/2,size/2);

            glowingSource = Blocks.selectSourceAnimationManual(origin.clone(), sPlayer.getColor(), size);
            loc = origin.clone();
//            Bukkit.broadcastMessage(glowingSource.getBlockDisplay().getBlock().getMaterial().toString());
            for (int i = 0; i < amount; i++){
                TempDisplayBlock tdb = new TempDisplayBlock(glowingSource.getLoc(), type, 60000, size);
                blocks.add(tdb);
            }

            shouldSneak = false;
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

        //new TempBlock(loc.getBlock(), type.createBlockData(), 500);
        //loc.getBlock().setBlockData(Material.DIRT.createBlockData());
        //loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 5);


        if (abilityStatus == AbilityStatus.SOURCING) {
            if (shouldSneak && !player.isSneaking()) {
                this.remove();
            }

            Vector dir = Vectors.getDirectionBetweenLocations(loc, targetLoc).normalize();

            loc.add(dir.clone().multiply(speed));

            List<Location> locs = Locations.getShotLocations(loc, amount, dir, speed);

            for (int i = 0; i < amount; i++){
                blocks.get(i).moveTo(locs.get(i).clone().add(Math.random(),Math.random(),Math.random()));
            }

            if (loc.distance(targetLoc) <= distanceToStop) {
                abilityStatus = AbilityStatus.SOURCED;
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        blocks.forEach(TempDisplayBlock::revert);

    }

    public void setAbilityStatus(AbilityStatus abilityStatus) {
        this.abilityStatus = abilityStatus;
        if (abilityStatus != AbilityStatus.SOURCE_SELECTED)
        {
            glowingSource.revert();
        }
    }

    public Material getType() {
        return type;
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
