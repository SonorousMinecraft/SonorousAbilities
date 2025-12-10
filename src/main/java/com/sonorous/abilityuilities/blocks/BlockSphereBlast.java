package com.sonorous.abilityuilities.blocks;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.AbilityDamage;
import com.sonorous.util.methods.Entities;
import com.sonorous.util.methods.Locations;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

/**
 * Causes a spherical shaped blast to be shot from the player
 */
public class BlockSphereBlast extends CoreAbility {

    private final boolean gravity;

    private Location loc;
    private final Location origin;
    private Vector dir;

    private final String name;


    private HashMap<Integer, TempDisplayBlock> spike = new HashMap<>();


    public BlockSphereBlast(Player player, String name, Location startLoc, boolean gravity) {
        super(player, name);

        this.name = name;
        this.gravity = gravity;

        this.origin = player.getEyeLocation().clone();
        this.loc = startLoc.clone();
        spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(loc, radius, size),
                DisplayBlock.SUN, size);
        this.abilityStatus = AbilityStatus.CHARGED;
        start();
    }


    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.CHARGED) {
            dir = player.getEyeLocation().getDirection().clone();
        }
        if (abilityStatus == AbilityStatus.SHOT) {
            if (loc.distance(origin) > range || !loc.getBlock().isPassable()) {
                this.abilityStatus = AbilityStatus.COMPLETE;
            }

            if (gravity) {
                dir = dir.setY(dir.getY() - 0.01);
            }

            loc.add(dir.clone().multiply(speed));

            spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(loc, radius, size),
                    DisplayBlock.SUN, size);

            boolean isFinished = AbilityDamage.damageSeveral(loc, this, player, true, player.getEyeLocation().getDirection());

            if (isFinished) {
                this.abilityStatus = AbilityStatus.COMPLETE;
            }
        }
    }

    public void moveToLoc(Location targetLoc) {
        if (loc.distanceSquared(targetLoc) > 1.0) {
//            Vector dir = Vectors.getDirectionBetweenLocations(loc, targetLoc).normalize();
//            loc.add(dir.clone().multiply(speed));
            loc = targetLoc.clone();
            spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(targetLoc, radius, size), DisplayBlock.SUN, size);
        }
    }

    public Location getLoc() {
        return loc;
    }

    @Override
    public void remove() {
        super.remove();
        revert();
    }

    public void revert() {
        for (TempDisplayBlock tb : spike.values()) {
            tb.revert();
        }
        spike.clear();
    }


    @Override
    public String getName() {
        return name;
    }
}
