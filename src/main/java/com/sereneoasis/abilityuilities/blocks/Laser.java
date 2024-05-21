package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Basic particle based Laser ability
 */
public class Laser extends CoreAbility {


    private Location loc;
    private Vector dir;

    private String name;

    private DisplayBlock displayBlock;


    public Laser(Player player, Location startLoc, String name, DisplayBlock displayBlock) {
        super(player, name);

        this.name = name;
        this.displayBlock = displayBlock;
        this.loc = startLoc.clone();
        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }

    public void setLoc(Location newLoc) {
        this.loc = newLoc;
    }

    public Location getLoc() {
        return loc;
    }

    @Override
    public void progress() {
        if (System.currentTimeMillis() > startTime + duration) {
            this.abilityStatus = AbilityStatus.COMPLETE;
        }
        if (abilityStatus == AbilityStatus.SHOT) {

            dir = player.getEyeLocation().getDirection().normalize();

            double distance = range;
            LivingEntity entity = Entities.getFacingEntity(player, range, hitbox);
            if (entity != null) {
                DamageHandler.damageEntity(entity, player, this, damage);
                distance = entity.getLocation().distance(loc);
            }

            for (double d = size; d < distance; d+=size) {
                new TempDisplayBlock(loc.clone().add(dir.clone().multiply(d)), displayBlock, 100, size);
                if (!loc.clone().add(dir.clone().multiply(d)).getBlock().isPassable()) {
                    break;
                }
            }
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
