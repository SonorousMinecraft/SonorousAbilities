package com.sereneoasis.abilityuilities.velocity;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Entities;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Levitate extends CoreAbility {

    private final String name;

    private state levitateState = state.ON;

    private enum state {
        OFF,
        ON

    }


    public Levitate(Player player, String name) {
        super(player, name);

        this.name = name;
        Entities.applyPotionPlayer(player, PotionEffectType.LEVITATION, Math.round(duration));
        abilityStatus = AbilityStatus.MOVING;
        start();
    }

    public Levitate(Player player, String name, long duration) {
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

    public void toggle(){
        if (levitateState == state.ON) {
            levitateState = state.OFF;
            player.removePotionEffect(PotionEffectType.LEVITATION);
        } else {
            levitateState = state.ON;
            Entities.applyPotionPlayer(player, PotionEffectType.LEVITATION, Math.round(duration - (System.currentTimeMillis() - startTime)));
        }
    }

    public boolean isLevitating(){
        if (levitateState == state.ON) {
            return  true;
        } else {
            return false;
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
