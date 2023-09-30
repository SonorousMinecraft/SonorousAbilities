package com.sereneoasis.airbending;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.Element;
import com.sereneoasis.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AirBlast extends AirAbility {

    private Location origin, loc;

    private Vector dir;

    private boolean hasClicked;

    public AirBlast(Player player) {
        super(player);
        if (CoreAbility.hasAbility(player, this.getClass()) || !sPlayer.canBend(this))
        {
            return;
        }

        setFields();
        start();

    }

    private void setFields()
    {
        hasClicked = false;
        loc = player.getEyeLocation();
        origin = loc.clone();
    }

    @Override
    public void progress() {
        loc.getWorld().spawnParticle(Particle.CLOUD,loc.getX(),loc.getY(),loc.getZ(),5);
        if (hasClicked)
        {
            loc.add(dir.clone().multiply(speed));
            Entity hit = Methods.getAffected(loc, radius, player);
            if (hit != null)
            {
                hit.setVelocity(dir.clone());
            }
            if (loc.distance(origin) > range)
            {
                this.remove();
            }
        }

    }

    public void setHasClicked()
    {
        if (!hasClicked) {
            hasClicked = true;
            dir = player.getLocation().getDirection().normalize();

        }
    }

    @Override
    public void remove() {
        super.remove();
    }


    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "AirBlast";
    }

}
