package com.sonorous.archetypes.chaos;

import com.sonorous.ability.superclasses.MasterAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.enhancedmethods.EnhancedDisplayBlocks;
import com.sonorous.util.enhancedmethods.EnhancedSchedulerEffects;
import com.sonorous.util.methods.*;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AbyssalFall extends MasterAbility {

    private static final String name = "AbyssalFall";

    private Vector dir;

    private Set<TempDisplayBlock> tempDisplayBlocks = new HashSet<>();


    public AbyssalFall(Player player) {
        super(player, name);

        if (shouldStart()) {
            player.setVelocity(new Vector(0, 3 * speed, 0));
            Particles.spawnParticle(Particle.SONIC_BOOM, player.getLocation(), 5, 3, 0);

            Entities.applyPotionPlayer(player, PotionEffectType.SLOW_FALLING, 60000);
            abilityStatus = AbilityStatus.CHARGED;
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (abilityStatus == AbilityStatus.MOVING) {
            PacketUtils.playRiptide(player, 20);
            player.setVelocity(dir.subtract(new Vector(0, Constants.GRAVITY * speed * 2, 0)).clone().multiply(speed));
            if (Blocks.isSolid(player.getLocation().subtract(0, 1, 0))) {

//                Blocks.getBlocksAroundPoint(player.getLocation(), radius).forEach(b -> {
//                    TempBlock tb = new TempBlock(b, Material.AIR, duration, true);
//                });

//                new BlockDisintegrateSphereSuck(player, name, player.getLocation(), player.getLocation().add(0,radius, 0), 0, speed);

                tempDisplayBlocks = EnhancedDisplayBlocks.createTopCircleTempBlocks(this, Material.BLACK_CONCRETE);
                List<Entity> targets = Entities.getEntitiesAroundPoint(player.getLocation(), radius).stream().filter(entity -> entity != player).collect(Collectors.toList());
                targets.forEach(entity -> entity.teleport(entity.getLocation().add(0, radius * 2, 0)));
                Scheduler.performTaskLater(20, () -> {
                    targets.stream().filter(entity -> entity != player).collect(Collectors.toSet())
                            .forEach(entity -> {
                                Particles.spawnParticle(Particle.SONIC_BOOM, entity.getLocation(), 5, 3, 0);
                                entity.setVelocity(new Vector(0, -speed, 0));
//                                        new BlockDisintegrateSphereSuck(player, name, entity.getLocation().subtract(0, radius * 2, 0), entity.getLocation(), 0, radius / 8, 1);

                            });
                });

                EnhancedSchedulerEffects.raiseTDBs(tempDisplayBlocks, 50, 1);
                abilityStatus = AbilityStatus.SHOT;
                player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                this.remove();
            }
        }

    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.CHARGED) {
            abilityStatus = AbilityStatus.MOVING;
            this.dir = player.getEyeLocation().getDirection();
            Particles.spawnParticle(Particle.SONIC_BOOM, player.getLocation(), 20, 2, 1);

        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);

        EnhancedSchedulerEffects.clearTDBs(tempDisplayBlocks, 50);
    }

    @Override
    public String getName() {
        return name;
    }
}
