package com.sereneoasis.util;

import com.sereneoasis.ability.superclasses.CoreAbility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 * Handles where abilities damage entities
 */
public class DamageHandler {

    public static void damageEntity(Entity entity, Player source, CoreAbility ability, double damage) {
        if (entity != null) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.damage(damage, source);
                //Event abilityDamageEntityEvent = new AbilityDamageEntityEvent(source, livingEntity, ability, damage);
                //Bukkit.getServer().getPluginManager().callEvent(abilityDamageEntityEvent);
            }
        }
    }
}
