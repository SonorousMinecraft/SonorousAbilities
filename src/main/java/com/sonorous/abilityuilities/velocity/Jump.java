package com.sonorous.abilityuilities.velocity;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.AbilityUtils;
import org.bukkit.entity.Player;

public class Jump extends CoreAbility {

    private final String name;
    private boolean charged;

    public Jump(Player player, String name, boolean charged) {
        super(player, name);
        this.charged = charged;
        this.name = name;

        if (!charged) {
            player.setVelocity(player.getEyeLocation().getDirection().clone().normalize().multiply(speed * 3));
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

                if (System.currentTimeMillis() > startTime + chargeTime) {
                    player.setVelocity(player.getEyeLocation().getDirection().clone().normalize().multiply(speed * 3));
                }

                abilityStatus = AbilityStatus.COMPLETE;
            } else if (System.currentTimeMillis() > startTime + chargeTime) {
                abilityStatus = AbilityStatus.CHARGED;
                AbilityUtils.showCharged(this);
            }
        }

    }


    @Override
    public String getName() {
        return name;
    }
}
