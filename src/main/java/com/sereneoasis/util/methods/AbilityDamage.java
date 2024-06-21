package com.sereneoasis.util.methods;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.DamageHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class AbilityDamage {

    public static boolean damageOne(Location loc, CoreAbility coreAbility, Player player, boolean kb, @Nullable Vector dir) {
        Entity target = Entities.getAffected(loc, coreAbility.getHitbox(), player);

        if (target != null) {
            DamageHandler.damageEntity(target, player, coreAbility, coreAbility.getDamage());
            if (kb) {
                target.setVelocity(dir.clone().multiply(coreAbility.getSpeed()));
            }
            return true;
        }
        return false;
    }

    public static boolean damageSeveral(Location loc, CoreAbility coreAbility, Player player, boolean kb, @Nullable Vector dir) {
        List<LivingEntity> livingEntities = Entities.getAffectedList(loc, coreAbility.getHitbox(), player);
        livingEntities.forEach(target -> {
            DamageHandler.damageEntity(target, player, coreAbility, coreAbility.getDamage());
            if (kb) {
                target.setVelocity(dir.clone().multiply(coreAbility.getSpeed()));
            }
        });
        return !livingEntities.isEmpty();
    }

    public static List<LivingEntity> damageSeveralReturnHit(Location loc, CoreAbility coreAbility, Player player, boolean kb, @Nullable Vector dir) {
        List<LivingEntity> livingEntities = Entities.getAffectedList(loc, coreAbility.getHitbox(), player);
        livingEntities.forEach(target -> {
            DamageHandler.damageEntity(target, player, coreAbility, coreAbility.getDamage());
            if (kb) {
                target.setVelocity(dir.clone().multiply(coreAbility.getSpeed()));
            }
        });
        return livingEntities;
    }

    public static List<LivingEntity> damageSeveralExceptReturnHit(Location loc, CoreAbility coreAbility, Player player, Set<LivingEntity> except, boolean kb, @Nullable Vector dir) {
        List<LivingEntity> livingEntities = Entities.getAffectedList(loc, coreAbility.getHitbox(), player);

        livingEntities.stream().filter(livingEntity -> except == null || !except.contains(livingEntity)).forEach(target -> {
            DamageHandler.damageEntity(target, player, coreAbility, coreAbility.getDamage());
            if (kb) {
                target.setVelocity(dir.clone().multiply(coreAbility.getSpeed()));
            }
        });
        return livingEntities;
    }

    public static boolean damageSeveralExcept(Location loc, CoreAbility coreAbility, Player player, Set<LivingEntity> except, boolean kb, @Nullable Vector dir) {
        List<LivingEntity> livingEntities = Entities.getAffectedList(loc, coreAbility.getHitbox(), player);
        if (except != null) {
            livingEntities.removeIf(except::contains);
        }
        livingEntities.forEach(target -> {
            DamageHandler.damageEntity(target, player, coreAbility, coreAbility.getDamage());
            if (kb) {
                target.setVelocity(dir.clone().multiply(coreAbility.getSpeed()));
            }
        });
        return !livingEntities.isEmpty();
    }
}
