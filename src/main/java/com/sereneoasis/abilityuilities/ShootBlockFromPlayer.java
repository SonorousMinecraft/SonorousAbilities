package com.sereneoasis.abilityuilities;

import com.sereneoasis.Methods;
import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ShootBlockFromPlayer extends CoreAbility {

    private Location loc;
    private CoreAbility user;

    private Material type;

    private boolean directable;

    private TempBlock tb;

    private Vector dir;

    public ShootBlockFromPlayer(Player player, CoreAbility user, Location startLoc, Material type, boolean directable) {
        super(player);
        this.user = user;
        this.type = type;
        this.loc = startLoc;
        this.directable = directable;
        this.dir = player.getEyeLocation().getDirection().normalize();
    }

    @Override
    public void progress() {

        if (loc.distance(player.getEyeLocation()) > range) {
            this.remove();
        }

        tb = new TempBlock(loc.getBlock(), Material.WATER.createBlockData(), 1000);
        if (directable) {
            dir = player.getEyeLocation().getDirection().normalize();
        }

        loc.add(dir.clone().multiply(speed));
        DamageHandler.damageEntity(Methods.getAffected(loc, radius, player), player, user, damage);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return user.getName();
    }
}
