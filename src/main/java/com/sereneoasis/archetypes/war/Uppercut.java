package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.*;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Uppercut extends CoreAbility {

    private final String name = "Uppercut";
    private LivingEntity target;

    private Location origin;
    public Uppercut(Player player, LivingEntity target) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        this.target = target;
        origin = player.getEyeLocation().clone();
        //player.teleport(player.getEyeLocation());
        DamageHandler.damageEntity(target, player, this, damage);

        start();

    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (player.getEyeLocation().distance(origin) > range) {
            this.remove();
            sPlayer.addCooldown(name, cooldown);
        }

        Vector dir = Vectors.getDirectionBetweenLocations(player.getEyeLocation(), target.getEyeLocation());
        double distance = dir.length();
        dir.subtract(new Vector(0,10*Math.log(distance + 1) - 10, 0));
        dir.normalize();



        player.setVelocity(dir.clone().multiply(speed));
        Particles.spawnParticle(Particle.ELECTRIC_SPARK, Locations.getMainHandLocation(player), 10, 0.2, 0);
        PacketUtils.playRiptide(player, 1);



        if (player.getEyeLocation().distance(target.getEyeLocation()) <= hitbox ) {
            Particles.spawnParticle(Particle.EXPLOSION_NORMAL, Locations.getMainHandLocation(player), 10, 0.2, 0);
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
