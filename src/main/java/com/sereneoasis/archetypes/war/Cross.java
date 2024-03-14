package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.*;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftSpider;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Cross extends CoreAbility {

    private final String name = "Cross";
    private LivingEntity target;

    private Location origin;
    private Vector dir;

    public Cross(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }


        target = Entities.getFacingEntity(player, range, hitbox);
        origin = player.getEyeLocation().clone();
        dir = origin.getDirection().clone().normalize();
        if (target != null) {
            player.teleport(player.getEyeLocation());
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
        PacketUtils.playRiptide(player, 1);


        if (player.getEyeLocation().distance(target.getEyeLocation()) <= hitbox) {
            Particles.spawnParticle(Particle.EXPLOSION_NORMAL, Locations.getMainHandLocation(player), 10, 0.2, 0);
            Vector orth = Vectors.getDirectionBetweenLocations(Locations.getLeftSide(player.getEyeLocation(), 0.5), Locations.getRightSide(player.getEyeLocation(), 0.5));
            dir.rotateAroundAxis(orth, -Math.toRadians(player.getEyeLocation().getPitch()));
            target.setVelocity(dir.clone().multiply(speed));
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
