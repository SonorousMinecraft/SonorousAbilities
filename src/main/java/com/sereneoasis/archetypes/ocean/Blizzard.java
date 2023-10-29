package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Blizzard extends CoreAbility {

    private final String name = "Blizzard";

    private int currentShots = 0, maxShots = 5;

    private HashMap<Integer, TempDisplayBlock> dome;


    public Blizzard(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name))
        {
            return;
        }

        if (player.getLocation().clone().subtract(new Vector(0,1,0)).getBlock().getType() == Material.ICE)
        {

            dome = new HashMap<>();
            start();
        }
    }

    @Override
    public void progress() {
        if (!player.isSneaking())
        {
            this.remove();
        }
        if (maxShots == currentShots)
        {
            this.remove();
        }

        dome = Entities.handleDisplayBlockEntities(dome, Locations.getOutsideSphereLocs(player.getEyeLocation(), radius, 0.5), Material.ICE, 0.5);
    }

    public void setHasClicked()
    {
        currentShots++;
        Location tempLoc = player.getEyeLocation();
        Vector dir = tempLoc.getDirection().normalize();

        new ShootBlockFromLoc(player, name, tempLoc.add(dir.multiply(radius)), Material.ICE, true);
    }

    @Override
    public void remove()
    {
        super.remove();
        sPlayer.addCooldown(this.getName(), this.cooldown);
        for (TempDisplayBlock tb : dome.values())
        {
            tb.revert();
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
