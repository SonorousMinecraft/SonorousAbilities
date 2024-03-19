package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.AbilityDamage;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
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

    private Location loc;

    private HashMap<Integer, TempDisplayBlock> smash;


    private boolean hasShot = false;

    private DisplayBlock displayBlock;

    private Set<LivingEntity> damagedSet = new HashSet<>();
    

    public BlockSmash(Player player, String name, DisplayBlock displayBlock, Location origin) {
        super(player, name);

        this.name = name;
        this.displayBlock = displayBlock;
        this.size = size;

        loc = origin.clone();
        smash = new HashMap<>();
        smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), displayBlock, size);
        start();

    }

    @Override
    public void progress() {
        if (player.isSneaking() && !hasShot) {
            Location targetLoc = player.getEyeLocation().
                    add(player.getEyeLocation().getDirection().multiply(Math.max(radius + 1, loc.distance(player.getEyeLocation()))));
            if (loc.distance(targetLoc) > 1) {
                Vector dir = Vectors.getDirectionBetweenLocations(loc, targetLoc).normalize();
                loc.add(dir.clone().multiply(speed));
                smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), displayBlock, size);
            }

        } else if (hasShot) {
            if (loc.distance(player.getLocation()) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
                return;
            }
            loc.add(player.getEyeLocation().getDirection().multiply(speed));
            smash = Entities.handleDisplayBlockEntities(smash, Locations.getOutsideSphereLocs(loc, radius, size), displayBlock, size);
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
