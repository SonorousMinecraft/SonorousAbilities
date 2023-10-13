package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.Methods;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ShootBlockFromPlayer extends CoreAbility {

    private Location loc;
    private String user;

    private Material type;

    private boolean directable;


    private Vector dir;

    private AbilityStatus abilityStatus;

    public ShootBlockFromPlayer(Player player, String user, Location startLoc, Material type, boolean directable) {
        super(player, user);
        this.user = user;
        this.type = type;
        this.loc = startLoc;
        this.directable = directable;
        this.dir = player.getEyeLocation().getDirection().normalize();
        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() {


        if (loc.distance(player.getEyeLocation()) > range) {
            abilityStatus = AbilityStatus.COMPLETE;
            return;
        }


        //new TempBlock(loc.getBlock(), Material.WATER.createBlockData(), 500);
        new TempDisplayBlock(loc, type.createBlockData(), 500, radius);
        if (directable) {
            dir = player.getEyeLocation().getDirection().normalize();
        }

        loc.add(dir.clone().multiply(speed));
        DamageHandler.damageEntity(Methods.getAffected(loc, radius, player), player, this, damage);
    }

    public AbilityStatus getAbilityStatus() {
        return abilityStatus;
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
