package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.*;
import org.apache.logging.log4j.core.Core;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftSpider;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Jab extends CoreAbility {

    private final String name = "Jab";
    private LivingEntity target;

    private Location origin;
    private Vector dir;

    public Jab(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }


        target = Entities.getFacingEntity(player, range, hitbox);
        origin = player.getEyeLocation().clone();
        dir = origin.getDirection().clone().normalize();
        if (target != null) {
            Particles.spawnParticle(Particle.ELECTRIC_SPARK, Locations.getMainHandLocation(player), 10, 0.2, 0);
            DamageHandler.damageEntity(target, player, this, damage);
            //player.teleport(player.getEyeLocation());
            player.setVelocity(Vectors.getVectorToMainHand(player).multiply(speed));
            start();
        }

    }

    @Override
    public void progress() throws ReflectiveOperationException {

        this.remove();
        sPlayer.addCooldown(name, cooldown);
    }

    public LivingEntity getTarget() {
        return target;
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
