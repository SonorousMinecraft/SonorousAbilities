package com.sereneoasis.airbending;

import com.sereneoasis.CoreAbility;
import com.sereneoasis.Element;
import com.sereneoasis.Methods;
import com.sereneoasis.SerenityPlayer;
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
        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.canBend(this))
        {
            return;
        }
        setFields();
        start();

    }

    private void setFields()
    {
        hasClicked = false;
        loc = player.getLocation();
        origin = loc.clone();
    }

    @Override
    public void progress() {
        if (hasClicked)
        {
            loc.add(dir.clone().multiply(0.5));
            loc.getWorld().spawnParticle(Particle.CLOUD,loc.getX(),loc.getY(),loc.getZ(),5);
            Entity hit = Methods.getAffected(loc, 1);
            if (hit != null)
            {
                hit.setVelocity(dir.clone());
            }
            if (loc.distance(origin) > 20)
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
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public long getCooldown() {
        return 10000;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "AirBlast";
    }

    @Override
    public String getInstructions() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Element getElement() {
        return Element.AIR;
    }
}
