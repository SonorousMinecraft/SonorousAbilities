package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

/**
 * @author Sakrajin
 * Causes a spherical shaped blast to be shot from the player
 */
public class BlockSphereBlast extends CoreAbility {

    private boolean gravity;

    private Location loc, origin;
    private Vector dir;

    private String name;


    private HashMap<Integer, TempDisplayBlock> spike = new HashMap<>();


    public BlockSphereBlast(Player player, String name, Location startLoc, boolean gravity) {
        super(player, name);

        this.name = name;
        this.gravity = gravity;

        this.origin = player.getEyeLocation().clone();
        this.dir = origin.getDirection().normalize();
        this.loc = startLoc.clone();
        spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(loc, radius, 0.5),
                DisplayBlock.SUN, 0.5);
        this.abilityStatus = AbilityStatus.CHARGED;
        start();
    }


    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.SHOT) {
            if (loc.distance(origin) > range) {
                this.abilityStatus = AbilityStatus.COMPLETE;
            }

            if (gravity) {
                dir = dir.setY(dir.getY() - 0.01);
            }

            loc.add(dir.clone().multiply(speed));

            spike = Entities.handleDisplayBlockEntities(spike, Locations.getOutsideSphereLocs(loc, radius, 0.5),
                    DisplayBlock.SUN, 0.5);

            DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, player), player, this, damage);

        }
    }

    public void setAbilityStatus(AbilityStatus abilityStatus)
    {
        this.abilityStatus = abilityStatus;
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
        return name;
    }
}
