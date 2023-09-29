package com.sereneoasis.util;

import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.events.AbilityDamageEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


public class DamageHandler {

    public static void damageEntity( Entity entity, Player source, CoreAbility ability, double damage)
    {
        if (entity instanceof LivingEntity livingEntity)
        {
            livingEntity.damage(damage, source);
        }
    }
}
