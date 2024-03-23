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

public class Hook extends CoreAbility {

    private final String name = "Hook";
    private LivingEntity target;

    private Location origin;

    private boolean hasShifted = false;

    public Hook(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }


        target = Entities.getFacingEntity(player, range, hitbox);
        origin = player.getEyeLocation().clone();
        if (target != null) {

            //player.teleport(player.getEyeLocation());
            start();
        }

    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (!hasShifted) {
            if (player.isSneaking()) {
                hasShifted = true;
            }

            if (player.getEyeLocation().distance(origin) > range) {
                this.remove();
                sPlayer.addCooldown(name, cooldown);
            }

            Location goalLoc = target.getLocation().add(Vectors.getVectorToMainHand(player));

            Vector dir = Vectors.getDirectionBetweenLocations(player.getLocation(), goalLoc);
            player.setVelocity(dir.clone().multiply(speed));
            PacketUtils.lookAtEntity(player, target);
            Particles.spawnParticle(Particle.ELECTRIC_SPARK, Locations.getMainHandLocation(player), 10, 0.2, 0);
            //PacketUtils.playRiptide(player, 20);


            if (player.getEyeLocation().distance(target.getEyeLocation()) <= hitbox + 0.5  ) {
                DamageHandler.damageEntity(target, player, this, damage);
                Particles.spawnParticle(Particle.EXPLOSION_NORMAL, Locations.getMainHandLocation(player), 10, 0.2, 0);
                Vector orth = Vectors.getDirectionBetweenLocations(Locations.getLeftSide(player.getEyeLocation(), 0.5), Locations.getRightSide(player.getEyeLocation(), 1));
                dir.rotateAroundAxis(orth, -Math.toRadians(player.getEyeLocation().getPitch()));
                target.setVelocity(dir.clone().multiply(speed));
                player.setVelocity(new Vector(0, 0, 0));
                this.remove();
                sPlayer.addCooldown(name, cooldown);
            }
        }
        else{
            if (CoreAbility.hasAbility(player, Uppercut.class)){
                this.remove();
                sPlayer.addCooldown(name, cooldown);
            }
        }
    }

    public LivingEntity getTarget() {
        return target;
    }

    @Override
    public void remove() {
        super.remove();
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
