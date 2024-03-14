package com.sereneoasis.archetypes.war;

import com.sereneoasis.Serenity;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.*;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.world.entity.HumanoidArm;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftSpider;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class FlickerJab extends CoreAbility {

    private final String name = "FlickerJab";
    private LivingEntity target;

    private Location origin;
    private Vector dir;

    public FlickerJab(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }


        origin = player.getEyeLocation().clone();
        dir = origin.getDirection().clone().normalize();
        target = Entities.getFacingEntity(player, range, hitbox);

        if (target != null) {
            PacketUtils.oneTwo(player);
            Particles.spawnParticle(Particle.ELECTRIC_SPARK, Locations.getMainHandLocation(player), 10, 0.2, 0);
            DamageHandler.damageEntity(target, player, this, damage/2);

            player.setVelocity(Vectors.getVectorToMainHand(player).multiply(speed));

            BukkitScheduler scheduler = Bukkit.getScheduler();


            scheduler.runTaskLater(Serenity.getPlugin(), () -> {
                Particles.spawnParticle(Particle.ELECTRIC_SPARK, Locations.getOffHandLocation(player), 10, 0.2, 0);
                DamageHandler.damageEntity(target, player, this, damage/2);

                player.setVelocity(Vectors.getVectorToOffHand(player).multiply(speed));

            }, 10L /*<-- the delay */);
            start();
        }

    }

    @Override
    public void progress() throws ReflectiveOperationException {

        this.remove();
        sPlayer.addCooldown(name, cooldown);
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
