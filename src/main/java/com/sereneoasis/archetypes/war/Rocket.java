package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.items.ShootItemDisplay;
import com.sereneoasis.abilityuilities.items.ThrowItemDisplay;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;

public class Rocket extends CoreAbility {
    private final String name = "Rocket";

    private ArmorStand armorStand;

    private ShootItemDisplay shootItemDisplay;

    private Location origin;
    public Rocket(Player player) {
        super(player);

//        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
//            return;
//        }

        origin = player.getEyeLocation().clone();
        shootItemDisplay = new ShootItemDisplay(player, name,origin , origin.getDirection().clone(), Material.FIREWORK_ROCKET, 3, false, false);
        armorStand = shootItemDisplay.getArmorStand();
        start();
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (armorStand.getLocation().distance(origin) > range) {
            this.remove();
            sPlayer.addCooldown(name, cooldown);
            shootItemDisplay.remove();
        }

    }

    public void setHasClicked()
    {
        Particles.spawnParticle(Particle.EXPLOSION_HUGE, armorStand.getLocation(), 10, 0.2, 0);
        for (Entity e : Entities.getEntitiesAroundPoint(armorStand.getLocation(), hitbox)) {
            if (e instanceof LivingEntity target)
            {
                DamageHandler.damageEntity(target, player, this, damage);
            }
        }
        this.remove();
        sPlayer.addCooldown(name, cooldown);
        shootItemDisplay.remove();
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
