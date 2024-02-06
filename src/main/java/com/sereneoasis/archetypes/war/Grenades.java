package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.items.ThrowItemDisplay;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Particles;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Grenades extends CoreAbility {

    private final String name = "Grenades";

    private HashMap<ThrowItemDisplay, Long> grenades = new HashMap<>();

    private int currentShots = 0, shots = 6;

    public Grenades(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        start();
        setHasClicked();
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        Iterator<Map.Entry<ThrowItemDisplay, Long>> it = grenades.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ThrowItemDisplay, Long> instance = it.next();
            ThrowItemDisplay grenade = instance.getKey();
            long explodeTime = instance.getValue();

            if (System.currentTimeMillis() > explodeTime) {
                Particles.spawnParticle(Particle.EXPLOSION_HUGE, grenade.getLoc(), 10, 0.2, 0);
                for (Entity e : Entities.getEntitiesAroundPoint(grenade.getLoc(), hitbox)) {
                    if (e instanceof LivingEntity target) {
                        DamageHandler.damageEntity(target, player, this, damage);
                    }
                }
                grenade.remove();
                it.remove();
            }
        }

        if (currentShots == shots && grenades.isEmpty()) {
            this.remove();
            sPlayer.addCooldown(name, cooldown);
        }

        AbilityUtils.showShots(this, currentShots, shots);

    }

    public void setHasClicked() {
        if (player.isSneaking()) {
            Iterator<ThrowItemDisplay> it = grenades.keySet().iterator();
            while (it.hasNext()) {
                ThrowItemDisplay grenade = it.next();
                Particles.spawnParticle(Particle.EXPLOSION_HUGE, grenade.getLoc(), 10, 0.2, 0);
                for (Entity e : Entities.getEntitiesAroundPoint(grenade.getLoc(), hitbox)) {
                    if (e instanceof LivingEntity target) {
                        DamageHandler.damageEntity(target, player, this, damage);
                    }
                }
                grenade.remove();
                it.remove();
            }
        } else if (currentShots < shots) {
            grenades.put(new ThrowItemDisplay(player, name, player.getEyeLocation(),
                    player.getEyeLocation().getDirection().clone(), Material.FIREWORK_STAR, 1, true, true), System.currentTimeMillis() + chargeTime);
            currentShots++;
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
