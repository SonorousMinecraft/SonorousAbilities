package com.sereneoasis.abilityuilities.velocity;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Entities;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Levitate extends CoreAbility {

    private final String name;


    public Levitate(Player player, String name) {
        super(player, name);

        this.name = name;
        Entities.applyPotionPlayer(player, PotionEffectType.LEVITATION, Math.round(duration));
        abilityStatus = AbilityStatus.MOVING;
        start();
    }

    @Override
    public void progress() {

        if (System.currentTimeMillis() > startTime + (duration)) {
            abilityStatus = AbilityStatus.COMPLETE;
        }

    }

    @Override
    public void remove() {
        super.remove();
        player.removePotionEffect(PotionEffectType.LEVITATION);
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
