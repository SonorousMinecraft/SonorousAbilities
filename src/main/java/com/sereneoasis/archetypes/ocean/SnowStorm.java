package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SnowStorm extends CoreAbility {

    private static String name = "SnowStorm";

    private HashMap<Integer,TempDisplayBlock> explosion = new HashMap<>();

    private double currentRadius = 0;

    public SnowStorm(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name)) {
            return;
        }

        if (CoreAbility.hasAbility(player, Blizzard.class)) {
            CoreAbility.getAbility(player, Blizzard.class).remove();
            start();
        }
    }

    @Override
    public void progress() {

        currentRadius+=speed;
        if (currentRadius < radius)
        {
            explosion = Entities.handleDisplayBlockEntities(explosion, Locations.getOutsideSphereLocs(player.getLocation(), currentRadius, 0.5), DisplayBlock.SNOW, 0.5);
        }
        else{
            this.remove();
            sPlayer.addCooldown(name, cooldown);
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
