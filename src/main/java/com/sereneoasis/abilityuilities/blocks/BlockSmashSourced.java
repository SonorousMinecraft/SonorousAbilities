package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.*;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sakrajin
 */
public class BlockSmashSourced extends CoreAbility {

    private final String name;

    private Location loc;

    private HashMap<Integer, TempDisplayBlock> smash;


    private boolean hasShot = false;

    private DisplayBlock displayBlock;

    private Material type;

    private boolean preselectedType = false;

    private Vector dir;

    private Set<LivingEntity> damagedSet = new HashSet<>();

    public BlockSmashSourced(Player player, String name, DisplayBlock displayBlock) {
        super(player, name);

        this.name = name;
        if (displayBlock != null) {
            preselectedType = true;
        }
        this.displayBlock = displayBlock;

        Block source = Blocks.getSourceBlock(player, sPlayer, sourceRange);
        abilityStatus = AbilityStatus.NO_SOURCE;
        if (source != null) {
            loc = source.getLocation();
            smash = new HashMap<>();
            if (!preselectedType) {
                type = source.getType();
                smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), type, size);
            } else {
                smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), displayBlock, size);
            }
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            start();
        }

    }

    @Override
    public void progress() {
        if (player.isSneaking() && !hasShot && sPlayer.getHeldAbility().equals(name)) {
            Location targetLoc = player.getEyeLocation().
                    add(player.getEyeLocation().getDirection().multiply(Math.max(radius + 1, loc.distance(player.getEyeLocation()))));
            if (loc.distance(targetLoc) > 1) {
                Vector dir = Vectors.getDirectionBetweenLocations(loc, targetLoc).normalize();
                loc.add(dir.clone().multiply(speed));
                if (preselectedType) {
                    smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), type, size);
                } else {
                    smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), displayBlock, size);
                }
            }

        } else if (hasShot) {
            if (loc.distance(player.getLocation()) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
                return;
            }
            loc.add(dir.clone().multiply(speed));
            if (preselectedType) {
                smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), type, size);
            } else {
                smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), displayBlock, size);
            }
            damagedSet.addAll(AbilityDamage.damageSeveralExceptReturnHit(loc, this, player, damagedSet, true, player.getEyeLocation().getDirection()));
        }


        if (!hasShot && System.currentTimeMillis() > startTime + duration) {
            abilityStatus = AbilityStatus.COMPLETE;
        }

    }

    @Override
    public void remove() {
        super.remove();
        for (TempDisplayBlock tb : smash.values()) {
            tb.revert();
        }
    }

    public void setHasClicked() {
        if (!hasShot) {
            hasShot = true;
            dir = player.getEyeLocation().getDirection();
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
