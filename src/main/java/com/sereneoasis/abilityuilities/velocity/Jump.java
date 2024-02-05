package com.sereneoasis.abilityuilities.velocity;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Player;

public class Jump extends CoreAbility {

    private boolean charged;
    private final String name;

    public Jump(Player player, String name, boolean charged) {
        super(player, name);
        this.charged = charged;
        this.name = name;

        if (!charged) {
            player.setVelocity(player.getEyeLocation().getDirection().clone().normalize().multiply(speed));
            abilityStatus = AbilityStatus.COMPLETE;
        } else {
            abilityStatus = AbilityStatus.CHARGING;
            start();
        }
    }

    @Override
    public void progress() {
        if (abilityStatus != AbilityStatus.COMPLETE) {
            if (!player.isSneaking()) {
                abilityStatus = AbilityStatus.COMPLETE;
            } else if (System.currentTimeMillis() > startTime + chargeTime) {
                player.setVelocity(player.getEyeLocation().getDirection().clone().normalize().multiply(speed));
                abilityStatus = AbilityStatus.COMPLETE;
            }
        }

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
