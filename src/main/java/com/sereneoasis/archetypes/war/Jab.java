package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Location;
import org.bukkit.Particle;
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
            DamageHandler.damageEntity(target, player, this, damage);
            start();
        }

    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (player.getEyeLocation().distance(origin) > range) {
            this.remove();
            sPlayer.addCooldown(name, cooldown);
        }

        player.setVelocity(dir.clone().multiply(speed));
        Particles.spawnParticle(Particle.ELECTRIC_SPARK, Locations.getMainHandLocation(player), 10, 0.2, 0);

        if (player.getEyeLocation().distance(target.getEyeLocation()) < hitbox + 3) {
            Particles.spawnParticle(Particle.EXPLOSION_HUGE, Locations.getMainHandLocation(player), 10, 0.2, 0);
            Vector orth = Vectors.getDirectionBetweenLocations(Locations.getLeftSide(player.getEyeLocation(), 0.5), Locations.getRightSide(player.getEyeLocation(), 0.5));
            dir.rotateAroundAxis(orth, -Math.toRadians(player.getEyeLocation().getPitch()));
            target.setVelocity(dir.clone().multiply(speed * 3));
            player.setVelocity(new Vector(0, 0, 0));
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
