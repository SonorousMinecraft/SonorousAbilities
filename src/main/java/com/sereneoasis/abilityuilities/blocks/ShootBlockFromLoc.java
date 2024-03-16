package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Allows the shooting of a block from a player
 */
public class ShootBlockFromLoc extends CoreAbility {

    private Location loc;
    private String user;

    private boolean directable, autoRemove;

    private Vector dir;

    private TempDisplayBlock block;


    public ShootBlockFromLoc(Player player, String user, Location startLoc, Material type, boolean directable, boolean autoRemove) {
        super(player, user);
        this.user = user;
        this.loc = startLoc;
        this.directable = directable;
        this.autoRemove = autoRemove;
        this.dir = player.getEyeLocation().getDirection().normalize();
        this.abilityStatus = AbilityStatus.SHOT;

        block = new TempDisplayBlock(loc, type, 60000, size);
        abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() {


        if (abilityStatus == AbilityStatus.SHOT) {

            loc.add(dir.clone().multiply(speed));
            block.moveTo(loc);

            if (directable) {
                dir = player.getEyeLocation().getDirection().normalize();
            }

            DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, player), player, this, damage);


            if (loc.distance(player.getEyeLocation()) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
                if (autoRemove) {
                    this.remove();
                }
            }

        }

    }

    @Override
    public void remove() {
        super.remove();
        block.revert();
    }

    public Location getLoc() {
        return loc;
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
