package com.sonorous.abilityuilities.blocks;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.AbilityDamage;
import com.sonorous.util.methods.Entities;
import com.sonorous.util.methods.Locations;
import com.sonorous.util.methods.Vectors;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sakrajin
 */
public class BlockSmash extends CoreAbility {

    private final String name;

    private Location loc, shotOrigin;

    private HashMap<Integer, TempDisplayBlock> smash;


    private boolean hasShot = false;

    private DisplayBlock displayBlock;

    private Material type;

    private Set<LivingEntity> damagedSet = new HashSet<>();

    private Vector dir;
    private boolean directional = false;

    public BlockSmash(Player player, String name, DisplayBlock displayBlock, Location origin) {
        super(player, name);

        this.name = name;
        this.displayBlock = displayBlock;
        loc = origin.clone();
        smash = new HashMap<>();
        smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), displayBlock, size);
        start();
    }


    public BlockSmash(Player player, String name, DisplayBlock displayBlock, Location origin, boolean directional) {
        super(player, name);

        this.name = name;
        this.displayBlock = displayBlock;
        loc = origin.clone();
        smash = new HashMap<>();
        smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), displayBlock, size);
        this.directional = directional;
        start();
    }

    public BlockSmash(Player player, String name, Location origin, Material type) {
        super(player, name);

        this.name = name;
        this.type = type;
        loc = origin.clone();
        smash = new HashMap<>();
        smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), type, size);
        start();
    }

    public Vector getDir() {
        return dir;
    }

    @Override
    public void progress() {
        if (player.isSneaking() && !hasShot) {
            Location targetLoc = player.getEyeLocation().
                    add(player.getEyeLocation().getDirection().multiply(Math.max(radius + 1, loc.distance(player.getEyeLocation()))));
            if (loc.distanceSquared(targetLoc) > 1) {
                Vector dir = Vectors.getDirectionBetweenLocations(loc, targetLoc).normalize();
                loc.add(dir.clone().multiply(speed));
                smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), displayBlock, size);
            }

        } else if (hasShot) {
            if (loc.distance(shotOrigin) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
                return;
            }
            if (directional) {
                loc.add(player.getEyeLocation().getDirection().multiply(speed));
            } else {
                loc.add(dir.clone().multiply(speed));
            }
            if (displayBlock != null) {
                smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), displayBlock, size);
            } else {
                smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), type, size);
            }
            damagedSet.addAll(AbilityDamage.damageSeveralExceptReturnHit(loc, this, player, damagedSet, true, player.getEyeLocation().getDirection()));

        }


        if (!hasShot && System.currentTimeMillis() > startTime + duration) {
            abilityStatus = AbilityStatus.COMPLETE;
        }

    }

    public Location getLoc() {
        return loc;
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
            this.dir = player.getEyeLocation().getDirection();
            this.shotOrigin = loc.clone();
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
