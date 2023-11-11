package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.velocity.Jet;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Daybreak extends CoreAbility {

    private final String name = "Daybreak";

    private Jet jet;
    public Daybreak(Player player) {
        super(player);

        jet = new Jet(player, name);
        start();
    }

    @Override
    public void progress() {

        if (!player.isSneaking() | jet.getAbilityStatus() == AbilityStatus.COMPLETE)
        {
            this.remove();
        }

        for (Entity e : Entities.getEntitiesAroundPoint(player.getEyeLocation(), hitbox)) {
            if (e != null && e.getUniqueId() != player.getUniqueId()) {
                DamageHandler.damageEntity(e, player, this, damage);
                Entities.applyPotion(e, PotionEffectType.BLINDNESS, Math.round(startTime - duration));
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        jet.remove();

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
