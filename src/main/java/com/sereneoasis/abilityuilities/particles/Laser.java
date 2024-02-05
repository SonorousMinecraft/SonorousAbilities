package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Particles;
import org.bukkit.Location;
import org.bukkit.Particle;
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

    private Particle particle;

    public Laser(Player player, Location startLoc, String name, Particle particle) {
        super(player, name);

        this.name = name;
        this.particle = particle;
        this.loc = startLoc.clone();
        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }

    public void setLoc(Location newLoc) {
        this.loc = newLoc;
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

            for (double d = 0; d < distance; d++) {
                Particles.spawnParticle(particle, loc.clone().add(dir.clone().multiply(d)), 1, 0, 0);
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
