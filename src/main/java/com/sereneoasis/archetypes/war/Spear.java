package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.items.ShootItemDisplay;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Spear extends CoreAbility {

    public final String name = "Spear";

    private ShootItemDisplay spear;

    private Location origin;

    public Spear(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        origin = player.getEyeLocation().clone();
        spear = new ShootItemDisplay(player, name, origin, origin.getDirection().clone(), Material.ARROW, size/3, size*2, true, false, false);
        abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (spear.getAbilityStatus() == AbilityStatus.SHOT){
            if (Entities.getAffected(spear.getArmorStand().getLocation(), hitbox, player) instanceof LivingEntity livingEntity) {
                livingEntity.setVelocity(spear.getArmorStand().getVelocity());
                DamageHandler.damageEntity(livingEntity, player, this, damage);
                spear.setAbilityStatus(AbilityStatus.COMPLETE);
            }
        }
        if (spear.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();

        }

    }

    @Override
    public void remove() {
        super.remove();
        spear.remove();
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
