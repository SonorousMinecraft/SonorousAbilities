package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.items.ShootItemDisplay;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Particles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Rocket extends CoreAbility {
    private final String name = "Rocket";


    private ShootItemDisplay rocket;

    private Location origin;

    public Rocket(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        origin = player.getEyeLocation().clone();
        rocket = new ShootItemDisplay(player, name, origin, origin.getDirection().clone(), Material.FIREWORK_ROCKET, size/2, size * 1.5,false, false, false);
        start();
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (rocket.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
            sPlayer.addCooldown(name, cooldown);
            rocket.remove();
        }

    }

    public void setHasClicked() {
        Particles.spawnParticle(Particle.EXPLOSION_HUGE, rocket.getArmorStand().getLocation(), 10, 0.2, 0);
        for (Entity e : Entities.getEntitiesAroundPoint(rocket.getArmorStand().getLocation(), hitbox)) {
            if (e instanceof LivingEntity target) {
                DamageHandler.damageEntity(target, player, this, damage);
            }
        }
        this.remove();
        sPlayer.addCooldown(name, cooldown);
        rocket.remove();
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
