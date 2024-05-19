package com.sereneoasis.util;

import com.sereneoasis.ability.superclasses.CoreAbility;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.*;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author Sakrajin
 * Handles where abilities damage entities
 */
public class DamageHandler {
    static ResourceKey<DamageType> EATING_SHIT = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("eating_shit"));
    public static void damageEntity(Entity entity, Player source, CoreAbility ability, double damage) {
        if (entity != null) {
            if (entity instanceof LivingEntity livingEntity) {
                // needd to add some damage cooldown
                livingEntity.damage(damage, source);
//                net.minecraft.world.entity.player.Player nmsPlayer = ((CraftPlayer)source).getHandle();
//                net.minecraft.world.entity.LivingEntity nmsTarget = ((CraftLivingEntity)livingEntity).getHandle();
//                nmsTarget.setHealth((float) (nmsTarget.getHealth() - damage));


                //Event abilityDamageEntityEvent = new AbilityDamageEntityEvent(source, livingEntity, ability, damage);
                //Bukkit.getServer().getPluginManager().callEvent(abilityDamageEntityEvent);
            }
        }
    }
}
