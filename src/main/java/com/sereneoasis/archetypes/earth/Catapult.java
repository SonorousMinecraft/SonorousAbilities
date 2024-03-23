package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Catapult extends CoreAbility {

    private final String name = "Catapult";
    private boolean hasJumped = false;


    public Catapult(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        abilityStatus = AbilityStatus.CHARGING;
        start();
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (abilityStatus == AbilityStatus.CHARGING) {
            if (startTime + chargeTime < System.currentTimeMillis()) {
                abilityStatus = AbilityStatus.CHARGED;
            }

            if (!player.isSneaking()) {
                this.remove();
            }
        }

        AbilityUtils.showCharged(this);

        if (abilityStatus == AbilityStatus.CHARGED && !player.isSneaking()) {
            if (!hasJumped) {
                Entities.setVelocity(player, (float) speed, 0);
                hasJumped = true;
                this.remove();
            }

        }

    }


    @Override
    public void remove() {
        super.remove();
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
