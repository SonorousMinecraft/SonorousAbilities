package com.sonorous.abilityuilities.blocks.forcetype;


import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.methods.Locations;
import com.sonorous.util.methods.Vectors;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author Sakrajin
 * Allows a player to source a block and have it travel towards them
 */
public class SourceBlockToPlayerGivenType extends CoreAbility {


    private Location loc;

    private String user;

    private double distanceToStop;

    private DisplayBlock type;

    private TempDisplayBlock glowingSource;

    public SourceBlockToPlayerGivenType(Player player, String user, DisplayBlock type, double distanceToStop) {
        super(player, user);

        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Blocks.getFacingBlockOrLiquid(player, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sPlayer).contains(source.getType())) {
            this.user = user;
            this.type = type;
            this.distanceToStop = distanceToStop;
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            glowingSource = Blocks.selectSourceAnimationManual(Blocks.getFacingBlockOrLiquidLoc(player, sourceRange).clone().subtract(0, size / 2, 0), sPlayer.getColor(), size);
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

        //new TempBlock(loc.getBlock(), type.createBlockData(), 500);
        //loc.getBlock().setBlockData(Material.DIRT.createBlockData());
        //loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 5);


        if (abilityStatus == AbilityStatus.SOURCING) {
            if (!player.isSneaking()) {
                this.remove();
            }

            Location targetLoc = player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().multiply(radius));

            Vector dir = Vectors.getDirectionBetweenLocations(loc, targetLoc).normalize();

            loc.add(dir.clone().multiply(speed));

            List<Location> locs = Locations.getShotLocations(loc, Math.round(Math.round(speed / (size / 3))), dir, speed);

            for (Location point : locs) {
                new TempDisplayBlock(point, type, 500, size);
            }

            if (loc.distanceSquared(targetLoc) <= distanceToStop * distanceToStop) {
                abilityStatus = AbilityStatus.SOURCED;
            }
        }
    }

    public void setAbilityStatus(AbilityStatus abilityStatus) {
        this.abilityStatus = abilityStatus;
        if (abilityStatus != AbilityStatus.SOURCE_SELECTED) {
            glowingSource.revert();
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
